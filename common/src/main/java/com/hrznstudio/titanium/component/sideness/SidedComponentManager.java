// Titanium Lite Sided Component Manager

package com.hrznstudio.titanium.component.sideness;

import com.hrznstudio.titanium.api.client.IAssetType;

public class SidedComponentManager {
    private final int posX;
    private final int posY;
    private final IAssetType<?> assetType;

    public SidedComponentManager(int posX, int posY, IAssetType<?> assetType) {
        this.posX = posX;
        this.posY = posY;
        this.assetType = assetType;
    }

    public static SidedComponentManager ofRight(int x, int y, int position, IAssetType<?> managerIconType, int padding) {
        return new SidedComponentManager(x + (managerIconType.getDefaultAsset().getArea().width + padding) * position, y, managerIconType);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public IAssetType<?> getAssetType() {
        return assetType;
    }
}
