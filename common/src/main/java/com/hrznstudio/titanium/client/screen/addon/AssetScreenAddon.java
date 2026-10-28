// Titanium Lite Asset Screen Addon

package com.hrznstudio.titanium.client.screen.addon;

import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;

public class AssetScreenAddon extends BasicScreenAddon {

    private IAssetType assetType;
    private boolean isBackground;
    private Rectangle area;

    public AssetScreenAddon(IAssetType assetType, int posX, int posY, boolean isBackground) {
        super(posX, posY);
        this.assetType = assetType;
        this.isBackground = isBackground;
    }

    @Override
    public int getXSize() {
        return area != null ? area.width : 0;
    }

    @Override
    public int getYSize() {
        return area != null ? area.height : 0;
    }

    @Override
    public void drawBackgroundLayer(GuiGraphicsExtractor stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        if (assetType != null && isBackground()) {
            IAsset asset = provider.getAsset(assetType);
            area = asset.getArea();
            AssetUtil.drawAsset(stack, screen, asset, this.getPosX() + guiX, this.getPosY() + guiY);
        }
    }

    @Override
    public void drawForegroundLayer(GuiGraphicsExtractor guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        if (assetType != null && !isBackground()) {
            AssetUtil.drawAsset(guiGraphics, screen, provider.getAsset(assetType), this.getPosX() + guiX, this.getPosY() + guiY);
        }
    }

    @Override
    public boolean isBackground() {
        return isBackground;
    }
}
