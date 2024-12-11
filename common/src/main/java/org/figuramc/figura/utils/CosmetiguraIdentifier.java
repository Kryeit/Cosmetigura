package org.figuramc.figura.utils;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.figura.CosmetiguraMod;

public class CosmetiguraIdentifier extends ResourceLocation {

    public CosmetiguraIdentifier(String string) {
        super(CosmetiguraMod.MOD_ID, string);
    }

    public static String formatPath(String path) {
        return Util.sanitizeName(path, ResourceLocation::validPathChar);
    }
}
