// Payload context adapter exposing only loader independent operations

package com.hrznstudio.titanium.network;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class NeoForgeMessageContext implements IMessageContext {

    private final IPayloadContext context;

    public NeoForgeMessageContext(IPayloadContext context) {
        this.context = context;
    }

    @Override
    public Player player() {
        return context.player();
    }

    @Override
    public void enqueue(Runnable task) {
        context.enqueueWork(task);
    }

    public IPayloadContext unwrap() {
        return context;
    }
}
