package org.figuramc.figura.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.PathPackResources;
import org.figuramc.figura.CosmetiguraMod;
import org.figuramc.figura.utils.IOUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FiguraRuntimeResources {

    private static String ASSETS_VERSION;
    public static final PathPackResources PACK = new PathPackResources(CosmetiguraMod.MOD_NAME + " runtime resource pack", getRootDirectory(), true);

    public static Path getRootDirectory() {
        return IOUtils.getOrCreateDir(CosmetiguraMod.getCacheDirectory(), "resources");
    }

    public static Path getAssetsDirectory() {
        return IOUtils.getOrCreateDir(getRootDirectory(), "assets/" + CosmetiguraMod.MOD_ID);
    }

    public static String getAssetsVersion() {
        if (ASSETS_VERSION == null)
             ASSETS_VERSION = CosmetiguraMod.METADATA.getCustomValueAsString("assets_version");
        return ASSETS_VERSION;
    }

    private static CompletableFuture<Void> future;

    public static void clearCache() {
        IOUtils.deleteFile(getRootDirectory());
    }

    public static CompletableFuture<Void> init() {
        return future = CompletableFuture.runAsync(() -> {
            CosmetiguraMod.LOGGER.info("Fetching backend resources...");

            JsonObject hashes, oldHashes;

        });
    }

    public static JsonObject constructAssetsDirectoryJsonObject(File[] files) {
        JsonObject object = new JsonObject();
        for (File file : files) {
            if (file.isDirectory()) {
                for (Map.Entry<String, JsonElement> entry : constructAssetsDirectoryJsonObject(file.listFiles()).entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsString());
                }
            } else if (!file.isHidden()){
                object.addProperty(getAssetsDirectory().toUri().relativize(file.toURI()).getPath(), "0");
            }
        }
        return object;
    }

    public static void joinFuture() {
        if (future != null && !future.isDone())
            future.join();
    }
}
