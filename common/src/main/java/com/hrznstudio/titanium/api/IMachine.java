// Titanium Lite I Machine

package com.hrznstudio.titanium.api;

import com.hrznstudio.titanium.api.augment.IAugmentType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IMachine {

    boolean isActive();

    boolean isPaused();

    boolean canAcceptAugment(ItemStack augment);

    List<ItemStack> getInstalledAugments();

    List<ItemStack> getInstalledAugments(IAugmentType filter);

    boolean hasAugmentInstalled(IAugmentType augmentType);

}
