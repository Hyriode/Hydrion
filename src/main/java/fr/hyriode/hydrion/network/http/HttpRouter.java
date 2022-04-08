package fr.hyriode.hydrion.network.http;

import com.google.common.io.ByteStreams;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.api.http.HttpContext;
import fr.hyriode.hydrion.api.http.request.HttpRequest;
import fr.hyriode.hydrion.api.http.request.IHttpRequestHandler;
import fr.hyriode.hydrion.api.response.error.EndpointError;
import fr.hyriode.hydrion.util.URIUtil;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:27
 */
public class HttpRouter {

    private byte[] favicon;

    private final Map<String, IHttpRequestHandler> handlers;

    public HttpRouter() {
        this.handlers = new HashMap<>();
    }

    public void handleRequest(HttpRequest request, HttpContext context) {
        final String path = URIUtil.normalize(URIUtil.getURIPrefix(request.getUri()));
        final IHttpRequestHandler handler = this.handlers.get(path.toLowerCase(Locale.ROOT));

        if (this.favicon != null && path.equalsIgnoreCase("/favicon")) {
            context.sendResponse(this.favicon, "image/png", HttpResponseStatus.OK);
            return;
        }

        if (handler == null) {
            context.text(new EndpointError().toJson(), HttpResponseStatus.NOT_FOUND);
            return;
        }

        handler.onRequest(request, context);
    }

    public HttpRouter setFavicon(String path) {
        try {
            final InputStream inputStream = Hydrion.class.getResourceAsStream(path);

            if (inputStream != null) {
                this.favicon = ByteStreams.toByteArray(inputStream);
            } else {
                System.err.println("Can't load favicon from '" + path + "' path!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpRouter addHandler(String path, IHttpRequestHandler handler) {
        this.handlers.put(path.toLowerCase(Locale.ROOT), handler);
        return this;
    }

}
