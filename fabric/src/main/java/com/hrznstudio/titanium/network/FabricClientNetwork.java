// Client only half of the payload transport, loaded only when the environment is client

package com.hrznstudio.titanium.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class FabricClientNetwork {

    private FabricClientNetwork() {
    }

    public static void bind(CustomPacketPayload.Type<FabricNetworkHandler.FabricPayload> type) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) ->
            context.client().execute(() -> payload.message().handleClientMessage(
                new FabricMessageContext(context.player(), context.client()::execute))));
    }

    public static void send(FabricNetworkHandler.FabricPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
