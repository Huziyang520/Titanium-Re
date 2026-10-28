// Payload context adapter keeping client classes out of the shared handler

package com.hrznstudio.titanium.network;

import java.util.function.Consumer;

import net.minecraft.world.entity.player.Player;

public final class FabricMessageContext implements IMessageContext {

    private final Player player;
    private final Consumer<Runnable> executor;

    public FabricMessageContext(Player player, Consumer<Runnable> executor) {
        this.player = player;
        this.executor = executor;
    }

    @Override
    public Player player() {
        return this.player;
    }

    @Override
    public void enqueue(Runnable task) {
        this.executor.accept(task);
    }
}
