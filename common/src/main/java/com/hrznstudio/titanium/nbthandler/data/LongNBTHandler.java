// Titanium Lite Long NBT Handler

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.util.Unboxing;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LongNBTHandler implements INBTHandler<Long> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return long.class.isAssignableFrom(aClass) || Long.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull Long object) {
        compound.putLong(name, object);
        return true;
    }

    @Override
    public Long readFromNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable Long current) {
        return compound.contains(name) ? compound.getLongOr(name, 0L) : Unboxing.safelyUnbox(current);
    }
}
