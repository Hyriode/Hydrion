package fr.hyriode.hydrion.client.module;

import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.http.HttpParameters;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 14/04/2022 at 10:12
 */
public class BoostersModule extends ClientModule {

    private static final String BOOSTERS_ENDPOINT = "boosters";
    private static final String BOOSTER_ENDPOINT = "booster";

    public BoostersModule(HydrionClient client) {
        super(client);
    }

    public CompletableFuture<HydrionResponse> getBoosters() {
        return this.get("boosters", BOOSTERS_ENDPOINT);
    }

    public CompletableFuture<HydrionResponse> getBooster(UUID identifier) {
        return this.get("booster", BOOSTER_ENDPOINT, HttpParameters.create().add(UUID_KEY, identifier.toString()));
    }

    public CompletableFuture<HydrionResponse> addBooster(UUID identifier, String booster) {
        return this.post(BOOSTER_ENDPOINT, booster, HttpParameters.create().add(UUID_KEY, identifier.toString()));
    }

    public CompletableFuture<HydrionResponse> removeBooster(UUID identifier) {
        return this.delete(BOOSTER_ENDPOINT, HttpParameters.create().add(UUID_KEY, identifier.toString()));
    }

}
