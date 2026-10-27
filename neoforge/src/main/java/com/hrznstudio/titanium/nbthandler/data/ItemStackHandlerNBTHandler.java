// Titanium Lite Item Stack Handler NBT Handler

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStackHandlerNBTHandler implements INBTHandler<ItemStacksResourceHandler> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return ItemStacksResourceHandler.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull ItemStacksResourceHandler object) {
        compound.put(name, com.hrznstudio.titanium.util.ValueIOSerialization.save(provider, object));
        return true;
    }

    @Override
    public ItemStacksResourceHandler readFromNBT(HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable ItemStacksResourceHandler current) {
        if (compound.contains(name)) {
            if (current == null) current = new ItemStacksResourceHandler(1);
            com.hrznstudio.titanium.util.ValueIOSerialization.load(provider, compound.getCompoundOrEmpty(name), current);
            return current;
        }
        return current;
    }
}
