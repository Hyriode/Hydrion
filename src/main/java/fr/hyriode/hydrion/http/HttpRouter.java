package fr.hyriode.hydrion.http;

import fr.hyriode.hydrion.api.http.HttpContext;
import fr.hyriode.hydrion.api.http.IHttpRouter;
import fr.hyriode.hydrion.api.http.request.HttpRequest;
import fr.hyriode.hydrion.api.http.request.IHttpRequestHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AstFaster
 * on 19/09/2022 at 20:54
 */
public class HttpRouter implements IHttpRouter {

    private final Map<String, HttpRouter> routers;
    private final Map<HttpMethod, Map<String, IHttpRequestHandler>> handlers;

    private final String path;

    public HttpRouter(String path) {
        this.path = path;
        this.routers = new HashMap<>();
        this.handlers = new HashMap<>();
    }

    public void dispatch(String[] path, HttpRequest request, HttpContext context) {
        if (path.length == 0) {
            final Map<String, IHttpRequestHandler> handlers = this.handlers.get(request.method());

            if (handlers != null) {
                final IHttpRequestHandler handler = handlers.get("/");

                if (handler != null) {
                    handler.onRequest(request, context);
                    return;
                }
            }

            context.error("Invalid endpoint", HttpResponseStatus.NOT_FOUND);
            return;
        }

        final HttpRouter router = this.routers.get(path[0]);

        if (router == null) {
            final Map<String, IHttpRequestHandler> handlers = this.handlers.get(request.method());

            if (handlers != null) {
                final IHttpRequestHandler handler = handlers.get(String.join("", path));

                if (handler != null) {
                    handler.onRequest(request, context);
                    return;
                }
            }

            context.error("Invalid endpoint!", HttpResponseStatus.NOT_FOUND);
            return;
        }

        router.dispatch(Arrays.copyOfRange(path, 1, path.length), request, context);
    }

    @Override
    public IHttpRouter subRouter(String path) {
        final HttpRouter router = new HttpRouter(path);

        this.routers.put(path.toLowerCase(), router);

        return router;
    }

    @Override
    public void get(String path, IHttpRequestHandler handler) {
        final Map<String, IHttpRequestHandler> handlers = this.handlers.getOrDefault(HttpMethod.GET, new HashMap<>());

        handlers.put(path, handler);

        this.handlers.put(HttpMethod.GET, handlers);
    }

    @Override
    public void post(String path, IHttpRequestHandler handler) {
        final Map<String, IHttpRequestHandler> handlers = this.handlers.getOrDefault(HttpMethod.POST, new HashMap<>());

        handlers.put(path, handler);

        this.handlers.put(HttpMethod.POST, handlers);
    }

    @Override
    public String getPath() {
        return this.path;
    }

}
