// Titanium Lite I Networkable

package com.hrznstudio.titanium.client.screen.addon.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface INetworkable {
    void sendMessage(int id, CompoundTag data);
}
