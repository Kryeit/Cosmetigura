package org.figuramc.figura.config.forge;

import org.figuramc.figura.config.ConfigKeyBind;
import org.figuramc.figura.forge.CosmetiguraModClientForge;

public class ConfigKeyBindImpl {
    public static void addKeyBind(ConfigKeyBind keyBind) {
        CosmetiguraModClientForge.KEYBINDS.add(keyBind);
    }
}
