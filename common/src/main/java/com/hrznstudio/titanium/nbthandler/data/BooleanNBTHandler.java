// Titanium Lite Boolean NBT Handler

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.util.Unboxing;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BooleanNBTHandler implements INBTHandler<Boolean> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return boolean.class.isAssignableFrom(aClass) || Boolean.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull Boolean object) {
        compound.putBoolean(name, object);
        return true;
    }

    @Override
    public Boolean readFromNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable Boolean currentValue) {
        return compound.contains(name) ? compound.getBooleanOr(name, false) : Unboxing.safelyUnbox(currentValue);
    }
}
