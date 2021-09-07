package fr.hyriode.hydrion.network.api.request;

import fr.hyriode.hydrion.util.References;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 10:30
 */
public class Request {

    private Map<String, List<String>> allHeaders;

    private final String uri;
    private final HttpMethod method;
    private final HttpHeaders headers;

    private final List<QueryParameter> parameters;

    private final FullHttpRequest request;

    public Request(FullHttpRequest request, List<QueryParameter> parameters) {
        this.request = request;
        this.parameters = parameters;
        this.uri = this.request.uri();
        this.method = this.request.method();
        this.headers = this.request.headers();
    }

    public <T> T getJsonForm(Class<T> clazz) {
        return References.GSON.fromJson(this.request.content().toString(), clazz);
    }

    public String getDataForm() {
        final byte[] bytes = new byte[this.request.content().readableBytes()];

        this.request.content().readBytes(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public String getUri() {
        return this.uri;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getHeader(String name) {
        return this.getHeaders(name).get(0);
    }

    public String getHeader(AsciiString name) {
        return this.getHeader(name.toString());
    }

    public List<String> getHeaders(String name) {
        return this.getAllHeaders().get(name.toLowerCase());
    }

    public Map<String, List<String>> getAllHeaders() {
        if (this.allHeaders != null) {
            this.allHeaders = new HashMap<>();

            for (Map.Entry<String, String> entry : this.headers.entries()) {
                this.allHeaders.put(entry.getKey().toLowerCase(), this.headers.getAll(entry.getKey()));
            }
        }
        return this.allHeaders;
    }

    public HttpHeaders getHttpHeaders() {
        return this.headers;
    }

    public List<QueryParameter> getParameters() {
        return this.parameters;
    }

    public FullHttpRequest getRequest() {
        return this.request;
    }

}
