// Titanium Lite Empty Locator Instance

package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class EmptyLocatorInstance extends LocatorInstance {
    public EmptyLocatorInstance() {
        super(LocatorTypes.EMPTY);
    }

    @Override
    public Optional<?> locale(Player playerEntity) {
        return Optional.empty();
    }
}
