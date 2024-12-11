package org.figuramc.figura.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.figuramc.figura.cosmetics.BackendConfig;
import org.figuramc.figura.cosmetics.network.FiguraNetworkManager;

public class CosmetiguraModFabricServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        FiguraNetworkManager.init(true);
        System.out.println(BackendConfig.apiUrl);
    }
}
