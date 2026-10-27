// Titanium Lite I Container Addon Provider

package com.hrznstudio.titanium.container.addon;

import com.hrznstudio.titanium.api.IFactory;

import javax.annotation.Nonnull;
import java.util.List;

public interface IContainerAddonProvider {
    @Nonnull
    List<IFactory<? extends IContainerAddon>> getContainerAddons();

    default boolean canInteract(){
        return true;
    }

}
