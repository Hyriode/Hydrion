package fr.hyriode.hydrion.network.api.request;

import fr.hyriode.hydrion.network.api.Context;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 08:53
 */
@FunctionalInterface
public interface IRequestHandler {

    void handle(Context ctx, Request request);

}
