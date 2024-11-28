package org.figuramc.figura.cosmetics;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CosmeticBackend {
    private static final Gson gson = new Gson();
    private static final String apiUrl = BackendConfig.apiUrl;
    private static final HttpClient client = HttpClient.newHttpClient();

    public static CompletableFuture<CosmeticManager.CosmeticData> getCosmeticData(long cosmeticId) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl + "/cosmetic/" + cosmeticId)).build();

        return sendRequest(request, HttpResponse.BodyHandlers.ofString()).thenApply(r -> gson.fromJson(r.body(), CosmeticManager.CosmeticData.class));
    }

    public static CompletableFuture<long[]> equipCosmetic(UUID player, long cosmeticId) {
        JsonObject body = new JsonObject();
        body.addProperty("cosmeticId", cosmeticId);

        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl + "/player/" + player + "/equip"))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return sendRequest(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> gson.fromJson(r.body(), EquippedCosmeticsResponse.class).equippedCosmetics());
    }

    public static CompletableFuture<long[]> unequipCosmetic(UUID player, long cosmeticId) {
        JsonObject body = new JsonObject();
        body.addProperty("cosmeticId", cosmeticId);

        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl + "/player/" + player + "/unequip"))
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

    public static CompletableFuture<Map<UUID, long[]>> getEquippedCosmetics(List<UUID> players) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl + "/equipped"))
                .method("GET", HttpRequest.BodyPublishers.ofString(gson.toJson(new EquippedCosmeticsBody(players))))
                .build();

        return sendRequest(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> gson.fromJson(r.body(), new TypeToken<Map<UUID, long[]>>() {
                }.getType()));
    }

    private record EquippedCosmeticsResponse(long[] equippedCosmetics) {
    }

    private record EquippedCosmeticsBody(List<UUID> players) {
    }

    public record WardrobeResponseEntry(long id, String name, CosmeticManager.CosmeticType type, boolean equipped,
                                        String previewImage) {
    }

    public static <T> CompletableFuture<HttpResponse<T>> sendRequest(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        HttpRequest authenticatedRequest = HttpRequest.newBuilder(request, (n, v) -> !n.equals("Authorization"))
                .header("Authorization", "Bearer " + BackendConfig.apiKey)
                .build();

        return client.sendAsync(authenticatedRequest, bodyHandler);
    }
}
