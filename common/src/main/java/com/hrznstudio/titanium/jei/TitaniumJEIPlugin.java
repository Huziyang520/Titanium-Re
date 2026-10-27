// Titanium Lite Titanium JEI Plugin

package com.hrznstudio.titanium.jei;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.client.screen.container.BasicContainerScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.Identifier;

import javax.annotation.Nonnull;

@JeiPlugin
public class TitaniumJEIPlugin implements IModPlugin {
    @Override
    @Nonnull
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(Titanium.MODID, "default");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(BasicContainerScreen.class, new BasicContainerScreenHandler());
    }
}
