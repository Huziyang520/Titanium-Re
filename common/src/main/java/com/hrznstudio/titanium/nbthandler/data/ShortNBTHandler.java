// Titanium Lite Short NBT Handler

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.util.Unboxing;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ShortNBTHandler implements INBTHandler<Short> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return short.class.isAssignableFrom(aClass) || Short.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull Short object) {
        compound.putShort(name, object);
        return true;
    }

    @Override
    public Short readFromNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable Short current) {
        return compound.getShortOr(name, Unboxing.safelyUnbox(current));
    }
}
