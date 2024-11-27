package org.figuramc.figura.cosmetics;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CosmeticBackend {
    private static final Gson gson = new Gson();
    private static final String apiUrl = BackendConfig.apiUrl;
    private static final HttpClient client = HttpClient.newHttpClient();

    public static CosmeticManager.CosmeticData getCosmeticData(long cosmeticId) {


        return switch ((int) cosmeticId) {
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
            default -> throw new IllegalStateException("Unexpected value: " + (int) cosmeticId);
        };
    }

    public static CompletableFuture<long[]> equipCosmetic(UUID player, long cosmeticId) {
        JsonObject body = new JsonObject();
        body.addProperty("cosmetic-id", cosmeticId);

        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl + "/player/" + player + "/equip"))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return sendRequest(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> gson.fromJson(r.body(), EquippedCosmeticsResponse.class).equippedCosmetics());
    }

    public static CompletableFuture<long[]> unequipCosmetic(UUID player, long cosmeticId) {
        JsonObject body = new JsonObject();
        body.addProperty("cosmeticId", cosmeticId);

        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl + "/player/" + player + "/uneqip"))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return sendRequest(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> gson.fromJson(r.body(), EquippedCosmeticsResponse.class).equippedCosmetics());
    }

    public static CompletableFuture<List<WardrobeResponseEntry>> getWardrobe(UUID player) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl + "/player/" + player + "/wardrobe")).build();

        return sendRequest(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> gson.fromJson(r.body(), new TypeToken<List<WardrobeResponseEntry>>() {
                }.getType()));
    }

    private record EquippedCosmeticsResponse(long[] equippedCosmetics) {
    }

    public record WardrobeResponseEntry(long id, String name, CosmeticManager.CosmeticType type, boolean equipped,
                                        String previewImage) {
    }

    private static String readString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> CompletableFuture<HttpResponse<T>> sendRequest(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        HttpRequest authenticatedRequest = HttpRequest.newBuilder(request, (n, v) -> !n.equals("Authorization"))
                .header("Authorization", "Bearer " + BackendConfig.apiKey)
                .build();

        return client.sendAsync(authenticatedRequest, bodyHandler);
    }
}
