// Titanium Lite I Background Asset

package com.hrznstudio.titanium.api.client.assets.types;

import com.hrznstudio.titanium.api.client.IAsset;

import java.awt.*;

public interface IBackgroundAsset extends IAsset {
    Point getInventoryPosition();

    Point getHotbarPosition();
}
