// Creates block entities for a registered type

package com.hrznstudio.titanium.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ITileFactory<T extends BlockEntity> {

    T create(BlockPos pos, BlockState state);
}
