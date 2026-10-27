// Titanium Lite Client Reward Storage

package com.hrznstudio.titanium.reward.storage;

import com.hrznstudio.titanium.nbthandler.INBTSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.UUID;

public class ClientRewardStorage implements INBTSerializable<CompoundTag> {

    public static final ClientRewardStorage REWARD_STORAGE = new ClientRewardStorage();

    private HashMap<UUID, EnabledRewards> rewards;

    public ClientRewardStorage() {
        this.rewards = new HashMap<>();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        rewards.clear();
        nbt.keySet().forEach(s -> {
            EnabledRewards rewards = new EnabledRewards();
            rewards.deserializeNBT(provider, nbt.getCompoundOrEmpty(s));
            this.rewards.put(UUID.fromString(s), rewards);
        });
    }

    public HashMap<UUID, EnabledRewards> getRewards() {
        return rewards;
    }
}
