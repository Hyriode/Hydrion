package fr.hyriode.hydrion.api.http.request;

import fr.hyriode.hydrion.api.http.HttpContext;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:24
 */
@FunctionalInterface
public interface IHttpRequestHandler {

    void onRequest(HttpRequest request, HttpContext ctx);

}
