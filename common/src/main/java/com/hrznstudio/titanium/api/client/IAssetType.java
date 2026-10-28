// Titanium Lite I Asset Type

package com.hrznstudio.titanium.api.client;

public interface IAssetType<T extends IAsset> {

    T getDefaultAsset();

    T castOrDefault(IAsset asset) throws ClassCastException;
}
