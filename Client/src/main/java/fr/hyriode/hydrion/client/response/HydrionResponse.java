package fr.hyriode.hydrion.client.response;

import com.google.gson.JsonElement;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 20:50
 */
public class HydrionResponse {

    private final boolean success;
    private final JsonElement content;

    public HydrionResponse(boolean success, JsonElement content) {
        this.success = success;
        this.content = content;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public JsonElement getContent() {
        return this.content;
    }

}
