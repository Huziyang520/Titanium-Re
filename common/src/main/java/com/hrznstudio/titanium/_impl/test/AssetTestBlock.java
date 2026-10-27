// Titanium Lite Asset Test Block

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.AssetTestTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import com.hrznstudio.titanium.module.BlockWithTile;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nonnull;

public class AssetTestBlock extends RotatableBlock<AssetTestTile> {

    public static BlockWithTile TEST;

    public AssetTestBlock() {
        super("asset_test", Properties.ofFullCopy(Blocks.STONE), AssetTestTile.class);
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public LootTable.Builder getLootTable(BasicBlockLootTables blockLootTables) {
        return blockLootTables.droppingNothing();
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<AssetTestTile> getTileEntityFactory() {
        return AssetTestTile::new;
    }
}
