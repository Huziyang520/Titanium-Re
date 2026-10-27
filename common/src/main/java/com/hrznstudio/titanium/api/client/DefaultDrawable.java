// Titanium Lite Default Drawable

package com.hrznstudio.titanium.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;

public class DefaultDrawable implements IDrawable {
    private final IAsset asset;
    private final PoseStack matrixStack;

    DefaultDrawable(IAsset asset, PoseStack matrixStack) {
        this.asset = asset;
        this.matrixStack = matrixStack;
    }

    @Override
    public void draw(GuiGraphicsExtractor guiGraphics, Screen gui, Point position, Point mousePosition) {
        Rectangle area = asset.getArea();
        com.hrznstudio.titanium.util.AssetUtil.blit(guiGraphics, asset.getIdentifier(), position.x, position.y, area.x, area.y, area.width, area.height);
    }
}
