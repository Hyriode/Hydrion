package fr.hyriode.hydrion.client.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.hyriode.hydrion.client.HydrionClient;
import fr.hyriode.hydrion.client.exception.InvalidCodeException;
import fr.hyriode.hydrion.client.exception.InvalidRequestException;
import fr.hyriode.hydrion.client.http.HttpClient;
import fr.hyriode.hydrion.client.http.HttpParameters;
import fr.hyriode.hydrion.client.http.HttpResponse;
import fr.hyriode.hydrion.client.response.HydrionResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 09/04/2022 at 15:27
 */
public abstract class ClientModule {

    protected static final String UUID_KEY = "uuid";
    protected static final String NAME_KEY = "name";

    private final HydrionClient client;
    private final HttpClient httpClient;

    public ClientModule(HydrionClient client) {
        this.client = client;
        this.httpClient = this.client.getHttpClient();
    }

    private String createUrl(String endpoint, HttpParameters parameters) {
        String url = this.client.getBaseUrl() + endpoint;

        if (parameters != null) {
            url += parameters.asQueryString();
        }

        return url;
    }

    protected CompletableFuture<HydrionResponse> get(String contentName, String endpoint, HttpParameters parameters) {
        return this.httpClient.get(this.createUrl(endpoint, parameters))
                .thenApply(this::checkHttpResponse)
                .thenApply(response -> this.createResponse(response, contentName));
    }

    protected CompletableFuture<HydrionResponse> get(String contentName, String endpoint) {
        return this.get(contentName, endpoint, null);
    }

    protected CompletableFuture<HydrionResponse> post(String endpoint, String content, HttpParameters parameters) {
        return this.httpClient.post(this.createUrl(endpoint, parameters), content)
                .thenApply(this::checkHttpResponse)
                .thenApply(response -> this.createResponse(response, "message"));
    }

    protected CompletableFuture<HydrionResponse> post(String endpoint, String content) {
        return this.post(endpoint, content, null);
    }

    protected CompletableFuture<HydrionResponse> delete(String endpoint, HttpParameters parameters) {
        return this.httpClient.delete(this.createUrl(endpoint, parameters))
                .thenApply(this::checkHttpResponse)
                .thenApply(response -> this.createResponse(response, "message"));
    }

    protected CompletableFuture<HydrionResponse> delete(String endpoint) {
        return this.delete(endpoint, null);
    }

    private HttpResponse checkHttpResponse(HttpResponse response) {
        if (response.getStatusCode() == 200) {
            return response;
        }

        String cause;
        try {
            cause = HydrionClient.GSON.fromJson(response.getBody(), JsonObject.class)
                    .get("cause")
                    .getAsString();
        } catch (JsonSyntaxException ignored) {
            cause = "Unknown cause (response body is not a json)";
        }

        throw new InvalidCodeException(response.getStatusCode(), cause);
    }

    private HydrionResponse createResponse(HttpResponse httpResponse, String contentName) {
        final JsonObject jsonObject = HydrionClient.GSON.fromJson(httpResponse.getBody(), JsonObject.class);
        final JsonElement contentElement = jsonObject.get(contentName);
        final String content = !contentElement.isJsonNull() ? contentElement.getAsString() : null;
        final HydrionResponse response = new HydrionResponse(jsonObject.get("success").getAsBoolean(), content);

        if (!response.isSuccess()) {
            throw new InvalidRequestException(response.getContent());
        }

        return response;
    }

}
