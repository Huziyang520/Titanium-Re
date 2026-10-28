// Titanium Lite multi inventory component exposed to the NeoForge transfer api

package com.hrznstudio.titanium.component.inventory;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHandler;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MultiInventoryComponent<T extends IComponentHarness> implements IScreenAddonProvider, IContainerAddonProvider, IComponentHandler {

    private final LinkedHashSet<InventoryComponent<T>> inventoryHandlers;
    private final Map<FacingUtil.Sideness, Optional<MultiInvCapabilityHandler<T>>> lazyOptionals;

    public MultiInventoryComponent() {
        this.inventoryHandlers = new LinkedHashSet<>();
        this.lazyOptionals = new HashMap<>();
        this.lazyOptionals.put(null, Optional.empty());
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            this.lazyOptionals.put(value, Optional.empty());
        }
    }

    @Override
    public void add(Object... component) {
        Arrays.stream(component).filter(this::accepts).forEach(inventoryComponent -> {
            this.inventoryHandlers.add((InventoryComponent<T>) inventoryComponent);
            this.rebuildCapability(FacingUtil.Sideness.values());
        });
    }

    private boolean accepts(Object component) {
        return component instanceof InventoryComponent;
    }

    public void rebuildCapability(FacingUtil.Sideness[] sides) {
        for (FacingUtil.Sideness side : sides) {
            this.lazyOptionals.put(side, Optional.of(new MultiInvCapabilityHandler<>(this.getHandlersForSide(side))));
        }
    }

    private List<InventoryComponent<T>> getHandlersForSide(FacingUtil.Sideness sideness) {
        if (sideness == null) {
            return new ArrayList<>(this.inventoryHandlers);
        }
        List<InventoryComponent<T>> handlers = new ArrayList<>();
        for (InventoryComponent<T> inventoryHandler : this.inventoryHandlers) {
            if (inventoryHandler instanceof IFacingComponent facing) {
                if (facing.getFacingModes().containsKey(sideness) && facing.getFacingModes().get(sideness).allowsConnection()) {
                    handlers.add(inventoryHandler);
                }
            } else {
                handlers.add(inventoryHandler);
            }
        }
        return handlers;
    }

    @Nonnull
    public Optional<MultiInvCapabilityHandler<T>> getCapabilityForSide(FacingUtil.Sideness sideness) {
        return this.lazyOptionals.get(sideness);
    }

    public boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, int mode) {
        for (InventoryComponent<T> inventoryHandler : this.inventoryHandlers) {
            if (inventoryHandler.getName().equals(handlerName) && inventoryHandler instanceof IFacingComponent component) {
                component.getFacingModes().put(facing, component.getValidFacingModes()[mode]);
                this.rebuildCapability(new FacingUtil.Sideness[]{facing});
                return true;
            }
        }
        return false;
    }

    public HashSet<InventoryComponent<T>> getInventoryHandlers() {
        return this.inventoryHandlers;
    }

    @Override
    public List<IFactory<?>> getScreenAddons() {
        List<IFactory<?>> addons = new ArrayList<>();
        this.inventoryHandlers.forEach(handler -> addons.addAll(handler.getScreenAddons()));
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>();
        for (InventoryComponent<T> inventoryComponent : this.inventoryHandlers) {
            addons.addAll(inventoryComponent.getContainerAddons());
        }
        return addons;
    }

    public static class MultiInvCapabilityHandler<T extends IComponentHarness> implements ResourceHandler<ItemResource> {

        private final List<InventoryComponent<T>> inventoryHandlers;
        private final int slotAmount;

        public MultiInvCapabilityHandler(List<InventoryComponent<T>> inventoryHandlers) {
            this.inventoryHandlers = inventoryHandlers;
            int amount = 0;
            for (InventoryComponent<T> inventoryHandler : this.inventoryHandlers) {
                amount += inventoryHandler.getSlots();
            }
            this.slotAmount = amount;
        }

        @Override
        public int size() {
            return this.slotAmount;
        }

        @Override
        public int insert(int slot, ItemResource resource, int amount, TransactionContext transaction) {
            InventoryComponent<T> handler = this.getFromSlot(slot);
            if (handler == null || resource.isEmpty()) {
                return 0;
            }
            int relativeSlot = this.getRelativeSlot(handler, slot);
            ItemStack stack = resource.toStack(amount);
            ItemStack remaining = handler.insertItem(relativeSlot, stack, transaction == null);
            return amount - remaining.getCount();
        }

        @Override
        public int extract(int slot, ItemResource resource, int amount, TransactionContext transaction) {
            InventoryComponent<T> handler = this.getFromSlot(slot);
            if (handler == null || resource.isEmpty()) {
                return 0;
            }
            int relativeSlot = this.getRelativeSlot(handler, slot);
            ItemStack extracted = handler.extractItem(relativeSlot, amount, transaction == null);
            return extracted.getCount();
        }

        @Override
        public ItemResource getResource(int slot) {
            InventoryComponent<T> handler = this.getFromSlot(slot);
            if (handler == null) {
                return ItemResource.EMPTY;
            }
            return ItemResource.of(handler.getStackInSlot(this.getRelativeSlot(handler, slot)));
        }

        @Override
        public long getAmountAsLong(int slot) {
            InventoryComponent<T> handler = this.getFromSlot(slot);
            if (handler == null) {
                return 0;
            }
            return handler.getStackInSlot(this.getRelativeSlot(handler, slot)).getCount();
        }

        @Override
        public long getCapacityAsLong(int slot, ItemResource resource) {
            InventoryComponent<T> handler = this.getFromSlot(slot);
            if (handler == null) {
                return 0;
            }
            return handler.getSlotLimit(this.getRelativeSlot(handler, slot));
        }

        @Override
        public boolean isValid(int slot, ItemResource resource) {
            InventoryComponent<T> handler = this.getFromSlot(slot);
            if (handler == null || resource.isEmpty()) {
                return false;
            }
            return handler.isItemValid(this.getRelativeSlot(handler, slot), resource.toStack(1));
        }

        private InventoryComponent<T> getFromSlot(int slot) {
            int index = slot;
            for (InventoryComponent<T> inventoryHandler : this.inventoryHandlers) {
                int size = inventoryHandler.getSlots();
                if (index < size) {
                    return inventoryHandler;
                }
                index -= size;
            }
            return null;
        }

        private int getRelativeSlot(InventoryComponent<T> handler, int slot) {
            int index = slot;
            for (InventoryComponent<T> inventoryHandler : this.inventoryHandlers) {
                if (inventoryHandler == handler) {
                    return index;
                }
                index -= inventoryHandler.getSlots();
            }
            return index;
        }
    }
}
