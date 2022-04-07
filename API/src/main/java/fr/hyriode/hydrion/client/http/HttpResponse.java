package fr.hyriode.hydrion.client.http;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 20:34
 */
public class HttpResponse {

    private final int statusCode;
    private final String body;

    public HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getBody() {
        return this.body;
    }

}
