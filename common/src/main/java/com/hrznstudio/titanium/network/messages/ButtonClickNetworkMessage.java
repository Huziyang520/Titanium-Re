// Titanium Lite Button Click Network Message

package com.hrznstudio.titanium.network.messages;

import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.network.IMessageContext;
import com.hrznstudio.titanium.network.Message;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.util.CastingUtil;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public class ButtonClickNetworkMessage extends Message {
    private LocatorInstance locatorInstance;
    private int id;
    private CompoundTag data;

    public ButtonClickNetworkMessage(LocatorInstance locatorInstance, int id, CompoundTag data) {
        this.locatorInstance = locatorInstance;
        this.id = id;
        this.data = data;
    }

    public ButtonClickNetworkMessage() {

    }

    @Override
    protected void handleServerMessage(IMessageContext context) {
        Optional.of(context.player())
            .flatMap(locatorInstance::locale)
            .flatMap(CastingUtil.attemptCast(IButtonHandler.class))
            .ifPresent(iButtonHandler -> iButtonHandler.handleButtonMessage(id, context.player(), data));
    }
}
