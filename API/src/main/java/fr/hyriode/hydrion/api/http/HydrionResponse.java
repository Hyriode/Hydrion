package fr.hyriode.hydrion.api.http;

/**
 * Created by AstFaster
 * on 20/09/2022 at 20:30
 */
public class HydrionResponse {

    private final boolean success;
    private final Object response;

    public HydrionResponse(boolean success, Object response) {
        this.success = success;
        this.response = response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Object getResponse() {
        return this.response;
    }

}
