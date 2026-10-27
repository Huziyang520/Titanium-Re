// Loader independent view of an incoming payload context

package com.hrznstudio.titanium.network;

import net.minecraft.world.entity.player.Player;

public interface IMessageContext {

    Player player();

    void enqueue(Runnable task);
}
