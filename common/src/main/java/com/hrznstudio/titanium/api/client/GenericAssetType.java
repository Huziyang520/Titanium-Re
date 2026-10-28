// Titanium Lite Generic Asset Type

package com.hrznstudio.titanium.api.client;

import java.util.function.Function;
import java.util.function.Supplier;

public class GenericAssetType<T extends IAsset> implements IAssetType<T> {
    private Supplier<T> defaultAsset;
    private Class<T> tClass;

    public GenericAssetType(Supplier<T> defaultAsset, Class<T> tClass) {
        this.defaultAsset = defaultAsset;
        this.tClass = tClass;
    }

    public GenericAssetType(Function<IAssetType<T>, T> defaultAsset, Class<T> tClass) {
        this.defaultAsset = () -> defaultAsset.apply(this);
        this.tClass = tClass;
    }

    @Override
    public T getDefaultAsset() {
        return defaultAsset.get();
    }

    @Override
    public T castOrDefault(IAsset asset) throws ClassCastException {
        if (tClass.isInstance(asset))
            return tClass.cast(asset);
        return defaultAsset.get();
    }
}
