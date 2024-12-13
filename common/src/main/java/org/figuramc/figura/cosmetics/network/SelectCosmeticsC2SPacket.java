package org.figuramc.figura.cosmetics.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.figuramc.figura.cosmetics.CosmeticBackend;
import org.figuramc.figura.cosmetics.ServerSupplier;

import java.util.List;

public class SelectCosmeticsC2SPacket implements NetworkManager.NetworkReceiver {
    public static final ResourceLocation ID = new ResourceLocation("cosmetigura", "select_cosmetics");

    public static FriendlyByteBuf write(FriendlyByteBuf buf, long cosmeticId, boolean equipped) {
        buf.writeLong(cosmeticId);
        buf.writeBoolean(equipped);
        return buf;
    }

    @Override
    public void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        long cosmeticId = buf.readLong();
        boolean equip = buf.readBoolean();

        if (equip) {
            CosmeticBackend.equipCosmetic(context.getPlayer().getUUID(), cosmeticId).thenAccept(equipped -> {
                List<ServerPlayer> players = ServerSupplier.serverInstance.getPlayerList().getPlayers();
                NetworkManager.sendToPlayers(players, SetCosmeticsS2CPacket.ID, SetCosmeticsS2CPacket.write(new FriendlyByteBuf(Unpooled.buffer()), context.getPlayer().getUUID(), equipped));
            });
        } else {
            CosmeticBackend.unequipCosmetic(context.getPlayer().getUUID(), cosmeticId).thenAccept(equipped -> {
                List<ServerPlayer> players = ServerSupplier.serverInstance.getPlayerList().getPlayers();
                NetworkManager.sendToPlayers(players, SetCosmeticsS2CPacket.ID, SetCosmeticsS2CPacket.write(new FriendlyByteBuf(Unpooled.buffer()), context.getPlayer().getUUID(), equipped));
            });
        }
    }
}
