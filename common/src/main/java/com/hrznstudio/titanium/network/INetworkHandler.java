// Loader independent network entry point used by common content

package com.hrznstudio.titanium.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public interface INetworkHandler {

    void sendToServer(Message message);

    void sendToNearby(Level world, BlockPos pos, int distance, Message message);

    void sendTo(Message message, ServerPlayer player);
}
