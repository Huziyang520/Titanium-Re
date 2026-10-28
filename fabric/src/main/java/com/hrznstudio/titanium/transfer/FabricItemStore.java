// Item store view over a slotted transfer storage

package com.hrznstudio.titanium.transfer;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;

public final class FabricItemStore implements IItemStore {

    private final SlottedStorage<ItemVariant> storage;

    public FabricItemStore(SlottedStorage<ItemVariant> storage) {
        this.storage = storage;
    }

    @Override
    public int size() {
        return this.storage.getSlotCount();
    }

    @Override
    public ItemStack get(int slot) {
        SingleSlotStorage<ItemVariant> view = this.storage.getSlot(slot);
        ItemVariant variant = view.getResource();
        return variant.isBlank() ? ItemStack.EMPTY : variant.toStack((int) Math.min(Integer.MAX_VALUE, view.getAmount()));
    }

    @Override
    public void set(int slot, ItemStack stack) {
        SingleSlotStorage<ItemVariant> view = this.storage.getSlot(slot);
        try (Transaction transaction = Transaction.openOuter()) {
            if (!view.isResourceBlank()) {
                view.extract(view.getResource(), view.getAmount(), transaction);
            }
            if (!stack.isEmpty()) {
                view.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            }
            transaction.commit();
        }
    }

    @Override
    public long capacity(int slot) {
        return this.storage.getSlot(slot).getCapacity();
    }

    @Override
    public long amount(int slot) {
        return this.storage.getSlot(slot).getAmount();
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = this.storage.getSlot(slot).insert(ItemVariant.of(stack), stack.getCount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            ItemStack leftover = stack.copy();
            leftover.setCount(stack.getCount() - (int) inserted);
            return leftover;
        }
    }

    @Override
    public ItemStack extract(int slot, long amount, boolean simulate) {
        SingleSlotStorage<ItemVariant> view = this.storage.getSlot(slot);
        if (view.isResourceBlank() || amount <= 0) {
            return ItemStack.EMPTY;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            long extracted = view.extract(view.getResource(), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return view.getResource().toStack((int) Math.min(Integer.MAX_VALUE, extracted));
        }
    }
}
