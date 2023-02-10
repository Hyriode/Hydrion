package fr.hyriode.hydrion.api.http.request;

import com.google.gson.JsonObject;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.hydrion.api.http.HttpContext;
import fr.hyriode.hydrion.api.util.URIUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:10
 */
public class HttpRequest {

    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";

    private final HttpContext ctx;

    private final String uri;
    private final HttpMethod method;
    private final HttpHeaders headers;
    private final ByteBuf content;

    private final List<HttpRequestParameter> parameters;

    public HttpRequest(FullHttpRequest request, HttpContext ctx, List<HttpRequestParameter> parameters) {
        this.ctx = ctx;
        this.parameters = parameters;
        this.uri = URIUtil.normalize(request.uri());
        this.method = request.method();
        this.headers = request.headers();
        this.content = request.content();
    }

    public String uri() {
        return this.uri;
    }

    public HttpMethod method() {
        return this.method;
    }

    public boolean hasParameter(String key) {
        return this.parameter(key) != null;
    }

    public HttpRequestParameter parameter(String key) {
        for (HttpRequestParameter parameter : this.parameters) {
            if (parameter.getKey().equalsIgnoreCase(key)) {
                return parameter;
            }
        }
        return null;
    }

    public List<HttpRequestParameter> parameters() {
        return this.parameters;
    }

    public HttpHeaders headers() {
        return this.headers;
    }

    public ByteBuf content() {
        return this.content;
    }

    public JsonObject jsonBody() {
        final String contentType = this.headers.get(CONTENT_TYPE_HEADER_KEY);

        if (contentType == null || !contentType.contains("application/json")) {
            this.ctx.error("Bad Content-Type", HttpResponseStatus.BAD_REQUEST);
            return null;
        }

        final String json = this.content.toString(StandardCharsets.UTF_8);

        try {
            return HyriAPI.GSON.fromJson(json, JsonObject.class);
        } catch (Exception e) {
            this.ctx.error("Bad Json", HttpResponseStatus.BAD_REQUEST);
            return null;
        }
    }

}
