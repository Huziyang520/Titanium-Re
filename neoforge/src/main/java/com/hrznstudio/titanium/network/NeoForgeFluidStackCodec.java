// Fluid stack codec backed by the loader per stack stream codec

package com.hrznstudio.titanium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.fluids.FluidStack;

public final class NeoForgeFluidStackCodec implements IFluidStackCodec {

    private static boolean installed;

    public static void install() {
        if (installed) {
            return;
        }
        installed = true;
        CompoundSerializableDataHandler.setFluidStackCodec(new NeoForgeFluidStackCodec());
    }

    @Override
    public Class<?> stackType() {
        return FluidStack.class;
    }

    @Override
    public Object read(RegistryFriendlyByteBuf buf) {
        return FluidStack.STREAM_CODEC.decode(buf);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf, Object stack) {
        FluidStack.STREAM_CODEC.encode(buf, (FluidStack) stack);
    }
}
