// Titanium Lite Machine Test Block

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.MachineTestTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.module.BlockWithTile;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MachineTestBlock extends RotatableBlock<MachineTestTile> {

    public static BlockWithTile TEST;

    public MachineTestBlock() {
        super("machine_test", Properties.ofFullCopy(Blocks.IRON_BLOCK), MachineTestTile.class);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<MachineTestTile> getTileEntityFactory() {
        return MachineTestTile::new;
    }
}
