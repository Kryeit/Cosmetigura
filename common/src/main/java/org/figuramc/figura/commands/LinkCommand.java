package org.figuramc.figura.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import org.figuramc.figura.CosmetiguraMod;
import org.figuramc.figura.utils.ColorUtils;
import org.figuramc.figura.utils.FiguraClientCommandSource;
import org.figuramc.figura.utils.FiguraText;

import java.util.ArrayList;
import java.util.List;

class LinkCommand {

    private static final List<CosmetiguraMod.Links> LINKS = new ArrayList<>() {{
            add(CosmetiguraMod.Links.Wiki);
            add(CosmetiguraMod.Links.Kofi);
            add(CosmetiguraMod.Links.OpenCollective);
            add(null);
            add(CosmetiguraMod.Links.Discord);
            add(CosmetiguraMod.Links.Github);
            add(null);
            add(CosmetiguraMod.Links.Modrinth);
            add(CosmetiguraMod.Links.Curseforge);
    }};

    public static LiteralArgumentBuilder<FiguraClientCommandSource> getCommand() {
        // get links
        LiteralArgumentBuilder<FiguraClientCommandSource> links = LiteralArgumentBuilder.literal("links");
        links.executes(context -> {
            // header
            MutableComponent message = Component.empty().withStyle(ColorUtils.Colors.AWESOME_BLUE.style)
                    .append(Component.literal("•*+•* ")
                            .append(FiguraText.of())
                            .append(" Links *•+*•").withStyle(ChatFormatting.UNDERLINE))
                    .append("\n");

            // add links
            for (CosmetiguraMod.Links link : LINKS) {
                message.append("\n");

                if (link == null)
                    continue;

                message.append(Component.literal("• [" + link.name() + "]")
                        .withStyle(link.style)
                        .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link.url)))
                        .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(link.url)))));
            }

            CosmetiguraMod.sendChatMessage(message);
            return 1;
        });

        return links;
    }
}
