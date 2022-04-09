package fr.hyriode.hydrion.client.module;

import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.http.HttpParameters;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 09/04/2022 at 15:47
 */
public class ResourcesModule extends ClientModule {

    private static final String RESOURCES_ENDPOINT = "resources/";
    private static final String GAME_ENDPOINT = RESOURCES_ENDPOINT + "game";
    private static final String GAMES_ENDPOINT = RESOURCES_ENDPOINT + "games";

    public ResourcesModule(HydrionClient client) {
        super(client);
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

}
