// Titanium Lite Arrow Button Component

package com.hrznstudio.titanium.component.button;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.client.screen.addon.ArrowButtonScreenAddon;
import com.hrznstudio.titanium.util.FacingUtil;

import java.util.Collections;
import java.util.List;

public class ArrowButtonComponent extends ButtonComponent {

    public final FacingUtil.Sideness direction;

    public ArrowButtonComponent(int posX, int posY, int sizeX, int sizeY, FacingUtil.Sideness direction) {
        super(posX, posY, sizeX, sizeY);
        this.direction = direction;
    }

    @Override
    public List<IFactory<?>> getScreenAddons() {
        return Collections.singletonList(() -> new ArrowButtonScreenAddon(this));
    }

    public FacingUtil.Sideness getDirection() {
        return direction;
    }
}
