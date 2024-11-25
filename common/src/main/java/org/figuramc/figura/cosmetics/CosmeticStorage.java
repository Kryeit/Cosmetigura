package org.figuramc.figura.cosmetics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class CosmeticStorage {
    public static CosmeticManager.CosmeticData getData(long id) {
        return switch ((int) id) {
            case 1 -> new CosmeticManager.CosmeticData(
                    readString(Path.of("figura/avatars/Snowman Skin/player.bbmodel")),
                    readString(Path.of("figura/avatars/Snowman Skin/script.lua")),
                    CosmeticManager.CosmeticType.BODY
            );
            case 2 -> new CosmeticManager.CosmeticData(
                    readString(Path.of("figura/avatars/Snowman Skin/christmas_hat.bbmodel")),
                    "",
                    CosmeticManager.CosmeticType.HAT
            );
            default -> throw new IllegalStateException("Unexpected value: " + (int) id);
        };
    }

    public static long[] getEquippedCosmetics(UUID player) {
        return new long[]{1};
    }

    private static String readString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
