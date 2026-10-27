// Titanium Lite I Stack Holder

package com.hrznstudio.titanium.api.capability;

import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface IStackHolder {

    Supplier<ItemStack> getHolder();

    void setHolder(Supplier<ItemStack> stack);

}
