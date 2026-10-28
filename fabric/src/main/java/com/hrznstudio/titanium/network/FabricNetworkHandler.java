// Fabric networking built on the play channel payload registry

package com.hrznstudio.titanium.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class FabricNetworkHandler implements INetworkHandler {

    private final String namespace;
    private final Map<Class<? extends Message>, CustomPacketPayload.Type<FabricPayload>> types = new ConcurrentHashMap<>();

    public FabricNetworkHandler(String modId) {
        this.namespace = modId;
    }

    public <REQ extends Message> void registerMessage(String name, Class<REQ> message) {
        Identifier id = Identifier.fromNamespaceAndPath(this.namespace, name);
        CustomPacketPayload.Type<FabricPayload> type = new CustomPacketPayload.Type<>(id);
        StreamCodec<RegistryFriendlyByteBuf, FabricPayload> codec = StreamCodec.of(
            (buffer, payload) -> payload.message().toBytes(buffer),
            buffer -> new FabricPayload(type, read(message, buffer)));
        PayloadTypeRegistry.serverboundPlay().register(type, codec);
        PayloadTypeRegistry.clientboundPlay().register(type, codec);
        this.types.put(message, type);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) ->
            context.server().execute(() -> payload.message().handleServerMessage(
                new FabricMessageContext(context.player(), context.server()::execute))));
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            FabricClientNetwork.bind(type);
        }
    }

    @Override
    public void sendToServer(Message message) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            return;
        }
        FabricClientNetwork.send(wrap(message));
    }

    @Override
    public void sendToNearby(Level world, BlockPos pos, int distance, Message message) {
        if (!(world instanceof ServerLevel serverLevel)) {
            return;
        }
        Vec3 centre = pos.getCenter();
        for (ServerPlayer player : PlayerLookup.around(serverLevel, centre, distance)) {
            ServerPlayNetworking.send(player, wrap(message));
        }
    }

    @Override
    public void sendTo(Message message, ServerPlayer player) {
        ServerPlayNetworking.send(player, wrap(message));
    }

    public FabricPayload wrap(Message message) {
        CustomPacketPayload.Type<FabricPayload> type = this.types.get(message.getClass());
        if (type == null) {
            throw new IllegalStateException("message not registered: " + message.getClass().getName());
        }
        return new FabricPayload(type, message);
    }

    private static <REQ extends Message> Message read(Class<REQ> message, RegistryFriendlyByteBuf buffer) {
        try {
            REQ instance = message.getConstructor().newInstance();
            instance.fromBytes(buffer);
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("unable to read message " + message.getName(), e);
        }
    }

    public record FabricPayload(CustomPacketPayload.Type<FabricPayload> type, Message message) implements CustomPacketPayload {
    }
}
