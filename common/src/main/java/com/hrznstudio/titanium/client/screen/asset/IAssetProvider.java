// Titanium Lite I Asset Provider

package com.hrznstudio.titanium.client.screen.asset;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import net.minecraft.resources.Identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IAssetProvider {
    Identifier DEFAULT_LOCATION = Identifier.fromNamespaceAndPath(Titanium.MODID, "textures/gui/background.png");
    Identifier DEFAULT_LOCATION_ALT = Identifier.fromNamespaceAndPath(Titanium.MODID, "textures/gui/background_alt.png");

    DefaultAssetProvider DEFAULT_PROVIDER = new DefaultAssetProvider();

    @Nonnull
    static <T extends IAsset> T getAsset(IAssetProvider provider, IAssetType<T> type) {
        T asset = provider.getAsset(type);
        if (asset == null && provider != DEFAULT_PROVIDER)
            asset = DEFAULT_PROVIDER.getAsset(type);
        return asset != null ? asset : type.getDefaultAsset();
    }

    @Nullable
    <T extends IAsset> T getAsset(IAssetType<T> assetType);

}
