// Titanium Lite NeoForge entrypoint

package com.hrznstudio.titanium.neoforge;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.platform.Platform;
import com.hrznstudio.titanium.platform.neoforge.NeoForgePlatform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Titanium.MODID)
public final class TitaniumNeoForge {

    public TitaniumNeoForge(IEventBus modBus) {
        Platform.install(new NeoForgePlatform(Titanium.MODID));
    }
}
