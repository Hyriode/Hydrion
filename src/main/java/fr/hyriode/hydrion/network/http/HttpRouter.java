package fr.hyriode.hydrion.network.http;

import fr.hyriode.hydrion.network.http.request.HttpRequest;
import fr.hyriode.hydrion.network.http.request.IHttpRequestHandler;
import fr.hyriode.hydrion.util.URIUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:27
 */
public class HttpRouter {

    private final Map<String, IHttpRequestHandler> handlers;

    public HttpRouter() {
        this.handlers = new HashMap<>();
    }

    public void handleRequest(HttpRequest request, HttpContext context) {
        final String path = URIUtil.normalize(URIUtil.getURIPrefix(request.getUri()));

        for (Map.Entry<String, IHttpRequestHandler> entry : this.handlers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(path)) {
                entry.getValue().onRequest(request, context);
            }
        }
    }

    public HttpRouter addHandler(String path, IHttpRequestHandler handler) {
        this.handlers.put(path, handler);
        return this;
    }

}
