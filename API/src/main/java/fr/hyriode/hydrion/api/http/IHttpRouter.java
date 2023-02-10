package fr.hyriode.hydrion.api.http;

import fr.hyriode.hydrion.api.http.request.IHttpRequestHandler;

/**
 * Created by AstFaster
 * on 19/09/2022 at 20:53
 */
public interface IHttpRouter {

    IHttpRouter subRouter(String path);

    void get(String path, IHttpRequestHandler handler);

    void post(String path, IHttpRequestHandler handler);

    String getPath();

}
