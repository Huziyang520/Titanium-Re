// Titanium Lite I Component Bundle

package com.hrznstudio.titanium.component;

import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;

public interface IComponentBundle extends IScreenAddonProvider, IContainerAddonProvider {

    void accept(IComponentHandler... handler);

}
