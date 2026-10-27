// Titanium Lite Twenty Four Test Block

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.TwentyFourTestTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import com.hrznstudio.titanium.module.BlockWithTile;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nonnull;

public class TwentyFourTestBlock extends RotatableBlock<TwentyFourTestTile> {

    public static BlockWithTile TEST;

    public TwentyFourTestBlock() {
        super("twenty_four_test", Properties.ofFullCopy(Blocks.STONE), TwentyFourTestTile.class);
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.TWENTY_FOUR_WAY;
    }

    @Override
    public LootTable.Builder getLootTable(BasicBlockLootTables blockLootTables) {
        return blockLootTables.droppingNothing();
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<TwentyFourTestTile> getTileEntityFactory() {
        return TwentyFourTestTile::new;
    }
}
