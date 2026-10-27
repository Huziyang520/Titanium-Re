// Titanium Lite Text Screen Addon

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

public class TextScreenAddon extends BasicScreenAddon {

    private String text;
    private boolean shadow;
    private int color;

    public TextScreenAddon(String text, int posX, int posY, boolean shadow, int color) {
        super(posX, posY);
        this.text = text;
        this.shadow = shadow;
        this.color = color;
    }

    public TextScreenAddon(String text, int posX, int posY, boolean shadow) {
        this(text, posX, posY, shadow, ChatFormatting.DARK_GRAY.getColor());
    }

    @Override
    public int getXSize() {
        return 0;
    }

    @Override
    public int getYSize() {
        return 0;
    }

    @Override
    public void drawBackgroundLayer(GuiGraphicsExtractor guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        if (shadow) {

            guiGraphics.text(Minecraft.getInstance().font, getText(), guiX + getPosX(), guiY + getPosY(), 0xff000000 | color, true);
        } else {

            guiGraphics.text(Minecraft.getInstance().font, getText(), guiX + getPosX(), guiY + getPosY(), 0xff000000 | color, false);
        }
    }

    @Override
    public void drawForegroundLayer(GuiGraphicsExtractor guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
    }

    public String getText() {
        return text;
    }

}
