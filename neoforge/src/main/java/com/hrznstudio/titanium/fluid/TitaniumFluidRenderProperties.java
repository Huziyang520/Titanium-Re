// Titanium Lite Titanium Fluid Render Properties

package com.hrznstudio.titanium.fluid;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.fluid.FluidTintSource;

import javax.annotation.Nullable;

public interface TitaniumFluidRenderProperties extends IClientFluidTypeExtensions {
    Identifier getStillTexture();

    Identifier getFlowingTexture();

    default @Nullable Identifier getOverlayTexture() {
        return null;
    }

    default @Nullable FluidTintSource getTintSource() {
        return null;
    }
}
