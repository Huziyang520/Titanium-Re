// Titanium Lite I Network Directional Connection

package com.hrznstudio.titanium.block_network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface INetworkDirectionalConnection {

    boolean canConnect(Level level, BlockPos blockPos, BlockState state, Direction direction);

}
