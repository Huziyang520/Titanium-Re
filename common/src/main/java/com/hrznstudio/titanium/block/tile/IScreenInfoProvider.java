// Titanium Lite I Screen Info Provider

package com.hrznstudio.titanium.block.tile;

import net.minecraft.network.chat.TextColor;

public interface IScreenInfoProvider {
    default int getTitleColor() {
        return TextColor.DARK_GRAY.getValue();
    }

    default float getTitleXPos(float titleWidth, float screenWidth, float screenHeight, float guiWidth, float guiHeight) {
        return (screenWidth - guiWidth) / 2 + guiWidth / 2 - titleWidth / 2;
    }

    default float getTitleYPos(float titleWidth, float screenWidth, float screenHeight, float guiWidth, float guiHeight) {
        return ((screenHeight - guiHeight) / 2) + 6;
    }
}
