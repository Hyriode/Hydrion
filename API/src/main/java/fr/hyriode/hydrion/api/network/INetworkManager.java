package fr.hyriode.hydrion.api.network;

import fr.hyriode.hydrion.api.http.IHttpRouter;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 19:04
 */
public interface INetworkManager {

    IHttpRouter newRouter(String path);

    IHttpRouter getRouter(String path);

}
