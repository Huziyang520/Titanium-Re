// Loader neutral fluid storage used by Titanium components

package com.hrznstudio.titanium.transfer;

public interface IFluidStore {

    int size();

    FluidAmount get(int slot);

    void set(int slot, FluidAmount amount);

    long capacity(int slot);

    FluidAmount insert(int slot, FluidAmount amount, boolean simulate);

    FluidAmount extract(int slot, long amount, boolean simulate);
}
