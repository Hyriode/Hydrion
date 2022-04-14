package fr.hyriode.hydrion.client.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.http.HttpParameters;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 14/04/2022 at 13:32
 */
public class UUIDModule extends ClientModule {

    private static final String ENDPOINT = "uuid";

    public UUIDModule(HydrionClient client) {
        super(client);
    }

    public CompletableFuture<UUID> getUUID(String name) {
        return this.get("uuid", ENDPOINT, HttpParameters.create().add(NAME_KEY, name)).thenApply(response -> {
            final JsonElement content = response.getContent();

            if (!content.isJsonNull()) {
                return UUID.fromString(content.getAsJsonObject().get("value").getAsString());
            }
            return null;
        });
    }

    public CompletableFuture<HydrionResponse> setUUID(String name, UUID uuid) {
        final JsonObject object = new JsonObject();

        object.addProperty("value", uuid.toString());

        return this.post(ENDPOINT, object.toString(), HttpParameters.create().add(NAME_KEY, name));
    }

}
