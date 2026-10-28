// Titanium Lite slot bound to an inventory component

package com.hrznstudio.titanium.container.addon;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class TitaniumSlot extends Slot {

    private final InventoryComponent<?> component;

    public TitaniumSlot(InventoryComponent<?> component, int index, int x, int y) {
        super(component, index, x, y);
        this.component = component;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.component.isItemValid(this.index, stack);
    }

    @Override
    public int getMaxStackSize() {
        return this.component.getSlotLimit(this.index);
    }

    @Override
    public boolean isActive() {
        return this.component.getSlotVisiblePredicate().test(this.index);
    }
}
