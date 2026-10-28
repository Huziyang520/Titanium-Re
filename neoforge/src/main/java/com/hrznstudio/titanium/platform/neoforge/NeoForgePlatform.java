// Titanium Lite NeoForge platform

package com.hrznstudio.titanium.platform.neoforge;

import com.hrznstudio.titanium.platform.IClassScanner;
import com.hrznstudio.titanium.platform.IPlatform;
import com.hrznstudio.titanium.platform.IRegistryHelper;

import java.nio.file.Path;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

public final class NeoForgePlatform implements IPlatform {

    private final NeoForgeRegistryHelper registries;
    private final NeoForgeClassScanner scanner;

    public NeoForgePlatform(String modId) {
        this.registries = new NeoForgeRegistryHelper(modId);
        this.scanner = new NeoForgeClassScanner();
    }

    @Override
    public String name() {
        return "neoforge";
    }

    @Override
    public boolean isClient() {
        return FMLEnvironment.getDist() == Dist.CLIENT;
    }

    @Override
    public Path configDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public IRegistryHelper registries() {
        return this.registries;
    }

    @Override
    public IClassScanner scanner() {
        return this.scanner;
    }

    @Override
    public void invalidateCapabilities(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            level.invalidateCapabilities(blockEntity.getBlockPos());
        }
    }
}
