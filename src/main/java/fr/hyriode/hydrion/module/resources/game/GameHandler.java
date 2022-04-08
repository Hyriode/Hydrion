package fr.hyriode.hydrion.module.resources.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.handler.HydrionHandler;
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

    private static final String NAME_PARAMETER = "name";

    private final ResourcesModule resourcesModule;

    public GameHandler(ResourcesModule resourcesModule) {
        this.resourcesModule = resourcesModule;

        this.addParameterHandlers(new StringHandler(NAME_PARAMETER));
        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> new HydrionResponse(true, new GameObject(this.resourcesModule.getGame(request.getParameter(String.class)))));
        this.addMethodHandler(HttpMethod.POST, (request, ctx) -> this.handleJsonPost(request, ctx, json -> {
            final JsonObject element = JsonParser.parseString(json).getAsJsonObject();

            if (element.get(NAME_PARAMETER) == null) {
                return new BadJsonError(NAME_PARAMETER);
            }

            final String name = request.getParameter(String.class);
            final BasicDBObject dbObject = BasicDBObject.parse(json);

            if (this.resourcesModule.getGame(name) == null) {
                this.resourcesModule.addGame(name, dbObject);
            } else {
                this.resourcesModule.updateGame(name, dbObject);
            }
            return new HydrionPostRequestResponse();
        }));
        this.addMethodHandler(HttpMethod.DELETE, ((request, context) -> {
            final String name = request.getParameter(String.class);

            this.resourcesModule.removeGame(name);

            return new HydrionPostRequestResponse();
        }));
    }

}
