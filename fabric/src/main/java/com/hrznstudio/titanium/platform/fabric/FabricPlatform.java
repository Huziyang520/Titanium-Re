// Fabric implementation of the loader services exposed to common code

package com.hrznstudio.titanium.platform.fabric;

import com.hrznstudio.titanium.platform.IClassScanner;
import com.hrznstudio.titanium.platform.IPlatform;
import com.hrznstudio.titanium.platform.IRegistryHelper;

import java.nio.file.Path;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class FabricPlatform implements IPlatform {

    private final FabricRegistryHelper registries;
    private final FabricClassScanner scanner;

    public FabricPlatform(String modId) {
        this.registries = new FabricRegistryHelper(modId);
        this.scanner = new FabricClassScanner();
    }

    @Override
    public String name() {
        return "fabric";
    }

    @Override
    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public Path configDirectory() {
        return FabricLoader.getInstance().getConfigDir();
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
    }
}
