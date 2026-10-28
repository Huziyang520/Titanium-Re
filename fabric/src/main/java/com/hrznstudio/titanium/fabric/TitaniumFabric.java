// Fabric entrypoint installing the platform, network and store services

package com.hrznstudio.titanium.fabric;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.network.FabricNetworkHandler;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.network.messages.TileFieldNetworkMessage;
import com.hrznstudio.titanium.platform.Platform;
import com.hrznstudio.titanium.platform.fabric.FabricPlatform;
import com.hrznstudio.titanium.transfer.FabricStoreProvider;
import com.hrznstudio.titanium.transfer.Stores;

import net.fabricmc.api.ModInitializer;

public final class TitaniumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Platform.install(new FabricPlatform(Titanium.MODID));
        FabricNetworkHandler network = new FabricNetworkHandler(Titanium.MODID);
        network.registerMessage("tile_field", TileFieldNetworkMessage.class);
        network.registerMessage("button_click", ButtonClickNetworkMessage.class);
        Titanium.NETWORK = network;
        Stores.install(new FabricStoreProvider());
    }
}
