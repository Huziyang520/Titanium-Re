// Titanium Lite slot container addon shared by both loaders

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.world.inventory.Slot;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;

public class SlotContainerAddon implements IContainerAddon {

    private final InventoryComponent<?> component;
    private final int xPos;
    private final int yPos;
    private final Function<Integer, Pair<Integer, Integer>> positionFunction;

    public SlotContainerAddon(InventoryComponent<?> component, int xPos, int yPos, Function<Integer, Pair<Integer, Integer>> positionFunction) {
        this.component = component;
        this.xPos = xPos;
        this.yPos = yPos;
        this.positionFunction = positionFunction;
    }

    @Override
    public List<Slot> getSlots() {
        List<Slot> slots = Lists.newArrayList();
        for (int i = 0; i < this.component.getSlots(); i++) {
            Pair<Integer, Integer> offset = this.positionFunction.apply(i);
            slots.add(new TitaniumSlot(this.component, i, this.xPos + offset.getLeft(), this.yPos + offset.getRight()));
        }
        return slots;
    }
}
