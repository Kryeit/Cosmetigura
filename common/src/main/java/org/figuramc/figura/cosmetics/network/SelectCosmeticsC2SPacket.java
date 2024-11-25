package org.figuramc.figura.cosmetics.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.figuramc.figura.cosmetics.ServerSupplier;

import java.util.List;

public class SelectCosmeticsC2SPacket implements NetworkManager.NetworkReceiver {
    public static final ResourceLocation ID = new ResourceLocation("figura", "select_cosmetics");

    public static FriendlyByteBuf write(FriendlyByteBuf buf, long[] cosmetics) {
        buf.writeLongArray(cosmetics);
        return buf;
    }

    @Override
    public void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        long[] selectedCosmetics = buf.readLongArray();
        // TODO sanitize

        List<ServerPlayer> players = ServerSupplier.serverInstance.getPlayerList().getPlayers();
        NetworkManager.sendToPlayers(players, SetCosmeticsS2CPacket.ID, SetCosmeticsS2CPacket.write(new FriendlyByteBuf(Unpooled.buffer()), context.getPlayer().getUUID(), selectedCosmetics));
    }
}
