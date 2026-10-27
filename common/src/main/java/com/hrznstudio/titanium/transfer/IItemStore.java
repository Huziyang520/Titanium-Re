// Loader neutral item storage used by Titanium components

package com.hrznstudio.titanium.transfer;

import net.minecraft.world.item.ItemStack;

public interface IItemStore {

    int size();

    ItemStack get(int slot);

    void set(int slot, ItemStack stack);

    long capacity(int slot);

    long amount(int slot);

    ItemStack insert(int slot, ItemStack stack, boolean simulate);

    ItemStack extract(int slot, long amount, boolean simulate);

    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            if (!get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
