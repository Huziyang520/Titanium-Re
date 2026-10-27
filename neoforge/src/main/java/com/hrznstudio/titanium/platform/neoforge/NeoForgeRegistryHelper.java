// Titanium Lite NeoForge registry helper

package com.hrznstudio.titanium.platform.neoforge;

import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.module.DeferredRegistryHelper;

import java.util.HashMap;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.hrznstudio.titanium.block.tile.ITileFactory;

public final class NeoForgeRegistryHelper extends DeferredRegistryHelper {

    private final HashMap<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries;
    private final IEventBus bus;

    public NeoForgeRegistryHelper(String modId) {
        super(modId);
        this.registries = new HashMap<>();
        this.bus = ModList.get().getModContainerById(modId).orElseThrow().getEventBus();
    }

    @Override
    protected <T> Supplier<T> createRegistration(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier) {
        return getRegistry(key).register(name, supplier);
    }

    @Override
    protected <T> void onRegistered(ResourceKey<? extends Registry<T>> key, Supplier<T> registered) {
        if (!Registries.ITEM.equals(key)) return;
    }

    @Override
    public void registerCapabilities(Supplier<BlockEntityType<?>> type) {
        bus.addListener((final RegisterCapabilitiesEvent event) -> {
            event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.Item.BLOCK, type.get(), (object, context) -> {
                    if (object instanceof ActiveTile<?> tile) {
                        return tile.getItemHandler(context);
                    }
                    return null;
                }
            );
        });
    }


    private <T> DeferredRegister<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        DeferredRegister<T> register = (DeferredRegister<T>) this.registries.get(key);
        if (register == null) {
            register = DeferredRegister.create(key, this.modId());
            register.register(this.bus);
            this.registries.put(key, register);
        }
        return register;
    }

    @Override
    protected BlockEntityType<?> createBlockEntityType(ITileFactory<?> factory, Block block) {
        return new BlockEntityType<>((pos, state) -> factory.create(pos, state), java.util.Set.of(block));
    }
}
