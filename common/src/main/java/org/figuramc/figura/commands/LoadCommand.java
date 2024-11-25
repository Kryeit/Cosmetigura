package org.figuramc.figura.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.figuramc.figura.cosmetics.network.SelectCosmeticsC2SPacket;
import org.figuramc.figura.utils.FiguraClientCommandSource;
import org.figuramc.figura.utils.FiguraText;

class LoadCommand {

    public static LiteralArgumentBuilder<FiguraClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FiguraClientCommandSource> load = LiteralArgumentBuilder.literal("load");

        RequiredArgumentBuilder<FiguraClientCommandSource, String> path = RequiredArgumentBuilder.argument("path", StringArgumentType.greedyString());
        path.executes(LoadCommand::loadAvatar);

        return load.then(path);
    }

    private static int loadAvatar(CommandContext<FiguraClientCommandSource> context) {
        String str = StringArgumentType.getString(context, "path");
        try {
            // parse path
//            Path p = LocalAvatarFetcher.getLocalAvatarDirectory().resolve(Path.of(str));
//
//            // try to load avatar
//            AvatarManager.loadLocalAvatar(p);

            String[] split = str.split(" ");
            long[] cosmetics = new long[split.length];
            for (int i = 0; i < split.length; i++) {
                cosmetics[i] = Long.parseLong(split[i]);
            }

            NetworkManager.sendToServer(SelectCosmeticsC2SPacket.ID, SelectCosmeticsC2SPacket.write(new FriendlyByteBuf(Unpooled.buffer()), cosmetics));
            context.getSource().figura$sendFeedback(FiguraText.of("command.load.loading"));
            return 1;
        } catch (Exception e) {
            context.getSource().figura$sendError(FiguraText.of("command.load.invalid", str));
        }

        return 0;
    }
}
