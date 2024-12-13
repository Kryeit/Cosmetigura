package org.figuramc.figura.cosmetics.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.figura.cosmetics.CosmeticManager;

public class PutWardrobeEntryS2CPacket implements NetworkManager.NetworkReceiver {
    public static final ResourceLocation ID = new ResourceLocation("cosmetigura", "wardrobe_entry");

    public static FriendlyByteBuf write(FriendlyByteBuf buf, long id, String name, CosmeticManager.CosmeticType type, byte[] previewImage) {
        buf.writeLong(id);
        buf.writeUtf(name);
        buf.writeEnum(type);
        buf.writeByteArray(previewImage);
        return buf;
    }

    @Override
    public void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        long id = buf.readLong();
        String name = buf.readUtf();
        CosmeticManager.CosmeticType type = buf.readEnum(CosmeticManager.CosmeticType.class);
        byte[] previewImage = buf.readByteArray();

        CosmeticManager.putWardrobeEntry(new CosmeticManager.WardrobeEntry(id, name, type), previewImage);
    }
}
