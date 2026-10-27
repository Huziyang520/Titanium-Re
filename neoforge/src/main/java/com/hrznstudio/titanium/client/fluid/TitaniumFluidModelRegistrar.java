// Titanium Lite Titanium Fluid Model Registrar

package com.hrznstudio.titanium.client.fluid;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.fluid.TitaniumFluidInstance;
import com.hrznstudio.titanium.fluid.TitaniumFluidRenderProperties;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class TitaniumFluidModelRegistrar {
    private TitaniumFluidModelRegistrar() {
    }

    public static void registerModels(RegisterFluidModelsEvent event) {
        for (TitaniumFluidInstance instance : TitaniumFluidInstance.getInstances()) {
            TitaniumFluidRenderProperties properties = instance.getFluidRenderProperties();
            if (properties == null) {
                Titanium.LOGGER.warn(
                    "Skipping fluid model registration for {} because its client properties do not implement TitaniumFluidRenderProperties",
                    instance.getFluid()
                );
                continue;
            }

            event.register(
                new FluidModel.Unbaked(
                    new Material(properties.getStillTexture()),
                    new Material(properties.getFlowingTexture()),
                    properties.getOverlayTexture() == null ? null : new Material(properties.getOverlayTexture()),
                    properties.getTintSource()
                ),
                instance.getSourceFluid(),
                instance.getFlowingFluid()
            );
        }
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        for (TitaniumFluidInstance instance : TitaniumFluidInstance.getInstances()) {
            event.registerFluidType(instance.getRenderProperties(), instance.getFluidType());
        }
    }
}
