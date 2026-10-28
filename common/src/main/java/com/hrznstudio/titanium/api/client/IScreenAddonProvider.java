// Titanium Lite I Screen Addon Provider

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.api.IFactory;

import javax.annotation.Nonnull;
import java.util.List;

public interface IScreenAddonProvider {
    @Nonnull
    List<IFactory<?>> getScreenAddons();

}
