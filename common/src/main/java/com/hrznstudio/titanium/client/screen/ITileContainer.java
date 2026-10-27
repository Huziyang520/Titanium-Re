// Titanium Lite I Tile Container

package com.hrznstudio.titanium.client.screen;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface ITileContainer<T extends BlockEntity> {

    T getTile();

}
