// Titanium Lite I Redstone Reader

package com.hrznstudio.titanium.api.redstone;

import net.minecraft.core.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRedstoneReader {

    @Nonnull
    IRedstoneState getEnvironmentValue(boolean strongPower, @Nullable Direction direction);

}
