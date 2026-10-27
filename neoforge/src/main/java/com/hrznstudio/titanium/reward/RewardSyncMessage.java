// Titanium Lite Reward Sync Message

package com.hrznstudio.titanium.reward;

import com.hrznstudio.titanium.network.Message;
import com.hrznstudio.titanium.reward.storage.ClientRewardStorage;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RewardSyncMessage extends Message {

    private CompoundTag compoundNBT;

    public RewardSyncMessage(CompoundTag compoundNBT) {
        this.compoundNBT = compoundNBT;
    }

    public RewardSyncMessage() {

    }

    @Override
    protected void handleServerMessage(IPayloadContext context) {

    }

    @Override
    public void handleClientMessage(IPayloadContext context) {
        ClientRewardStorage.REWARD_STORAGE.deserializeNBT(context.player().level().registryAccess(), compoundNBT);
    }
}
