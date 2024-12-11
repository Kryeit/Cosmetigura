package org.figuramc.figura.commands;

import com.mojang.brigadier.context.CommandContext;
import org.figuramc.figura.CosmetiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.model.rendering.AvatarRenderer;
import org.figuramc.figura.utils.FiguraClientCommandSource;
import org.figuramc.figura.utils.FiguraText;

public class FiguraCommands {

    protected static Avatar checkAvatar(CommandContext<FiguraClientCommandSource> context) {
        Avatar avatar = AvatarManager.getAvatarForPlayer(CosmetiguraMod.getLocalPlayerUUID());
        if (avatar == null) {
            context.getSource().figura$sendError(FiguraText.of("command.no_avatar_error"));
            return null;
        }
        return avatar;
    }

    protected static FiguraLuaRuntime getRuntime(CommandContext<FiguraClientCommandSource> context) {
        Avatar avatar = checkAvatar(context);
        if (avatar == null)
            return null;
        if (avatar.luaRuntime == null || avatar.scriptError) {
            context.getSource().figura$sendError(FiguraText.of("command.no_script_error"));
            return null;
        }
        return avatar.luaRuntime;
    }

    protected static AvatarRenderer getRenderer(CommandContext<FiguraClientCommandSource> context) {
        Avatar avatar = checkAvatar(context);
        if (avatar == null)
            return null;
        if (avatar.renderer == null) {
            context.getSource().figura$sendError(FiguraText.of("command.no_renderer_error"));
            return null;
        }
        return avatar.renderer;
    }
}
