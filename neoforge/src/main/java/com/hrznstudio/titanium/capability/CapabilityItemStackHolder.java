// Titanium Lite Capability Item Stack Holder

package com.hrznstudio.titanium.capability;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.capability.IStackHolder;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.ItemCapability;

public class CapabilityItemStackHolder {
    public static final ItemCapability<IStackHolder, Void> ITEMSTACK_HOLDER_CAPABILITY = ItemCapability.createVoid(Identifier.fromNamespaceAndPath(Titanium.MODID, "stackholder"), IStackHolder.class);
}
