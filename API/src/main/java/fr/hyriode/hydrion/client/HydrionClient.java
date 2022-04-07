package fr.hyriode.hydrion.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.hyriode.hydrion.client.exception.InvalidCodeException;
import fr.hyriode.hydrion.client.exception.InvalidRequestException;
import fr.hyriode.hydrion.client.http.HttpClient;
import fr.hyriode.hydrion.client.http.HttpParameters;
import fr.hyriode.hydrion.client.http.HttpResponse;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 20:48
 */
public class HydrionClient {

    private static final String PLAYER_ENDPOINT = "player";
    private static final String RESOURCES_ENDPOINT = "resources/";
    private static final String GAME_ENDPOINT = RESOURCES_ENDPOINT + "game";
    private static final String GAMES_ENDPOINT = RESOURCES_ENDPOINT + "games";
    private static final String FRIENDS_ENDPOINT = "friends";
    private static final String NETWORK_ENDPOINT = "network";

    private static final String UUID_KEY = "uuid";
    private static final String NAME_KEY = "name";

    private static final String BASE_URL = "http://localhost:8080/";
    private static final Gson GSON = new Gson();

    private final HttpClient httpClient;

    public HydrionClient(UUID apiKey) {
        this.httpClient = new HttpClient(apiKey);
    }

    public CompletableFuture<HydrionResponse> getPlayer(UUID playerId) {
        return this.get("player", PLAYER_ENDPOINT, HttpParameters.create().add(UUID_KEY, playerId.toString()));
    }

    public CompletableFuture<HydrionResponse> setPlayer(UUID playerId, String player) {
        return this.post(PLAYER_ENDPOINT, player, HttpParameters.create().add(UUID_KEY, playerId.toString()));
    }

    public CompletableFuture<HydrionResponse> getFriends(UUID playerId) {
        return this.get("friends", FRIENDS_ENDPOINT, HttpParameters.create().add(UUID_KEY, playerId.toString()));
    }

    public CompletableFuture<HydrionResponse> setFriends(UUID playerId, String friends) {
        return this.post(FRIENDS_ENDPOINT, friends, HttpParameters.create().add(UUID_KEY, playerId.toString()));
    }

    public CompletableFuture<HydrionResponse> getGames() {
        return this.get("games", GAMES_ENDPOINT);
    }

    public CompletableFuture<HydrionResponse> getGame(String name) {
        return this.get("game", GAME_ENDPOINT, HttpParameters.create().add(NAME_KEY, name));
    }

    public CompletableFuture<HydrionResponse> addGame(String name, String game) {
        return this.post(GAME_ENDPOINT, game, HttpParameters.create().add(NAME_KEY, name));
    }

    public CompletableFuture<HydrionResponse> removeGame(String name) {
        return this.delete(GAME_ENDPOINT, HttpParameters.create().add(NAME_KEY, name));
    }

    public CompletableFuture<HydrionResponse> getNetwork() {
        return this.get("network", NETWORK_ENDPOINT);
    }

    public CompletableFuture<HydrionResponse> setNetwork(String network) {
        return this.post(NETWORK_ENDPOINT, network);
    }

    private String createUrl(String endpoint, HttpParameters parameters) {
        String url = BASE_URL + endpoint;

        if (parameters != null) {
            url += parameters.asQueryString();
        }

        return url;
    }

    private CompletableFuture<HydrionResponse> get(String contentName, String endpoint, HttpParameters parameters) {
        return this.httpClient.get(this.createUrl(endpoint, parameters))
                .thenApply(this::checkHttpResponse)
                .thenApply(response -> this.createResponse(response, contentName));
    }

    private CompletableFuture<HydrionResponse> get(String contentName, String endpoint) {
        return this.get(contentName, endpoint, null);
    }

    private CompletableFuture<HydrionResponse> post(String endpoint, String content, HttpParameters parameters) {
        return this.httpClient.post(this.createUrl(endpoint, parameters), content)
                .thenApply(this::checkHttpResponse)
                .thenApply(response -> this.createResponse(response, "message"));
    }

    private CompletableFuture<HydrionResponse> post(String endpoint, String content) {
        return this.post(endpoint, content, null);
    }

    private CompletableFuture<HydrionResponse> delete(String endpoint, HttpParameters parameters) {
        return this.httpClient.delete(this.createUrl(endpoint, parameters))
                .thenApply(this::checkHttpResponse)
                .thenApply(response -> this.createResponse(response, "message"));
    }

    private CompletableFuture<HydrionResponse> delete(String endpoint) {
        return this.delete(endpoint, null);
    }

    private HttpResponse checkHttpResponse(HttpResponse response) {
        if (response.getStatusCode() == 200) {
            return response;
        }

        String cause;
        try {
            cause = GSON.fromJson(response.getBody(), JsonObject.class)
                    .get("cause")
                    .getAsString();
        } catch (JsonSyntaxException ignored) {
            cause = "Unknown cause (response body is not a json)";
        }

        throw new InvalidCodeException(response.getStatusCode(), cause);
    }

    private HydrionResponse createResponse(HttpResponse httpResponse, String contentName) {
        final JsonObject jsonObject = GSON.fromJson(httpResponse.getBody(), JsonObject.class);
        final JsonElement contentElement = jsonObject.get(contentName);
        final String content = !contentElement.isJsonNull() ? contentElement.getAsString() : null;
        final HydrionResponse response = new HydrionResponse(jsonObject.get("success").getAsBoolean(), content);

        if (!response.isSuccess()) {
            throw new InvalidRequestException(response.getContent());
        }

        return response;
    }

}
