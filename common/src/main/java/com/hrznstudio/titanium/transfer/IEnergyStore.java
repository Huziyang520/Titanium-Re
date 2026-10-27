// Loader neutral energy storage used by Titanium components

package com.hrznstudio.titanium.transfer;

public interface IEnergyStore {

    int stored();

    void setStored(int energy);

    int capacity();

    int insert(int amount, boolean simulate);

    int extract(int amount, boolean simulate);
}
