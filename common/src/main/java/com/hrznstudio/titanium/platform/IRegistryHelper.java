// Registry access used by common registration code

package com.hrznstudio.titanium.platform;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface IRegistryHelper {

    <T> Supplier<T> register(Registry<T> registry, String name, Supplier<T> supplier);

    <T> Supplier<T> registerFor(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier);
}
