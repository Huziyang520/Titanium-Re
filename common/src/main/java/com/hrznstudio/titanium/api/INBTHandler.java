// Titanium Lite INBT Handler

package com.hrznstudio.titanium.api;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface INBTHandler<T> {

    boolean isClassValid(Class<?> aClass);

    boolean storeToNBT(HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull T object);

    T readFromNBT(HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable T currentValue);
}
