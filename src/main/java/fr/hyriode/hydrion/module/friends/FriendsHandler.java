package fr.hyriode.hydrion.module.friends;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.api.handler.HydrionHandler;
import fr.hyriode.hydrion.api.handler.parameter.ParameterKeys;
import fr.hyriode.hydrion.api.handler.parameter.UUIDHandler;
import fr.hyriode.hydrion.api.http.HttpContext;
import fr.hyriode.hydrion.api.http.request.HttpRequest;
import fr.hyriode.hydrion.api.response.HydrionPostRequestResponse;
import fr.hyriode.hydrion.api.response.HydrionResponse;
import io.netty.handler.codec.http.HttpMethod;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:38
 */
public class FriendsHandler extends HydrionHandler {

    private final FriendsModule friendsManager;

    public FriendsHandler(FriendsModule friendsManager) {
        this.friendsManager = friendsManager;

        this.addParameterHandlers(new UUIDHandler());
        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> this.get(request.getParameter(ParameterKeys.UUID, UUID.class)));
        this.addMethodHandler(HttpMethod.POST, (request, ctx) -> this.post(request.getParameter(ParameterKeys.UUID, UUID.class), request, ctx));
    }

    private HydrionResponse get(UUID playerId) {
        return new HydrionResponse(true, new FriendsObject(playerId, this.friendsManager.getFriends(playerId)));
    }

    private HydrionResponse post(UUID playerId, HttpRequest request, HttpContext ctx) {
        return this.handleJsonPost(request, ctx, json -> {
            final BasicDBObject dbObject = BasicDBObject.parse(json);

            if (this.friendsManager.getFriends(playerId) == null) {
                this.friendsManager.addFriends(dbObject);
            } else {
                this.friendsManager.updateFriends(playerId, dbObject);
            }
            return new HydrionPostRequestResponse();
        });
    }

}
