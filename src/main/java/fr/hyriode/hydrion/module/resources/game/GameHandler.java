package fr.hyriode.hydrion.module.resources.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.handler.HydrionHandler;
import fr.hyriode.hydrion.api.handler.parameter.ParameterKeys;
import fr.hyriode.hydrion.api.handler.parameter.StringHandler;
import fr.hyriode.hydrion.api.response.HydrionPostRequestResponse;
import fr.hyriode.hydrion.api.response.HydrionResponse;
import fr.hyriode.hydrion.api.response.error.BadJsonError;
import fr.hyriode.hydrion.module.resources.ResourcesModule;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 19:43
 */
public class GameHandler extends HydrionHandler {

    private final ResourcesModule resourcesModule;

    public GameHandler(ResourcesModule resourcesModule) {
        this.resourcesModule = resourcesModule;

        this.addParameterHandlers(new StringHandler(ParameterKeys.NAME));
        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> new HydrionResponse(true, new GameObject(this.resourcesModule.getGame(request.getParameter(ParameterKeys.NAME, String.class)))));
        this.addMethodHandler(HttpMethod.POST, (request, ctx) -> this.handleJsonPost(request, ctx, json -> {
            final JsonObject element = JsonParser.parseString(json).getAsJsonObject();

            if (element.get(ParameterKeys.NAME) == null) {
                return new BadJsonError(ParameterKeys.NAME);
            }

            final String name = request.getParameter(ParameterKeys.NAME, String.class);
            final BasicDBObject dbObject = BasicDBObject.parse(json);

            if (this.resourcesModule.getGame(name) == null) {
                System.out.println("Added '" + name + "' game");

                this.resourcesModule.addGame(dbObject);
            } else {
                System.out.println("Updated '" + name + "' game");

                this.resourcesModule.updateGame(name, dbObject);
            }
            return new HydrionPostRequestResponse();
        }));
        this.addMethodHandler(HttpMethod.DELETE, ((request, context) -> {
            final String name = request.getParameter(ParameterKeys.NAME, String.class);

            this.resourcesModule.removeGame(name);

            return new HydrionPostRequestResponse();
        }));
    }

}
