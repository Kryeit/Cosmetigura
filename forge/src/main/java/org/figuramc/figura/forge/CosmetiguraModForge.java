package org.figuramc.figura.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("figura")
public class CosmetiguraModForge {
    // dummy empty mod class, we are client only
    public CosmetiguraModForge() {
        MinecraftForge.EVENT_BUS.addListener(CosmetiguraModClientForge::cancelVanillaOverlays);
    }
}
