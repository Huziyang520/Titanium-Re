// Titanium Lite Block With Tile

package com.hrznstudio.titanium.module;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record BlockWithTile(Supplier<Block> block, Supplier<BlockEntityType<?>> type) implements ItemLike {
    @Override
    public Item asItem() {
        return block.get().asItem();
    }

    public Block getBlock() {
        return block.get();
    }

    public BlockEntityType<?> getType() {
        return type.get();
    }
}
