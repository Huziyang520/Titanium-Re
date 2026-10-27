// Titanium Lite Disableable Item Handler Slot

package com.hrznstudio.titanium.container.impl;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

public class DisableableItemHandlerSlot extends ResourceHandlerSlot {
    private final BooleanSupplier isDisabled;

    public DisableableItemHandlerSlot(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition, BasicInventoryContainer basicInventoryContainer) {
        this(itemHandler, index, xPosition, yPosition, basicInventoryContainer::isDisabled);
    }

    public DisableableItemHandlerSlot(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition, BooleanSupplier isDisabled) {
        super(itemHandler, itemHandler::set, index, xPosition, yPosition);
        this.isDisabled = isDisabled;
    }

    @Override
    public boolean isActive() {
        return !isDisabled.getAsBoolean();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return !isDisabled.getAsBoolean();
    }
}
