// Titanium Lite I Asset

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.resources.Identifier;

import java.awt.*;

public interface IAsset {

    default Identifier getIdentifier() {
        return IAssetProvider.DEFAULT_LOCATION;
    }

    Rectangle getArea();

    default Point getOffset() {
        return new Point();
    }

}
