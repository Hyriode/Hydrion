package fr.hyriode.hydrion.module.friends;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.handler.HydrionHandler;
import fr.hyriode.hydrion.handler.parameter.UUIDHandler;
import fr.hyriode.hydrion.network.http.HttpContext;
import fr.hyriode.hydrion.network.http.request.HttpRequest;
import fr.hyriode.hydrion.response.HydrionPostRequestResponse;
import fr.hyriode.hydrion.response.HydrionResponse;
import io.netty.handler.codec.http.HttpMethod;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:38
 */
public class FriendsHandler extends HydrionHandler {

    private final FriendsModule friendsManager;

    public FriendsHandler(Hydrion hydrion, FriendsModule friendsManager) {
        super(hydrion);
        this.friendsManager = friendsManager;

        this.addAcceptedMethods(HttpMethod.GET, HttpMethod.POST);
        this.addParameterHandlers(new UUIDHandler());
        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> this.get(request.getParameter(UUID.class)));
        this.addMethodHandler(HttpMethod.POST, (request, ctx) -> this.post(request.getParameter(UUID.class), request, ctx));
    }

    private HydrionResponse get(UUID playerId) {
        return new HydrionResponse(true, new FriendsObject(playerId, this.friendsManager.getFriends(playerId)));
    }

    private HydrionResponse post(UUID playerId, HttpRequest request, HttpContext ctx) {
        return this.handleJsonPost(request, ctx, json -> {
            final BasicDBObject dbObject = BasicDBObject.parse(json);

            if (this.friendsManager.getFriends(playerId) == null) {
                this.friendsManager.addFriends(playerId, dbObject);
            } else {
                this.friendsManager.updateFriends(playerId, dbObject);
            }
            return new HydrionPostRequestResponse();
        });
    }

}
