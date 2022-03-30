package fr.hyriode.hydrion.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.hyriode.hydrion.object.HydrionObject;

import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:33
 */
public class HydrionResponse {

    protected final boolean success;
    private final HydrionObject object;

    public HydrionResponse(boolean success, HydrionObject object) {
        this.success = success;
        this.object = object;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public HydrionObject getObject() {
        return this.object;
    }

    public String toJson() {
        final JsonObject result = new JsonObject();
        final JsonElement element = new Gson().toJsonTree(this.object);

        result.addProperty("success", this.success);

        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            result.add(entry.getKey(), entry.getValue());
        }

        return result.toString();
    }

}
