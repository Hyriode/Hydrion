package fr.hyriode.hydrion.routes;

import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.http.IHttpRouter;

/**
 * Created by AstFaster
 * on 20/09/2022 at 20:57
 */
public abstract class Routes {

    protected IHttpRouter mainRouter;

    public Routes() {
        this.mainRouter = HydrionAPI.get().getNetworkManager().getRouter("/");
    }

}
