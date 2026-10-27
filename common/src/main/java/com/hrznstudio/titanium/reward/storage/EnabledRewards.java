// Titanium Lite Enabled Rewards

package com.hrznstudio.titanium.reward.storage;

import com.hrznstudio.titanium.nbthandler.INBTSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;

import java.util.HashMap;

public class EnabledRewards implements INBTSerializable<CompoundTag> {

    private HashMap<Identifier, String> enabled;

    public EnabledRewards() {
        this.enabled = new HashMap<>();
    }

    public HashMap<Identifier, String> getEnabled() {
        return enabled;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundNBT = new CompoundTag();
        enabled.forEach((resourceLocation, s) -> compoundNBT.putString(resourceLocation.toString(), s));
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        enabled.clear();
        nbt.keySet().forEach(s -> enabled.put(Identifier.parse(s), nbt.getStringOr(s, "")));
    }
}
