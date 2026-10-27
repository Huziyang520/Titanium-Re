// Titanium Lite I Drawable

package com.hrznstudio.titanium.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;

public interface IDrawable {
    static IDrawable of(IAsset asset, PoseStack matrixStack) {
        return new DefaultDrawable(asset, matrixStack);
    }

    void draw(GuiGraphicsExtractor guiGraphics, Screen gui, Point position, Point mousePosition);
}
