// Titanium Lite Function Reference Holder

package com.hrznstudio.titanium.container.referenceholder;

import net.minecraft.world.inventory.DataSlot;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class FunctionReferenceHolder extends DataSlot {
    private final IntConsumer setter;
    private final IntSupplier getter;

    public FunctionReferenceHolder(IntConsumer setter, IntSupplier getter) {
        this.setter = setter;
        this.getter = getter;
    }

    @Override
    public int get() {
        return getter.getAsInt();
    }

    @Override
    public void set(int i) {
        setter.accept(i);
    }
}
