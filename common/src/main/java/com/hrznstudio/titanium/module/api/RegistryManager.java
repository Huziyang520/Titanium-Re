// Titanium Lite Registry Manager

package com.hrznstudio.titanium.module.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryManager<B> {
    <T> B content(ResourceKey<? extends Registry<T>> registryKey, T t);
}
