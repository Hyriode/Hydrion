package fr.hyriode.hydrion.api.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.hyriode.hydrion.api.HydrionAPI;

/**
 * Created by AstFaster
 * on 10/02/2023 at 09:11
 */
public class HttpResponse {

    private final JsonObject jsonObject;

    public HttpResponse(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HttpResponse() {
        this(new JsonObject());
    }

    public HttpResponse add(String key, Object value) {
        this.jsonObject.add(key, HydrionAPI.GSON.toJsonTree(value));
        return this;
    }

    public HttpResponse add(String key, JsonElement jsonElement) {
        this.jsonObject.add(key, jsonElement);
        return this;
    }

    public HttpResponse add(String key, String value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public HttpResponse add(String key, boolean value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public HttpResponse add(String key, Number value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public HttpResponse add(String key, Character value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public JsonObject jsonObject() {
        return this.jsonObject;
    }

    @Override
    public String toString() {
        return this.jsonObject.toString();
    }

}
