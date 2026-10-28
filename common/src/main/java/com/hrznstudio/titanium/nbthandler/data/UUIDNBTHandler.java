// Titanium Lite UUIDNBT Handler

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class UUIDNBTHandler implements INBTHandler<UUID> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return UUID.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull UUID object) {
        compound.putIntArray(name, UUIDUtil.uuidToIntArray(object));
        return true;
    }

    @Override
    public UUID readFromNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable UUID current) {
        return compound.getIntArray(name).map(UUIDUtil::uuidFromIntArray).orElse(current);
    }
}
