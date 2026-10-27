// NBT handler for inventory components

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStackHandlerNBTHandler implements INBTHandler<InventoryComponent> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return InventoryComponent.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nonnull InventoryComponent object) {
        compound.put(name, object.serializeNBT(provider));
        return true;
    }

    @Override
    public InventoryComponent readFromNBT(HolderLookup.Provider provider, @Nonnull CompoundTag compound, @Nonnull String name, @Nullable InventoryComponent current) {
        if (compound.contains(name) && current != null) {
            current.deserializeNBT(provider, compound.getCompoundOrEmpty(name));
        }
        return current;
    }
}
