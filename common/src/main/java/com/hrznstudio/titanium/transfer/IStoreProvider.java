// Maps a block entity or item stack to the stores above

package com.hrznstudio.titanium.transfer;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IStoreProvider {

    IItemStore itemStore(BlockEntity blockEntity, Direction side);

    IFluidStore fluidStore(BlockEntity blockEntity, Direction side);

    IEnergyStore energyStore(BlockEntity blockEntity, Direction side);

    IEnergyStore energyStore(ItemStack stack);
}
