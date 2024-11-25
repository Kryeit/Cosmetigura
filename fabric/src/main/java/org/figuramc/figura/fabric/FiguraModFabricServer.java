package org.figuramc.figura.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.figuramc.figura.cosmetics.network.FiguraNetworkManager;

public class FiguraModFabricServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        FiguraNetworkManager.init(true);
    }
}
