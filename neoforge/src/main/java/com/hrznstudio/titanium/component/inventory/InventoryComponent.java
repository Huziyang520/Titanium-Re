// Titanium Lite Inventory Component

package com.hrznstudio.titanium.component.inventory;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.container.addon.SlotContainerAddon;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class InventoryComponent<T extends IComponentHarness> extends ItemStacksResourceHandler implements IScreenAddonProvider,
        IContainerAddonProvider {

    public InventoryComponent(String name, int xPos, int yPos, int size) {
        super(size);
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.setRange(size, 1);
        this.insertPredicate = (stack, integer) -> true;
        this.extractPredicate = (stack, integer) -> true;
        this.onSlotChanged = (stack, integer) -> {
        };
        this.slotAmountFilter = new HashMap<>();
        this.slotToStackRenderMap = new HashMap<>();
        this.colorGuiEnabled = false;
        this.slotToColorRenderMap = new HashMap<>();
        this.slotLimit = 64;
        this.slotPosition = integer -> Pair.of(18 * (integer % xSize), 18 * (integer / xSize));
        this.slotVisiblePredicate = integer -> true;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return com.hrznstudio.titanium.util.ValueIOSerialization.save(provider, this);
    }

    private final String name;
    private int xPos;
    private int yPos;
    private int xSize;
    private int ySize;
    private T componentHarness;
    private BiPredicate<ItemStack, Integer> insertPredicate;
    private BiPredicate<ItemStack, Integer> extractPredicate;
    private BiConsumer<ItemStack, Integer> onSlotChanged;
    private Map<Integer, Integer> slotAmountFilter;
    private Map<Integer, ItemStack> slotToStackRenderMap;
    private boolean colorGuiEnabled;
    private Map<Integer, Color> slotToColorRenderMap;
    private int slotLimit;
    private Function<Integer, Pair<Integer, Integer>> slotPosition;
    private Predicate<Integer> slotVisiblePredicate;

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        com.hrznstudio.titanium.util.ValueIOSerialization.load(provider, tag, this);
    }

    public InventoryComponent<T> setRange(int x, int y) {
        this.xSize = x;
        this.ySize = y;
        return this;
    }

    public InventoryComponent<T> setComponentHarness(T componentHarness) {
        this.componentHarness = componentHarness;
        return this;
    }

    public InventoryComponent<T> setInputFilter(BiPredicate<ItemStack, Integer> predicate) {
        this.insertPredicate = predicate;
        return this;
    }

    public InventoryComponent<T> setOutputFilter(BiPredicate<ItemStack, Integer> predicate) {
        this.extractPredicate = predicate;
        return this;
    }

    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        try (var transaction = Transaction.openRoot()) {
            int inserted = insert(slot, ItemResource.of(stack), stack.getCount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return stack.copyWithCount(stack.getCount() - inserted);
        }
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemResource resource = getResource(slot);
        if (resource.isEmpty()) return ItemStack.EMPTY;
        try (var transaction = Transaction.openRoot()) {
            int extracted = extract(slot, resource, amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return resource.toStack(extracted);
        }
    }

    @Override
    protected void onContentsChanged(int slot, ItemStack previousContents) {
        if (this.componentHarness != null) {
            componentHarness.markComponentDirty();
        }
        onSlotChanged.accept(getStackInSlot(slot), slot);
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        ItemStack stack = resource.toStack(Math.min(amount, getAmountAsInt(index)));
        return extractPredicate.test(stack, index) ? super.extract(index, resource, amount, transaction) : 0;
    }

    public int getSlots() {
        return size();
    }

    public ItemStack getStackInSlot(int slot) {
        ItemResource resource = getResource(slot);
        return resource.isEmpty() ? ItemStack.EMPTY : resource.toStack(getAmountAsInt(slot));
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        set(slot, ItemResource.of(stack), stack.getCount());
    }

    public String getName() {
        return name;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public T getComponentHarness() {
        return componentHarness;
    }

    public BiPredicate<ItemStack, Integer> getInsertPredicate() {
        return insertPredicate;
    }

    public BiPredicate<ItemStack, Integer> getExtractPredicate() {
        return extractPredicate;
    }

    public BiConsumer<ItemStack, Integer> getOnSlotChanged() {
        return onSlotChanged;
    }

    public InventoryComponent<T> setColorGuiEnabled(boolean colorGuiEnabled) {
        this.colorGuiEnabled = colorGuiEnabled;
        return this;
    }

    public boolean isColorGuiEnabled() {
        return colorGuiEnabled;
    }

    public Map<Integer, Color> getSlotToColorRenderMap() {
        return slotToColorRenderMap;
    }

    public InventoryComponent<T> setOnSlotChanged(BiConsumer<ItemStack, Integer> onSlotChanged) {
        this.onSlotChanged = onSlotChanged;
        return this;
    }

    public InventoryComponent<T> setSlotLimit(int slot, int limit) {
        this.slotAmountFilter.put(slot, limit);
        return this;
    }

    public InventoryComponent<T> setSlotToItemStackRender(int slot, ItemStack stack) {
        this.slotToStackRenderMap.put(slot, stack);
        return this;
    }

    public InventoryComponent<T> setSlotToColorRender(int slot, int color) {
        if (!this.colorGuiEnabled) {
            this.setColorGuiEnabled(true);
        }
        this.slotToColorRenderMap.put(slot, new Color(color));
        return this;
    }

    public InventoryComponent<T> setSlotToColorRender(int slot, DyeColor color) {
        if (!this.colorGuiEnabled) {
            this.setColorGuiEnabled(true);
        }
        this.slotToColorRenderMap.put(slot, new Color(color.getMapColor().col));
        return this;
    }

    public InventoryComponent<T> setSlotToColorRender(int slot, Color color) {
        if (!this.colorGuiEnabled) {
            this.setColorGuiEnabled(true);
        }
        this.slotToColorRenderMap.put(slot, color);
        return this;
    }

    public ItemStack getItemStackForSlotRendering(int slot) {
        return this.slotToStackRenderMap.getOrDefault(slot, ItemStack.EMPTY);
    }

    public Color getColorForSlotRendering(int slot) {
        return this.slotToColorRenderMap.get(slot);
    }

    public InventoryComponent<T> setSlotLimit(int limit) {
        this.slotLimit = limit;
        return this;
    }

    public Predicate<Integer> getSlotVisiblePredicate() {
        return slotVisiblePredicate;
    }

    public InventoryComponent<T> setSlotVisiblePredicate(Predicate<Integer> slotVisiblePredicate) {
        this.slotVisiblePredicate = slotVisiblePredicate;
        return this;
    }

    public int getSlotLimit(int slot) {
        return slotAmountFilter.getOrDefault(slot, this.slotLimit);
    }

    @Override
    protected int getCapacity(int index, ItemResource resource) {
        int itemLimit = resource.isEmpty() ? slotLimit : resource.getMaxStackSize();
        return Math.min(getSlotLimit(index), itemLimit);
    }

    public Function<Integer, Pair<Integer, Integer>> getSlotPosition() {
        return slotPosition;
    }

    public InventoryComponent<T> setSlotPosition(Function<Integer, Pair<Integer, Integer>> slotPosition) {
        this.slotPosition = slotPosition;
        return this;
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return insertPredicate.test(stack, slot);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return !resource.isEmpty() && insertPredicate.test(resource.toStack(1), index);
    }

    @Override
    public List<IFactory<?>> getScreenAddons() {
        List<IFactory<?>> addons = new ArrayList<>();
        addons.add(() -> new SlotsScreenAddon<>(this));
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return Lists.newArrayList(
                () -> new SlotContainerAddon(this, xPos, yPos, slotPosition)
        );
    }
}
