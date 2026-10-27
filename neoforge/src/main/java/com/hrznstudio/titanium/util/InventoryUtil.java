// Titanium Lite Inventory Util

package com.hrznstudio.titanium.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class InventoryUtil {
    public static List<ItemStack> getStacks(@Nullable ResourceHandler<ItemResource> handler) {
        if (handler == null)
            return Collections.emptyList();
        ImmutableList.Builder<ItemStack> builder = new ImmutableList.Builder<>();
        for (int slot = 0; slot < handler.size(); slot++) {
            ItemStack subStack = handler.getResource(slot).toStack(handler.getAmountAsInt(slot));
            if (!subStack.isEmpty())
                builder.add(subStack);
        }
        return builder.build();
    }
}
