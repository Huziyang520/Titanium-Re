// Titanium Lite Generator Tile

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;

import javax.annotation.Nonnull;

public abstract class GeneratorTile<T extends GeneratorTile<T>> extends PoweredTile<T> {

    @Save
    private ProgressBarComponent<T> progressBar;

    public GeneratorTile(BasicTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(base, blockEntityType, pos, state);
        this.addProgressBar(progressBar = getProgressBar()
                .setComponentHarness(this.getSelf())
                .setCanIncrease(tileEntity -> !isSmart() || this.getEnergyCapacity() - this.getEnergyStorage().getEnergyStored() >= getEnergyProducedEveryTick())
                .setIncreaseType(false)
                .setOnStart(() -> {
                    progressBar.setMaxProgress(consumeFuel());
                    progressBar.setProgress(progressBar.getMaxProgress());
                    markForUpdate();
                })
                .setCanReset(tileEntity -> canStart() && progressBar.getProgress() == 0)
                .setOnTickWork(() -> this.getEnergyStorage().setEnergyStored(getEnergyProducedEveryTick() + this.getEnergyStorage().getEnergyStored()))
        );
    }

    public abstract int consumeFuel();

    public abstract boolean canStart();

    public abstract int getEnergyProducedEveryTick();

    public abstract ProgressBarComponent<T> getProgressBar();

    public abstract int getEnergyCapacity();

    public abstract int getExtractingEnergy();

    public boolean isSmart() {
        return true;
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        for (Direction facing : Direction.values()) {
            BlockPos checking = this.worldPosition.relative(facing);
            var storage = level.getCapability(Capabilities.Energy.BLOCK, checking, facing.getOpposite());
            if (storage != null) {
                EnergyHandlerUtil.move(this.getEnergyStorage(), storage, this.getExtractingEnergy(), null);
            }
        }
    }

    @Nonnull
    @Override
    protected EnergyStorageComponent<T> createEnergyStorage() {
        return new EnergyStorageComponent<>(getEnergyCapacity(),0, getExtractingEnergy(), 10, 20);
    }
}
