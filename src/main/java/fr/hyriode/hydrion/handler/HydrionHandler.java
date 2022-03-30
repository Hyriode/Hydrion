package fr.hyriode.hydrion.handler;

import fr.hyriode.hydrion.network.http.HttpContext;
import fr.hyriode.hydrion.network.http.request.HttpRequest;
import fr.hyriode.hydrion.network.http.request.IHttpRequestHandler;
import fr.hyriode.hydrion.response.HydrionResponse;
import fr.hyriode.hydrion.response.error.HydrionError;
import fr.hyriode.hydrion.response.error.HydrionHeaderError;
import fr.hyriode.hydrion.response.error.HydrionServerError;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:30
 */
public abstract class HydrionHandler implements IHttpRequestHandler {

    private static final String API_KEY_HEADER_KEY = "API-Key";

    @Override
    public void onRequest(HttpRequest request, HttpContext ctx) {
        // TODO API Key system
        /*if (!request.getHeaders().contains(API_KEY_HEADER_KEY)) {
            ctx.text(new HydrionHeaderError(API_KEY_HEADER_KEY).toJson());
            return;
        }

        final String apiKey = request.getHeaders().get(API_KEY_HEADER_KEY);

        if (Verify key) {
            ctx.text(new HydrionError("Invalid API key").toJson());
            return;
        }*/

        final HydrionResponse response = this.handle(request, ctx);

        if (response == null) {
            ctx.text(new HydrionServerError().toJson(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
            return;
        }

        ctx.text(response.toJson());
    }

    public abstract HydrionResponse handle(HttpRequest request, HttpContext ctx);

}
