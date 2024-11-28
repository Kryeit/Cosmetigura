package org.figuramc.figura.cosmetics.network;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.figuramc.figura.cosmetics.CosmeticBackend;
import org.figuramc.figura.cosmetics.ServerSupplier;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FiguraNetworkManager {
    public static void init(boolean server) {
        if (server) {
            NetworkManager.registerReceiver(NetworkManager.Side.C2S, RequestCosmeticDataC2SPacket.ID, new RequestCosmeticDataC2SPacket());
            NetworkManager.registerReceiver(NetworkManager.Side.C2S, SelectCosmeticsC2SPacket.ID, new SelectCosmeticsC2SPacket());

            PlayerEvent.PLAYER_JOIN.register(player -> {
                UUID playerUUID = player.getUUID();
                CosmeticBackend.getWardrobe(playerUUID).thenAccept(wardrobe -> {
                    LongArrayList equippedCosmetics = new LongArrayList();

                    for (CosmeticBackend.WardrobeResponseEntry entry : wardrobe) {
                        byte[] previewImage = Base64.getDecoder().decode(entry.previewImage());
                        FriendlyByteBuf buf = PutWardrobeEntryS2CPacket.write(new FriendlyByteBuf(Unpooled.buffer()), entry.id(), entry.name(), entry.type(), previewImage);
                        NetworkManager.sendToPlayer(player, PutWardrobeEntryS2CPacket.ID, buf);
                        if (entry.equipped()) equippedCosmetics.add(entry.id());
                    }

                    List<ServerPlayer> players = ServerSupplier.serverInstance.getPlayerList().getPlayers();
                    NetworkManager.sendToPlayers(players, SetCosmeticsS2CPacket.ID, SetCosmeticsS2CPacket.write(new FriendlyByteBuf(Unpooled.buffer()), playerUUID, equippedCosmetics.toLongArray()));

                    CosmeticBackend.getEquippedCosmetics(players.stream().map(Entity::getUUID).toList()).thenAccept(uuidMap -> {
                        for (Map.Entry<UUID, long[]> entry : uuidMap.entrySet()) {
                            NetworkManager.sendToPlayer(player, SetCosmeticsS2CPacket.ID, SetCosmeticsS2CPacket.write(new FriendlyByteBuf(Unpooled.buffer()), entry.getKey(), entry.getValue()));
                        }
                    });
                });
            });
        } else {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, CosmeticDataS2CPacket.ID, new CosmeticDataS2CPacket());
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, SetCosmeticsS2CPacket.ID, new SetCosmeticsS2CPacket());
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, PutWardrobeEntryS2CPacket.ID, new PutWardrobeEntryS2CPacket());
        }
    }
}
