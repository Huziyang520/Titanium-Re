// Fluid store view over a slotted transfer storage

package com.hrznstudio.titanium.transfer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public final class FabricFluidStore implements IFluidStore {

    private final SlottedStorage<FluidVariant> storage;

    public FabricFluidStore(SlottedStorage<FluidVariant> storage) {
        this.storage = storage;
    }

    @Override
    public int size() {
        return this.storage.getSlotCount();
    }

    @Override
    public FluidAmount get(int slot) {
        SingleSlotStorage<FluidVariant> view = this.storage.getSlot(slot);
        FluidVariant variant = view.getResource();
        return variant.isBlank() ? FluidAmount.EMPTY : new FluidAmount(variant.getFluid(), view.getAmount());
    }

    @Override
    public void set(int slot, FluidAmount amount) {
        SingleSlotStorage<FluidVariant> view = this.storage.getSlot(slot);
        try (Transaction transaction = Transaction.openOuter()) {
            if (!view.isResourceBlank()) {
                view.extract(view.getResource(), view.getAmount(), transaction);
            }
            if (!amount.isEmpty()) {
                view.insert(FluidVariant.of(amount.fluid()), amount.amount(), transaction);
            }
            transaction.commit();
        }
    }

    @Override
    public long capacity(int slot) {
        return this.storage.getSlot(slot).getCapacity();
    }

    @Override
    public FluidAmount insert(int slot, FluidAmount amount, boolean simulate) {
        if (amount.isEmpty()) {
            return FluidAmount.EMPTY;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = this.storage.getSlot(slot).insert(FluidVariant.of(amount.fluid()), amount.amount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted >= amount.amount() ? FluidAmount.EMPTY : amount.withAmount(amount.amount() - inserted);
        }
    }

    @Override
    public FluidAmount extract(int slot, long amount, boolean simulate) {
        SingleSlotStorage<FluidVariant> view = this.storage.getSlot(slot);
        if (view.isResourceBlank() || amount <= 0) {
            return FluidAmount.EMPTY;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            long extracted = view.extract(view.getResource(), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted <= 0 ? FluidAmount.EMPTY : new FluidAmount(view.getResource().getFluid(), extracted);
        }
    }
}
