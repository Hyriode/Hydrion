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
import fr.hyriode.hydrion.client.module.*;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 20:48
 */
public class HydrionClient {

    public static final Gson GSON = new Gson();

    private final Map<String, ClientModule> modules;

    private final PlayerModule playerModule;
    private final FriendsModule friendsModule;
    private final ResourcesModule resourcesModule;
    private final NetworkModule networkModule;

    private final String baseUrl;
    private final HttpClient httpClient;

    public HydrionClient(String baseUrl, UUID apiKey) {
        this.baseUrl = baseUrl;
        this.httpClient = new HttpClient(apiKey);
        this.modules = new HashMap<>();

        this.playerModule = this.addModule("player", new PlayerModule(this));
        this.friendsModule = this.addModule("friends", new FriendsModule(this));
        this.resourcesModule = this.addModule("resources", new ResourcesModule(this));
        this.networkModule = this.addModule("network", new NetworkModule(this));
    }

    public PlayerModule getPlayerModule() {
        return this.playerModule;
    }

    public FriendsModule getFriendsModule() {
        return this.friendsModule;
    }

    public ResourcesModule getResourcesModule() {
        return this.resourcesModule;
    }

    public NetworkModule getNetworkModule() {
        return this.networkModule;
    }

    public <T extends ClientModule> T addModule(String name, T module) {
        this.modules.put(name, module);
        return module;
    }

    public <T extends ClientModule> T getModule(Class<T> clazz) {
        for (ClientModule module : this.modules.values()) {
            if (module.getClass() == clazz) {
                return clazz.cast(module);
            }
        }
        return null;
    }

    public ClientModule getModule(String name) {
        return this.modules.get(name);
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

}
