// Titanium Lite Tank NBT Handler

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TankNBTHandler implements INBTHandler<FluidStacksResourceHandler> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return FluidStacksResourceHandler.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull FluidStacksResourceHandler object) {
        compound.put(name, com.hrznstudio.titanium.util.ValueIOSerialization.save(provider, object));
        return true;
    }

    @Override
    public FluidStacksResourceHandler readFromNBT(net.minecraft.core.HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable FluidStacksResourceHandler currentValue) {
        if (compound.contains(name)) {
            com.hrznstudio.titanium.util.ValueIOSerialization.load(provider, compound.getCompoundOrEmpty(name), currentValue);
            return currentValue;
        }
        return currentValue;
    }
}
