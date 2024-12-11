package org.figuramc.figura.utils.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import org.figuramc.figura.CosmetiguraMod;
import org.figuramc.figura.utils.PlatformUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

public class PlatformUtilsImpl {
    public static Path getGameDir() {
        return FMLPaths.GAMEDIR.relative();
    }

    public static String getFiguraModVersionString() {
        return ModList.get().getModContainerById(CosmetiguraMod.MOD_ID).get().getModInfo().getVersion().toString();
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.relative();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static String getModVersion(String modId) {
        return ModList.get().getModContainerById(modId).get().getModInfo().getVersion().getQualifier();
    }

    public static PlatformUtils.ModLoader getModLoader() {
        return PlatformUtils.ModLoader.FORGE;
    }

    public static InputStream loadFileFromRoot(String path) throws FileNotFoundException {
        File file = ModList.get().getModFileById(CosmetiguraMod.MOD_ID).getFile().findResource(path).toFile();
        return new FileInputStream(file);
    }
}
