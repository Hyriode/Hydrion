package fr.hyriode.hydrion.module.player;

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
public class PlayerHandler extends HydrionHandler {

    private final PlayerModule playerModule;

    public PlayerHandler(Hydrion hydrion, PlayerModule playerModule) {
        super(hydrion);
        this.playerModule = playerModule;

        this.addParameterHandlers(new UUIDHandler());
        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> this.get(request.getParameter(UUID.class)));
        this.addMethodHandler(HttpMethod.POST, (request, ctx) -> this.post(request.getParameter(UUID.class), request, ctx));
    }

    private HydrionResponse get(UUID playerId) {
        return new HydrionResponse(true, new PlayerObject(this.playerModule.getPlayer(playerId)));
    }

    private HydrionResponse post(UUID playerId, HttpRequest request, HttpContext ctx) {
        return this.handleJsonPost(request, ctx, json -> {
            final BasicDBObject dbObject = BasicDBObject.parse(json);

            if (this.playerModule.getPlayer(playerId) == null) {
                this.playerModule.addPlayer(playerId, dbObject);
            } else {
                this.playerModule.updatePlayer(playerId, dbObject);
            }

            return new HydrionPostRequestResponse();
        });
    }

}
