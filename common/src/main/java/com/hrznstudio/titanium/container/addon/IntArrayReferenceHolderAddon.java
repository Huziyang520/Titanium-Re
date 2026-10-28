// Titanium Lite Int Array Reference Holder Addon

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.world.inventory.ContainerData;

import java.util.List;

public class IntArrayReferenceHolderAddon implements IContainerAddon {
    private final ContainerData[] referenceHolders;

    public IntArrayReferenceHolderAddon(ContainerData... referenceHolders) {
        this.referenceHolders = referenceHolders;
    }

    @Override
    public List<ContainerData> getIntArrayReferenceHolders() {
        return Lists.newArrayList(referenceHolders);
    }
}
