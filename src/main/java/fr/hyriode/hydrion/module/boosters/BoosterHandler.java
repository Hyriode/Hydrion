package fr.hyriode.hydrion.module.boosters;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.handler.HydrionHandler;
import fr.hyriode.hydrion.api.handler.parameter.ParameterKeys;
import fr.hyriode.hydrion.api.handler.parameter.StringHandler;
import fr.hyriode.hydrion.api.handler.parameter.UUIDHandler;
import fr.hyriode.hydrion.api.response.HydrionPostRequestResponse;
import fr.hyriode.hydrion.api.response.HydrionResponse;
import fr.hyriode.hydrion.api.response.error.BadJsonError;
import fr.hyriode.hydrion.module.resources.ResourcesModule;
import fr.hyriode.hydrion.module.resources.game.GameObject;
import io.netty.handler.codec.http.HttpMethod;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 19:43
 */
public class BoosterHandler extends HydrionHandler {

    private final BoostersModule boostersModule;

    public BoosterHandler(BoostersModule boostersModule) {
        this.boostersModule = boostersModule;

        this.addParameterHandlers(new UUIDHandler());
        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> new HydrionResponse(true, new BoosterObject(this.boostersModule.getBooster(request.getParameter(ParameterKeys.UUID, UUID.class)))));
        this.addMethodHandler(HttpMethod.POST, (request, ctx) -> this.handleJsonPost(request, ctx, json -> {
            final JsonObject element = JsonParser.parseString(json).getAsJsonObject();

            if (element.get(BoostersModule.IDENTIFIER_KEY) == null) {
                return new BadJsonError(BoostersModule.IDENTIFIER_KEY);
            }

            final UUID uuid = request.getParameter(ParameterKeys.UUID, UUID.class);
            final BasicDBObject dbObject = BasicDBObject.parse(json);

            if (this.boostersModule.getBooster(uuid) == null) {
                this.boostersModule.addBooster(dbObject);
            } else {
                this.boostersModule.updateBooster(uuid, dbObject);
            }
            return new HydrionPostRequestResponse();
        }));
        this.addMethodHandler(HttpMethod.DELETE, ((request, context) -> {
            this.boostersModule.removeBooster(request.getParameter(ParameterKeys.UUID, UUID.class));

            return new HydrionPostRequestResponse();
        }));
    }

}
