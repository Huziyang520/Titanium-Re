// Titanium Lite inventory component shared by both loaders

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
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class InventoryComponent<T extends IComponentHarness> implements Container, IScreenAddonProvider, IContainerAddonProvider {

    private final String name;
    private final ItemStack[] stacks;
    private int xPos;
    private int yPos;
    private int xSize;
    private int ySize;
    private T componentHarness;
    private BiPredicate<ItemStack, Integer> insertPredicate;
    private BiPredicate<ItemStack, Integer> extractPredicate;
    private BiConsumer<ItemStack, Integer> onSlotChanged;
    private final Map<Integer, Integer> slotAmountFilter;
    private final Map<Integer, ItemStack> slotToStackRenderMap;
    private boolean colorGuiEnabled;
    private final Map<Integer, Color> slotToColorRenderMap;
    private int slotLimit;
    private Function<Integer, Pair<Integer, Integer>> slotPosition;
    private Predicate<Integer> slotVisiblePredicate;

    public InventoryComponent(String name, int xPos, int yPos, int size) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.stacks = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            this.stacks[i] = ItemStack.EMPTY;
        }
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
        this.slotPosition = integer -> Pair.of(18 * (integer % this.xSize), 18 * (integer / this.xSize));
        this.slotVisiblePredicate = integer -> true;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", this.name);
        ListTag items = new ListTag();
        for (int i = 0; i < this.stacks.length; i++) {
            if (!this.stacks[i].isEmpty()) {
                CompoundTag entry = new CompoundTag();
                entry.putInt("Slot", i);
                entry.put("Stack", com.hrznstudio.titanium.util.ItemStackSerialization.save(provider, this.stacks[i]));
                items.add(entry);
            }
        }
        tag.put("Items", items);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ListTag items = tag.getListOrEmpty("Items");
        for (int i = 0; i < this.stacks.length; i++) {
            this.stacks[i] = ItemStack.EMPTY;
        }
        for (int i = 0; i < items.size(); i++) {
            CompoundTag entry = items.getCompoundOrEmpty(i);
            int slot = entry.getInt("Slot").orElse(-1);
            if (slot >= 0 && slot < this.stacks.length) {
                this.stacks[slot] = com.hrznstudio.titanium.util.ItemStackSerialization.load(provider, entry.getCompoundOrEmpty("Stack"));
            }
        }
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
        if (stack.isEmpty() || slot < 0 || slot >= this.stacks.length) {
            return stack;
        }
        if (!this.insertPredicate.test(stack, slot)) {
            return stack;
        }
        ItemStack current = this.stacks[slot];
        if (!current.isEmpty() && !ItemStack.isSameItemSameComponents(current, stack)) {
            return stack;
        }
        int limit = Math.min(this.getSlotLimit(slot), stack.getMaxStackSize());
        int room = limit - current.getCount();
        if (room <= 0) {
            return stack;
        }
        int moved = Math.min(room, stack.getCount());
        if (!simulate) {
            if (current.isEmpty()) {
                this.stacks[slot] = stack.copyWithCount(moved);
            } else {
                current.grow(moved);
                this.stacks[slot] = current;
            }
            this.changed(slot);
        }
        return stack.copyWithCount(stack.getCount() - moved);
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 0 || slot >= this.stacks.length || amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack current = this.stacks[slot];
        if (current.isEmpty() || !this.extractPredicate.test(current, slot)) {
            return ItemStack.EMPTY;
        }
        int taken = Math.min(amount, current.getCount());
        ItemStack result = current.copyWithCount(taken);
        if (!simulate) {
            ItemStack remaining = current.copyWithCount(current.getCount() - taken);
            this.stacks[slot] = remaining.isEmpty() ? ItemStack.EMPTY : remaining;
            this.changed(slot);
        }
        return result;
    }

    private void changed(int slot) {
        if (this.componentHarness != null) {
            this.componentHarness.markComponentDirty();
        }
        this.onSlotChanged.accept(this.getStackInSlot(slot), slot);
    }

    public int getSlots() {
        return this.stacks.length;
    }

    public ItemStack getStackInSlot(int slot) {
        return slot >= 0 && slot < this.stacks.length ? this.stacks[slot] : ItemStack.EMPTY;
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot < 0 || slot >= this.stacks.length) {
            return;
        }
        this.stacks[slot] = stack;
        this.changed(slot);
    }

    public String getName() {
        return this.name;
    }

    public int getXPos() {
        return this.xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return this.yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int getXSize() {
        return this.xSize;
    }

    public int getYSize() {
        return this.ySize;
    }

    public T getComponentHarness() {
        return this.componentHarness;
    }

    public BiPredicate<ItemStack, Integer> getInsertPredicate() {
        return this.insertPredicate;
    }

    public BiPredicate<ItemStack, Integer> getExtractPredicate() {
        return this.extractPredicate;
    }

    public BiConsumer<ItemStack, Integer> getOnSlotChanged() {
        return this.onSlotChanged;
    }

    public InventoryComponent<T> setColorGuiEnabled(boolean colorGuiEnabled) {
        this.colorGuiEnabled = colorGuiEnabled;
        return this;
    }

    public boolean isColorGuiEnabled() {
        return this.colorGuiEnabled;
    }

    public Map<Integer, Color> getSlotToColorRenderMap() {
        return this.slotToColorRenderMap;
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
        return this.setSlotToColorRender(slot, new Color(color));
    }

    public InventoryComponent<T> setSlotToColorRender(int slot, DyeColor color) {
        return this.setSlotToColorRender(slot, new Color(color.getMapColor().col));
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
        return this.slotVisiblePredicate;
    }

    public InventoryComponent<T> setSlotVisiblePredicate(Predicate<Integer> slotVisiblePredicate) {
        this.slotVisiblePredicate = slotVisiblePredicate;
        return this;
    }

    public int getSlotLimit(int slot) {
        return this.slotAmountFilter.getOrDefault(slot, this.slotLimit);
    }

    public Function<Integer, Pair<Integer, Integer>> getSlotPosition() {
        return this.slotPosition;
    }

    public InventoryComponent<T> setSlotPosition(Function<Integer, Pair<Integer, Integer>> slotPosition) {
        this.slotPosition = slotPosition;
        return this;
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.insertPredicate.test(stack, slot);
    }

    @Override
    public int getContainerSize() {
        return this.stacks.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack extracted = this.extractItem(slot, amount, false);
        return extracted;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack current = this.getStackInSlot(slot).copy();
        this.setStackInSlot(slot, ItemStack.EMPTY);
        return current;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.setStackInSlot(slot, stack);
    }

    @Override
    public int getMaxStackSize() {
        return this.slotLimit;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.insertPredicate.test(stack, slot);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.stacks.length; i++) {
            this.stacks[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public List<IFactory<?>> getScreenAddons() {
        List<IFactory<?>> addons = new ArrayList<>();
        addons.add(() -> new SlotsScreenAddon<>(this));
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return Lists.newArrayList(() -> new SlotContainerAddon(this, this.xPos, this.yPos, this.slotPosition));
    }
}
