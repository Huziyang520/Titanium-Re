// Titanium Lite I Component Harness

package com.hrznstudio.titanium.component;

import net.minecraft.world.level.Level;

public interface IComponentHarness {

    Level getComponentWorld();

    void markComponentForUpdate(boolean referenced);

    void markComponentDirty();

}
