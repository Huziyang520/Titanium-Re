// Titanium Lite I Button Handler

package com.hrznstudio.titanium.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface IButtonHandler {
    void handleButtonMessage(int id, Player playerEntity, CompoundTag compound);
}
