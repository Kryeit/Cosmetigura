package org.figuramc.figura.utils.fabric;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.figuramc.figura.utils.CosmetiguraIdentifier;
import org.figuramc.figura.utils.FiguraResourceListener;

import java.util.function.Consumer;

public class FiguraResourceListenerImpl extends FiguraResourceListener implements SimpleSynchronousResourceReloadListener {
    public FiguraResourceListenerImpl(String id, Consumer<ResourceManager> reloadConsumer) {
        super(id, reloadConsumer);
    }

    public static FiguraResourceListener createResourceListener(String id, Consumer<ResourceManager> reloadConsumer) {
        return new FiguraResourceListenerImpl(id, reloadConsumer);
    }

    public ResourceLocation getFabricId() {
        return new CosmetiguraIdentifier(this.id());
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        reloadConsumer().accept(manager);
    }
}
