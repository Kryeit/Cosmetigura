package org.figuramc.figura.cosmetics.network;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.figuramc.figura.cosmetics.CosmeticStorage;
import org.figuramc.figura.cosmetics.ServerSupplier;

import java.util.List;
import java.util.UUID;

public class FiguraNetworkManager {
    public static void init(boolean server) {
        if (server) {
            NetworkManager.registerReceiver(NetworkManager.Side.C2S, RequestCosmeticDataC2SPacket.ID, new RequestCosmeticDataC2SPacket());
            NetworkManager.registerReceiver(NetworkManager.Side.C2S, SelectCosmeticsC2SPacket.ID, new SelectCosmeticsC2SPacket());

            PlayerEvent.PLAYER_JOIN.register(player -> {
                UUID playerUUID = player.getUUID();
                long[] equippedCosmetics = CosmeticStorage.getEquippedCosmetics(playerUUID);
                List<ServerPlayer> players = ServerSupplier.serverInstance.getPlayerList().getPlayers();
                // TODO wait until player is in world
                NetworkManager.sendToPlayers(players, SetCosmeticsS2CPacket.ID, SetCosmeticsS2CPacket.write(new FriendlyByteBuf(Unpooled.buffer()), playerUUID, equippedCosmetics));
            });
        } else {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, CosmeticDataS2CPacket.ID, new CosmeticDataS2CPacket());
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, SetCosmeticsS2CPacket.ID, new SetCosmeticsS2CPacket());
        }
    }
}
