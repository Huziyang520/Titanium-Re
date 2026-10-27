// Loader specific fluid stack serializer plugged into the field handler

package com.hrznstudio.titanium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IFluidStackCodec {

    Class<?> stackType();

    Object read(RegistryFriendlyByteBuf buf);

    void write(RegistryFriendlyByteBuf buf, Object stack);
}
