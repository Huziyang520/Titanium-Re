// Titanium Lite Item Stack Holder Capability

package com.hrznstudio.titanium.capability;

import com.hrznstudio.titanium.api.capability.IStackHolder;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ItemStackHolderCapability implements IStackHolder {

    private Supplier<ItemStack> holder;

    public ItemStackHolderCapability(Supplier<ItemStack> holder) {
        this.holder = holder;
    }

    @Override
    public Supplier<ItemStack> getHolder() {
        return holder;
    }

    @Override
    public void setHolder(Supplier<ItemStack> stack) {
        this.holder = stack;
    }

}
