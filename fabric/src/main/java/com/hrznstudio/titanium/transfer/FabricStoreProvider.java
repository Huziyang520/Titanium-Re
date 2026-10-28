// Fabric store provider bridging the transfer lookups to the loader neutral stores

package com.hrznstudio.titanium.transfer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class FabricStoreProvider implements IStoreProvider {

    @Override
    public IItemStore itemStore(BlockEntity blockEntity, Direction side) {
        if (blockEntity.getLevel() == null) {
            return null;
        }
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(blockEntity.getLevel(), blockEntity.getBlockPos(), side);
        return storage instanceof SlottedStorage<ItemVariant> slotted ? new FabricItemStore(slotted) : null;
    }

    @Override
    public IFluidStore fluidStore(BlockEntity blockEntity, Direction side) {
        if (blockEntity.getLevel() == null) {
            return null;
        }
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(blockEntity.getLevel(), blockEntity.getBlockPos(), side);
        return storage instanceof SlottedStorage<FluidVariant> slotted ? new FabricFluidStore(slotted) : null;
    }

    @Override
    public IEnergyStore energyStore(BlockEntity blockEntity, Direction side) {
        return null;
    }

    @Override
    public IEnergyStore energyStore(ItemStack stack) {
        return null;
    }
}
