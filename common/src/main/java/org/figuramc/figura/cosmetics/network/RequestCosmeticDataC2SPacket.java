package org.figuramc.figura.cosmetics.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.figuramc.figura.cosmetics.CosmeticBackend;

public class RequestCosmeticDataC2SPacket implements NetworkManager.NetworkReceiver {
    public static final ResourceLocation ID = new ResourceLocation("figura", "request_cosmetic_data");

    public static FriendlyByteBuf write(FriendlyByteBuf buf, long id) {
        buf.writeLong(id);
        return buf;
    }

    @Override
    public void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        long id = buf.readLong();

        CosmeticBackend.getCosmeticData(id).thenAccept(data ->
                NetworkManager.sendToPlayer((ServerPlayer) context.getPlayer(), CosmeticDataS2CPacket.ID, CosmeticDataS2CPacket.write(new FriendlyByteBuf(Unpooled.buffer()), id, data)));
    }
}
