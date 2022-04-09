package fr.hyriode.hydrion.client.module;

import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 09/04/2022 at 15:50
 */
public class NetworkModule extends ClientModule {

    private static final String NETWORK_ENDPOINT = "network";

    public NetworkModule(HydrionClient client) {
        super(client);
    }

    public CompletableFuture<HydrionResponse> getNetwork() {
        return this.get("network", NETWORK_ENDPOINT);
    }

    public CompletableFuture<HydrionResponse> setNetwork(String network) {
        return this.post(NETWORK_ENDPOINT, network);
    }

}
