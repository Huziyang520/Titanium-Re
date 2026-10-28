// Titanium Lite Int Reference Holder Addon

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.world.inventory.DataSlot;

import java.util.List;

public class IntReferenceHolderAddon implements IContainerAddon {
    private final DataSlot[] referenceHolders;

    public IntReferenceHolderAddon(DataSlot... referenceHolders) {
        this.referenceHolders = referenceHolders;
    }

    @Override
    public List<DataSlot> getIntReferenceHolders() {
        return Lists.newArrayList(referenceHolders);
    }
}
