package fr.hyriode.hydrion.client.http;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 20:31
 */
public class HttpClient {

    private static final String API_KEY_HEADER = "API-Key";
    private static final String APPLICATION_JSON = "application/json";

    private final UUID apiKey;
    private final ExecutorService executorService;
    private final org.apache.http.client.HttpClient httpClient;

    public HttpClient(UUID apiKey) {
        this.apiKey = apiKey;
        this.executorService = Executors.newCachedThreadPool();
        this.httpClient = HttpClientBuilder.create().
                setUserAgent("Hyriode Hydrion/1.0")
                .build();
    }

    public CompletableFuture<HttpResponse> get(String url) {
        return CompletableFuture.supplyAsync(() -> this.executeRequest(this.createRequest(new HttpGet(url))), this.executorService);
    }

    public CompletableFuture<HttpResponse> post(String url, String content) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final HttpPost request = this.createRequest(new HttpPost(url));

                request.addHeader(HttpHeaders.ACCEPT, APPLICATION_JSON);
                request.addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON + "; charset=UTF-8");
                request.setEntity(new StringEntity(content));

                return this.executeRequest(request);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }, this.executorService);
    }

    public CompletableFuture<HttpResponse> delete(String url) {
        return CompletableFuture.supplyAsync(() -> this.executeRequest(this.createRequest(new HttpDelete(url))), this.executorService);
    }

    private <T extends HttpUriRequest> T createRequest(T request) {
        request.addHeader(API_KEY_HEADER, this.apiKey.toString());

        return request;
    }

    private HttpResponse executeRequest(HttpUriRequest request) {
        try {
            final org.apache.http.HttpResponse response = this.httpClient.execute(request);

            return new HttpResponse(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
