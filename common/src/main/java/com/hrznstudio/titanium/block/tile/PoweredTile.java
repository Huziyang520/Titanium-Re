// Titanium Lite Powered Tile

package com.hrznstudio.titanium.block.tile;

import com.google.common.collect.Sets;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public abstract class PoweredTile<T extends PoweredTile<T>> extends ActiveTile<T> {
    @Save
    private final EnergyStorageComponent<T> energyStorage;

    private boolean showEnergy = true;

    public PoweredTile(BasicTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(base, blockEntityType, pos, state);
        this.energyStorage = this.createEnergyStorage();
        this.energyStorage.setComponentHarness(this.getSelf());
    }

    @Nonnull
    public EnergyStorageComponent<T> getEnergyStorage() {
        return energyStorage;
    }

    @Nonnull
    protected EnergyStorageComponent<T> createEnergyStorage() {
        return new EnergyStorageComponent<>(10000, 4, 10);
    }

    public Set<Direction> getValidEnergyFaces() {
        return Sets.newHashSet(Direction.values());
    }

    @Override
    @Nonnull
    public List<IFactory<?>> getScreenAddons() {
        List<IFactory<?>> screenAddons = super.getScreenAddons();
        if (showEnergy) {
            screenAddons.addAll(this.getEnergyStorage().getScreenAddons());
        }
        return screenAddons;
    }

    @Override
    @Nonnull
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> containerAddons = super.getContainerAddons();
        if (showEnergy) {
            containerAddons.addAll(this.getEnergyStorage().getContainerAddons());
        }
        return containerAddons;
    }

    public void setShowEnergy(boolean showEnergy) {
        this.showEnergy = showEnergy;
    }
}
