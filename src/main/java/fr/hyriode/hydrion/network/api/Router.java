package fr.hyriode.hydrion.network.api;

import com.google.common.io.ByteStreams;
import fr.hyriode.hydrion.network.api.request.IRequestHandler;
import fr.hyriode.hydrion.network.api.request.Request;
import fr.hyriode.hydrion.network.api.util.URIUtil;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 12:50
 */
public class Router {

    private byte[] favicon;

    private final Map<String, IRequestHandler> requestHandlers;

    private final IRequestHandler defaultHandler;
    private final String defaultPath;

    public Router(String defaultPath, IRequestHandler defaultHandler) {
        this.defaultPath = URIUtil.normalize(defaultPath);
        this.defaultHandler = defaultHandler;
        this.requestHandlers = new HashMap<>();
    }

    public Router(String defaultPath) {
        this(defaultPath, null);
    }

    public Router addHandler(String path, IRequestHandler requestHandler) {
        path = URIUtil.normalize(path);

        System.out.println("Adding handler on '" + path + "'.");

        this.requestHandlers.put(path, requestHandler);

        return this;
    }

    public Router setFavicon(String resourcePath) {
        try {
            final InputStream inputStream = Router.class.getResourceAsStream(resourcePath);

            if (inputStream != null) {
                this.favicon = ByteStreams.toByteArray(inputStream);
            } else {
                System.err.println("Cannot get favicon from resource path provided!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void handle(Context context, Request request) {
        final String path = URIUtil.normalize(URIUtil.getURIPrefix(request.getUri()));

        if (path.equals("/favicon.ico")) {
            context.sendResponse(this.favicon, "image/png", HttpResponseStatus.OK.code());
            return;
        }

        if (!path.equals("/")) {
            final IRequestHandler requestHandler = this.requestHandlers.get(path);

            if (requestHandler != null) {
                requestHandler.handle(context, request);
            }
        } else {
            this.defaultHandler.handle(context, request);
        }
    }

    public String getDefaultPath() {
        return this.defaultPath;
    }

    public Map<String, IRequestHandler> getRequestHandlers() {
        return this.requestHandlers;
    }

}
