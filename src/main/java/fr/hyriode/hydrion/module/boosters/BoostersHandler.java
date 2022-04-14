package fr.hyriode.hydrion.module.boosters;

import fr.hyriode.hydrion.api.handler.HydrionHandler;
import fr.hyriode.hydrion.api.response.HydrionResponse;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:38
 */
public class BoostersHandler extends HydrionHandler {

    private final BoostersModule boostersModule;

    public BoostersHandler(BoostersModule boostersModule) {
        this.boostersModule = boostersModule;

        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> new HydrionResponse(true, new BoostersObject(this.boostersModule.getBoosters())));
    }

}
