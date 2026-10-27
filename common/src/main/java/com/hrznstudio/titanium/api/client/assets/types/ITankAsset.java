// Titanium Lite I Tank Asset

package com.hrznstudio.titanium.api.client.assets.types;

import com.hrznstudio.titanium.api.client.IAsset;
import net.minecraft.core.Direction;

public interface ITankAsset extends IAsset {

    int getFluidRenderPadding(Direction facing);

}
