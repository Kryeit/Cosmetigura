package org.figuramc.figura.utils;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.figuramc.figura.CosmetiguraMod;

public class FiguraText extends TranslatableContents {

    public FiguraText() {
        super(CosmetiguraMod.MOD_ID, CosmetiguraMod.MOD_NAME, NO_ARGS);
    }

    public FiguraText(String string) {
        super(CosmetiguraMod.MOD_ID + "." + string, null, NO_ARGS);
    }

    public FiguraText(String string, Object... args) {
        super(CosmetiguraMod.MOD_ID + "." + string, null, args);
    }

    public static MutableComponent of() {
        return MutableComponent.create(new FiguraText());
    }

    public static MutableComponent of(String string) {
        return MutableComponent.create(new FiguraText(string));
    }

    public static MutableComponent of(String string, Object... args) {
        return MutableComponent.create(new FiguraText(string, args));
    }
}