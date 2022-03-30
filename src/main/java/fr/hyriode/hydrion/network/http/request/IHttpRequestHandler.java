package fr.hyriode.hydrion.network.http.request;

import fr.hyriode.hydrion.network.http.HttpContext;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:24
 */
@FunctionalInterface
public interface IHttpRequestHandler {

    void onRequest(HttpRequest request, HttpContext ctx);

}
