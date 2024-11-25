package org.figuramc.figura.cosmetics.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.figura.cosmetics.CosmeticManager;

import java.util.UUID;

public class SetCosmeticsS2CPacket implements NetworkManager.NetworkReceiver {
    public static final ResourceLocation ID = new ResourceLocation("figura", "set_cosmetics");

    public static FriendlyByteBuf write(FriendlyByteBuf buf, UUID targetPlayer, long[] cosmetics) {
        buf.writeUUID(targetPlayer);
        buf.writeLongArray(cosmetics);
        return buf;
    }

    @Override
    public void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        UUID targetPlayer = buf.readUUID();
        long[] cosmetics = buf.readLongArray();
        CosmeticManager.setEquippedCosmetics(targetPlayer, cosmetics);
    }
}
