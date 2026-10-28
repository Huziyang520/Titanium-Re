// Titanium Lite Basic Addon Screen

package com.hrznstudio.titanium.client.screen.container;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BasicAddonScreen extends BasicContainerScreen<BasicAddonContainer> {
    public BasicAddonScreen(BasicAddonContainer container, Inventory inventory, Component title) {
        super(container, inventory, title, container.getAssetProvider());
        if (container.getProvider() instanceof IScreenAddonProvider) {
            ((IScreenAddonProvider) container.getProvider()).getScreenAddons()
                .stream()
                .map(IFactory::create)
                .map(IScreenAddon.class::cast)
                .forEach(this.getAddons()::add);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.getMenu().update();
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
    }
}
