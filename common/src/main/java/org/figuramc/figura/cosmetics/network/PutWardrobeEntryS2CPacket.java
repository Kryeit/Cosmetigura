package org.figuramc.figura.cosmetics.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.figura.cosmetics.CosmeticManager;

public class PutWardrobeEntryS2CPacket implements NetworkManager.NetworkReceiver {
    public static final ResourceLocation ID = new ResourceLocation("figura", "wardrobe_entry");

    public static FriendlyByteBuf write(FriendlyByteBuf buf, long id, CosmeticManager.CosmeticData data) {
        buf.writeLong(id);
        buf.writeByteArray(data.model().getBytes());
        buf.writeByteArray(data.script().getBytes());
        buf.writeEnum(data.type());
        return buf;
    }

    @Override
    public void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        long id = buf.readLong();
        String model = new String(buf.readByteArray());
        String script = new String(buf.readByteArray());
        CosmeticManager.CosmeticType type = buf.readEnum(CosmeticManager.CosmeticType.class);

        CosmeticManager.cacheData(id, new CosmeticManager.CosmeticData(model, script, type));
    }
}
