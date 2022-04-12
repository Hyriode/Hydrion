package fr.hyriode.hydrion.api.http.request;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:10
 */
public class HttpRequest {

    private final Map<Class<?>, Map<String, Object>> parameterObjects;

    private final String uri;
    private final HttpMethod method;
    private final HttpHeaders headers;
    private final ByteBuf content;

    private final List<HttpRequestParameter> parameters;

    public HttpRequest(FullHttpRequest request, List<HttpRequestParameter> parameters) {
        this.parameters = parameters;
        this.uri = request.uri();
        this.method = request.method();
        this.headers = request.headers();
        this.content = request.content();
        this.parameterObjects = new HashMap<>();
    }

    public String getUri() {
        return this.uri;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public boolean hasParameter(String key) {
        return this.getParameter(key) != null;
    }

    public HttpRequestParameter getParameter(String key) {
        for (HttpRequestParameter parameter : this.parameters) {
            if (parameter.getKey().equalsIgnoreCase(key)) {
                return parameter;
            }
        }
        return null;
    }


    public <T> T getParameter(String key, Class<T> clazz) {
        final Map<String, Object> objects = this.parameterObjects.get(clazz);

        return objects == null ? null : clazz.cast(objects.get(key));
    }

    public List<HttpRequestParameter> getParameters() {
        return this.parameters;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public ByteBuf getContent() {
        return this.content;
    }

    public Map<Class<?>, Map<String, Object>> getParameterObjects() {
        return this.parameterObjects;
    }

}
