// Titanium Lite Float NBT Handler

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.util.Unboxing;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FloatNBTHandler implements INBTHandler<Float> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return float.class.isAssignableFrom(aClass) || Float.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull Float object) {
        compound.putFloat(name, object);
        return true;
    }

    @Override
    public Float readFromNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable Float currentValue) {
        return compound.contains(name) ? compound.getFloatOr(name, 0.0F) : Unboxing.safelyUnbox(currentValue);
    }
}
