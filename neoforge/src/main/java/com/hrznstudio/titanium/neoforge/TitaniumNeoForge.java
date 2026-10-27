package com.hrznstudio.titanium.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.hrznstudio.titanium.Titanium;

/**
 * NeoForge entrypoint: only loader wiring lives here, everything else stays in common.
 */
@Mod(Titanium.MOD_ID)
public final class TitaniumNeoForge {

    public TitaniumNeoForge(IEventBus modBus) {
    }
}
