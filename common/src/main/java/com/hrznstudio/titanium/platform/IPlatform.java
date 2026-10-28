// Loader services that common code is allowed to call

package com.hrznstudio.titanium.platform;

import java.nio.file.Path;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IPlatform {

    String name();

    boolean isClient();

    Path configDirectory();

    IRegistryHelper registries();

    IClassScanner scanner();

    void invalidateCapabilities(BlockEntity blockEntity);
}
