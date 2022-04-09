package fr.hyriode.hydrion.client.module;

import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.http.HttpParameters;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 09/04/2022 at 15:45
 */
public class PlayerModule extends ClientModule {

    private static final String PLAYER_ENDPOINT = "player";

    public PlayerModule(HydrionClient client) {
        super(client);
    }

    public CompletableFuture<HydrionResponse> getPlayer(UUID playerId) {
        return this.get("player", PLAYER_ENDPOINT, HttpParameters.create().add(UUID_KEY, playerId.toString()));
    }

    public CompletableFuture<HydrionResponse> setPlayer(UUID playerId, String player) {
        return this.post(PLAYER_ENDPOINT, player, HttpParameters.create().add(UUID_KEY, playerId.toString()));
    }

}
