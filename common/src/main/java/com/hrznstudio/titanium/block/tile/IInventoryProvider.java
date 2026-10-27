// Block entities that own inventory components

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;

import java.util.Collection;
import java.util.List;

public interface IInventoryProvider {

    Collection<InventoryComponent<?>> getInventories();

    default List<InventoryComponent<?>> getInventoriesAsList() {
        return List.copyOf(this.getInventories());
    }
}
