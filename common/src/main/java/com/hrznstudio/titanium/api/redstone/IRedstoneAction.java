// Titanium Lite I Redstone Action

package com.hrznstudio.titanium.api.redstone;

public interface IRedstoneAction {

    boolean canRun(IRedstoneState state);

    boolean startsOnChange();

    String getName();

}
