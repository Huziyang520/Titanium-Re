// Titanium Lite Message

package com.hrznstudio.titanium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;

import java.io.Serializable;
import java.lang.reflect.Field;

public abstract class Message implements Serializable {

    protected abstract void handleServerMessage(IMessageContext context);

    public void handleClientMessage(IMessageContext context) {

    }

    public final void fromBytes(RegistryFriendlyByteBuf buf) {
        try {
            Class<?> clazz = getClass();
            for (Field f : clazz.getDeclaredFields()) {
                if (!f.isAccessible()) f.setAccessible(true);
                Class<?> type = f.getType();
                if (CompoundSerializableDataHandler.acceptField(f, type))
                    CompoundSerializableDataHandler.readField(f, type, buf, this);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error at reading packet " + this, e);
        }
    }

    public final void toBytes(RegistryFriendlyByteBuf buf) {
        try {
            Class<?> clazz = getClass();
            for (Field f : clazz.getDeclaredFields()) {
                if (!f.isAccessible()) f.setAccessible(true);
                Class<?> type = f.getType();
                if (CompoundSerializableDataHandler.acceptField(f, type))
                    CompoundSerializableDataHandler.writeField(f, type, buf, this);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error at writing packet " + this, e);
        }
    }

}
