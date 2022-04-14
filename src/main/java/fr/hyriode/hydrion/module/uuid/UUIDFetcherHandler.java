package fr.hyriode.hydrion.module.uuid;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.handler.HydrionHandler;
import fr.hyriode.hydrion.api.handler.parameter.ParameterKeys;
import fr.hyriode.hydrion.api.handler.parameter.StringHandler;
import fr.hyriode.hydrion.api.response.HydrionPostRequestResponse;
import fr.hyriode.hydrion.api.response.HydrionResponse;
import fr.hyriode.hydrion.api.response.error.BadJsonError;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 14/04/2022 at 10:56
 */
public class UUIDFetcherHandler extends HydrionHandler {

    public UUIDFetcherHandler(UUIDFetcherModule module) {
        this.addParameterHandlers(new StringHandler(ParameterKeys.NAME));
        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> {
            final String name = request.getParameter(ParameterKeys.NAME, String.class);

            return new HydrionResponse(true, new UUIDObject(module.getUUID(name)));
        });
        this.addMethodHandler(HttpMethod.POST, (request, ctx) -> this.handleJsonPost(request, ctx, json -> {
            final JsonObject element = JsonParser.parseString(json).getAsJsonObject();

            if (element.get(ParameterKeys.UUID) == null) {
                return new BadJsonError(ParameterKeys.UUID);
            }

            final BasicDBObject dbObject = BasicDBObject.parse(json);
            final String name = request.getParameter(ParameterKeys.NAME, String.class);

            if (module.getUUID(name) == null) {
                module.addUUID(name, dbObject);
            } else {
                module.updateUUID(name, dbObject);
            }

            return new HydrionPostRequestResponse();
        }));
    }

}
