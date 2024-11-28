package org.figuramc.figura.cosmetics;

import com.mojang.blaze3d.platform.NativeImage;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.cosmetics.network.RequestCosmeticDataC2SPacket;
import org.figuramc.figura.parsers.BlockbenchModelParser;

import java.io.IOException;
import java.util.*;

public class CosmeticManager {
    private static final Map<CosmeticType, List<WardrobeEntry>> wardrobe = new HashMap<>();
    private static final Map<UUID, long[]> equippedCosmetics = new HashMap<>();
    private static final Map<Long, CosmeticData> cache = new HashMap<>();
    private static final String baseScript = """
            if models.hat then
              local hat = models.hat.Hat
              if not models.body then
                hat:setParentType("Head")
                hat:setPos(0, 24, 0)
              else
                hat:moveTo(models.body.Head)
                hat:setPos(models.body.Head:getPivot())
              end
            end
            """;

    public static void setEquippedCosmetics(UUID player, long... cosmetics) {
        Map<CosmeticType, CosmeticData> map = new HashMap<>();
        for (long id : cosmetics) {
            CosmeticData data = cache.get(id);
            if (data == null) {
                NetworkManager.sendToServer(RequestCosmeticDataC2SPacket.ID, RequestCosmeticDataC2SPacket.write(new FriendlyByteBuf(Unpooled.buffer()), id));
            } else {
                map.put(data.type(), data);
            }
        }
        equipCosmetics(map, player);
        equippedCosmetics.put(player, cosmetics);
    }

    public static long[] getEquippedCosmetics(UUID player) {
        return equippedCosmetics.getOrDefault(player, new long[0]);
    }

    private static void equipCosmetics(Map<CosmeticType, CosmeticData> cosmetics, UUID player) {
        CompoundTag tag = new CompoundTag();

        CompoundTag scripts = new CompoundTag();
        for (CosmeticType value : CosmeticType.values()) {
            CosmeticData cosmetic = cosmetics.get(value);
            if (cosmetic != null && !cosmetic.script().isEmpty()) {
                scripts.put(cosmetic.type().name().toLowerCase(), new ByteArrayTag(cosmetic.script().getBytes()));
            }
        }
        scripts.put("base", new ByteArrayTag(baseScript.getBytes()));
        tag.put("scripts", scripts);

        ListTag models = new ListTag();
        CompoundTag textures = new CompoundTag();
        ListTag animations = new ListTag();
        BlockbenchModelParser modelParser = new BlockbenchModelParser();

        for (CosmeticData cosmetic : cosmetics.values()) {
            String name = String.valueOf(cosmetic.type());
            BlockbenchModelParser.ModelData data;
            try {
                data = modelParser.parseModel(cosmetic.model(), name, "");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            models.add(data.modelNbt());
            animations.addAll(data.animationList());

            CompoundTag dataTag = data.textures();
            if (dataTag.isEmpty())
                continue;

            if (textures.isEmpty()) {
                textures.put("data", new ListTag());
                textures.put("src", new CompoundTag());
            }

            textures.getList("data", Tag.TAG_COMPOUND).addAll(dataTag.getList("data", Tag.TAG_COMPOUND));
            textures.getCompound("src").merge(dataTag.getCompound("src"));
        }
        CompoundTag modelsTag = new CompoundTag();
        modelsTag.put("chld", models);
        modelsTag.putString("name", "models");

        if (!modelsTag.isEmpty()) tag.put("models", modelsTag);
        if (!textures.isEmpty()) tag.put("textures", textures);
        if (!animations.isEmpty()) tag.put("animations", animations);

        AvatarManager.setAvatar(player, tag);
    }

    public record CosmeticData(String model, String script, CosmeticType type) {
    }

    public record WardrobeEntry(long id, String name, CosmeticType type) {
    }

    public static void putWardrobeEntry(WardrobeEntry entry, byte[] previewImage) {
        try {
            NativeImage image = NativeImage.read(previewImage);
            ResourceLocation textureLocation = new ResourceLocation("figura", "thumbnail_" + entry.id());
            Minecraft.getInstance().getTextureManager().register(textureLocation, new DynamicTexture(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        wardrobe.computeIfAbsent(entry.type(), k -> new ArrayList<>()).add(entry);
    }

    public static List<WardrobeEntry> getWardrobe() {
        List<WardrobeEntry> result = new ArrayList<>();
        for (List<WardrobeEntry> list : wardrobe.values()) {
            result.addAll(list);
        }
        return result;
    }

    public static void clearWardrobe() {
        wardrobe.clear();
    }

    public enum CosmeticType {
        BODY,
        HAT;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public static void cacheData(long id, CosmeticData data) {
        CosmeticData previous = cache.put(id, data);
        if (previous == null) {
            equippedCosmetics.forEach((uuid, cosmetics) -> {
                for (long cosmeticId : cosmetics) {
                    if (!cache.containsKey(cosmeticId)) return;
                }
                setEquippedCosmetics(uuid, cosmetics);
            });
        }
    }
}
