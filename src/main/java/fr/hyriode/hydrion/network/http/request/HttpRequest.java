package fr.hyriode.hydrion.network.http.request;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:10
 */
public class HttpRequest {

    private final String uri;
    private final HttpMethod method;
    private final HttpHeaders headers;

    private final List<HttpRequestParameter> parameters;

    public HttpRequest(io.netty.handler.codec.http.HttpRequest request, List<HttpRequestParameter> parameters) {
        this.parameters = parameters;
        this.uri = request.uri();
        this.method = request.method();
        this.headers = request.headers();
    }

    public String getUri() {
        return this.uri;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public HttpRequestParameter getParameter(String key) {
        for (HttpRequestParameter parameter : this.parameters) {
            if (parameter.getKey().equalsIgnoreCase(key)) {
                return parameter;
            }
        }
        return null;
    }

    public List<HttpRequestParameter> getParameters() {
        return this.parameters;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

}
