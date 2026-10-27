// Titanium Lite Shade Picker Addon

package com.hrznstudio.titanium.client.screen.addon.color;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ShadePickerAddon extends BasicScreenAddon {

    private static final int S_TILES = 10, V_TILES = 10;
    private final Supplier<Float> hueSupplier;
    private final Consumer<Float> brightnessConsumer;
    private final Consumer<Float> saturationConsumer;
    private float brightness;
    private float saturation;

    public ShadePickerAddon(int posX, int posY, float brightness, float saturation, Supplier<Float> hueSupplier, Consumer<Float> brightnessConsumer, Consumer<Float> saturationConsumer) {
        super(posX, posY);
        this.hueSupplier = hueSupplier;
        this.brightness = brightness;
        this.saturation = saturation;
        this.brightnessConsumer = brightnessConsumer;
        this.saturationConsumer = saturationConsumer;
    }

    @Override
    public int getXSize() {
        return 100;
    }

    @Override
    public int getYSize() {
        return 80;
    }

    @Override
    public void drawBackgroundLayer(GuiGraphicsExtractor stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        drawTiledGradient(stack, this.getPosX() + guiX, this.getPosY() + guiY, getXSize(), getYSize());
        AssetUtil.drawAsset(stack, screen, provider.getAsset(AssetTypes.SHADE_PICKER), guiX + (int) (this.getPosX() + this.saturation * this.getXSize()) - 4, (int) (guiY + this.getPosY() + (1 - this.brightness) * this.getYSize()) - 4);
    }

    @Override
    public void drawForegroundLayer(GuiGraphicsExtractor stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {

    }

    private void drawTiledGradient(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height) {
        int tileWidth = Math.round((float) width / S_TILES);
        int tileHeight = Math.round((float) height / V_TILES);
        for (int i = 0; i < 10; i++) {
            float minV = (float) i / V_TILES, maxV = (float) (i + 1) / V_TILES;
            for (int j = 0; j < 10; j++) {
                float minS = (float) j / S_TILES, maxS = (float) (j + 1) / S_TILES;
                Color tl = Color.getHSBColor(hueSupplier.get(), minS, maxV), tr = Color.getHSBColor(hueSupplier.get(), maxS, maxV), bl = Color.getHSBColor(hueSupplier.get(), minS, minV), br = Color.getHSBColor(hueSupplier.get(), maxS, minV);
                drawGradient(guiGraphics, x + j * tileWidth, y + (V_TILES - i - 1) * tileHeight, tileWidth, tileHeight, tl, tr, bl, br);
            }
        }
    }

    private void drawGradient(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, Color tl, Color tr, Color bl, Color br) {

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        this.saturation = (((float) mouseX - this.getPosX()) / getXSize());
        this.brightness = 1 - (((float) mouseY - this.getPosY()) / getYSize());
        this.saturationConsumer.accept(saturation);
        this.brightnessConsumer.accept(brightness);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.saturation = (float) ((mouseX - this.getPosX()) / getXSize());
        this.brightness = 1 - (float) ((mouseY - this.getPosY()) / getYSize());
        this.saturationConsumer.accept(saturation);
        this.brightnessConsumer.accept(brightness);
        return true;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }
}
