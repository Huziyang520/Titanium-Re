// Titanium Lite I Container Addon

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public interface IContainerAddon {
    default List<Slot> getSlots() {
        return Lists.newArrayList();
    }

    default List<DataSlot> getIntReferenceHolders() {
        return Lists.newArrayList();
    }

    default List<ContainerData> getIntArrayReferenceHolders() {
        return Lists.newArrayList();
    }
}
