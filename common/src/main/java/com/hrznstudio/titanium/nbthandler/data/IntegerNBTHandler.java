// Titanium Lite Integer NBT Handler

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.util.Unboxing;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IntegerNBTHandler implements INBTHandler<Integer> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return int.class.isAssignableFrom(aClass) || Integer.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull Integer object) {
        compound.putInt(name, object);
        return true;
    }

    @Override
    public Integer readFromNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable Integer current) {
        return compound.contains(name) ? compound.getIntOr(name, 0) : Unboxing.safelyUnbox(current);
    }
}
