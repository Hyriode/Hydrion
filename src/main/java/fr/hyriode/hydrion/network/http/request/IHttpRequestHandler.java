package fr.hyriode.hydrion.network.http.request;

import fr.hyriode.hydrion.network.http.HttpResponse;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:24
 */
@FunctionalInterface
public interface IHttpRequestHandler {

    void onRequest(HttpRequest request, HttpResponse context);

}
