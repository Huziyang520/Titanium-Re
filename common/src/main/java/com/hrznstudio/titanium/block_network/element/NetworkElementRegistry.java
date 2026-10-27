// Titanium Lite Network Element Registry

package com.hrznstudio.titanium.block_network.element;

import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class NetworkElementRegistry {

    public static final NetworkElementRegistry INSTANCE = new NetworkElementRegistry();

    private final Map<Identifier, NetworkElementFactory> factories = new HashMap<>();

    private NetworkElementRegistry() {
    }

    public void addFactory(Identifier id, NetworkElementFactory factory) {
        if (factories.containsKey(id)) {
            throw new RuntimeException("Cannot register duplicate pipe factory " + id.toString());
        }

        factories.put(id, factory);
    }

    @Nullable
    public NetworkElementFactory getFactory(Identifier id) {
        return factories.get(id);
    }

}
