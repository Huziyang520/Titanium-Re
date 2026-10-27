// Titanium Lite mod identifiers and logger

package com.hrznstudio.titanium;

import com.hrznstudio.titanium.network.INetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Titanium {

    public static final String MODID = "titanium";
    public static final String MOD_NAME = "Titanium Lite";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static INetworkHandler NETWORK;

    private Titanium() {
    }
}
