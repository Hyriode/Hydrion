package fr.hyriode.hydrion.client.response;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 20:50
 */
public class HydrionResponse {

    private final boolean success;
    private final String content;

    public HydrionResponse(boolean success, String content) {
        this.success = success;
        this.content = content;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getContent() {
        return this.content;
    }

}
