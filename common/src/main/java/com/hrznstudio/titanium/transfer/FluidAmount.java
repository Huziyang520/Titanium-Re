// Fluid and amount pair shared by both loaders

package com.hrznstudio.titanium.transfer;

import net.minecraft.world.level.material.Fluid;

public record FluidAmount(Fluid fluid, long amount) {

    public static final FluidAmount EMPTY = new FluidAmount(null, 0);

    public boolean isEmpty() {
        return this.fluid == null || this.amount <= 0;
    }

    public FluidAmount withAmount(long amount) {
        return new FluidAmount(this.fluid, amount);
    }
}
