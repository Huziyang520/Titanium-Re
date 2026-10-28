// Fabric registry helper backed by the vanilla Registry entry points

package com.hrznstudio.titanium.platform.fabric;

import com.hrznstudio.titanium.block.tile.ITileFactory;
import com.hrznstudio.titanium.module.DeferredRegistryHelper;

import java.util.function.Supplier;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class FabricRegistryHelper extends DeferredRegistryHelper {

    public FabricRegistryHelper(String modId) {
        super(modId);
    }

    @Override
    protected <T> Supplier<T> createRegistration(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier) {
        Identifier id = Identifier.fromNamespaceAndPath(modId(), name);
        T value = Registry.register(registry(key), id, supplier.get());
        return () -> value;
    }

    @Override
    protected <T> Holder<T> createHolder(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier) {
        Identifier id = Identifier.fromNamespaceAndPath(modId(), name);
        return Registry.registerForHolder(registry(key), id, supplier.get());
    }

    @Override
    protected BlockEntityType<?> createBlockEntityType(ITileFactory<?> factory, Block block) {
        return FabricBlockEntityTypeBuilder.create((pos, state) -> factory.create(pos, state), block).build();
    }

    @SuppressWarnings("unchecked")
    private static <T> Registry<T> registry(ResourceKey<? extends Registry<T>> key) {
        Registry<?> registry = BuiltInRegistries.REGISTRY.getValue(key.identifier());
        if (registry == null) {
            throw new IllegalStateException("unknown registry " + key.identifier());
        }
        return (Registry<T>) registry;
    }
}
