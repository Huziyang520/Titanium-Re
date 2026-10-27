// Titanium Lite Held Stack Locator Instance

package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.itemstack.ItemStackHarnessRegistry;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class HeldStackLocatorInstance extends LocatorInstance {
    private boolean mainHand;

    public HeldStackLocatorInstance() {
        this(true);
    }

    public HeldStackLocatorInstance(boolean mainHand) {
        super(LocatorTypes.HELD_STACK);
        this.mainHand = mainHand;
    }

    @Override
    public Optional<?> locale(Player playerEntity) {
        return Optional.of(playerEntity.getItemInHand(mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND))
            .map(ItemStackHarnessRegistry::createItemStackHarness).orElseGet(null);
    }

    public boolean isMainHand() {
        return mainHand;
    }
}
