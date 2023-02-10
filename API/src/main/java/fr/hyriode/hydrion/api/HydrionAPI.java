package fr.hyriode.hydrion.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.hydrion.api.network.INetworkManager;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 18:53
 */
public abstract class HydrionAPI {

    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();

    private static HydrionAPI instance;

    public HydrionAPI() {
        instance = this;

        System.out.println("Registered implementation for API.");
    }

    public abstract UUID getAPIKey();

    public abstract INetworkManager getNetworkManager();

    public static HydrionAPI get() {
        return instance;
    }

}
