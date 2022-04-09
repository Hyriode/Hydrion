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
public class FriendsModule extends ClientModule {

    private static final String FRIENDS_ENDPOINT = "friends";

    public FriendsModule(HydrionClient client) {
        super(client);
    }

    public CompletableFuture<HydrionResponse> getFriends(UUID playerId) {
        return this.get("friends", FRIENDS_ENDPOINT, HttpParameters.create().add(UUID_KEY, playerId.toString()));
    }

    public CompletableFuture<HydrionResponse> setFriends(UUID playerId, String friends) {
        return this.post(FRIENDS_ENDPOINT, friends, HttpParameters.create().add(UUID_KEY, playerId.toString()));
    }

}
