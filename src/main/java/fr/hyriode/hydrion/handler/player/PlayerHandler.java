package fr.hyriode.hydrion.handler.player;

import fr.hyriode.hydrion.handler.HydrionHandler;
import fr.hyriode.hydrion.network.http.HttpContext;
import fr.hyriode.hydrion.network.http.request.HttpRequest;
import fr.hyriode.hydrion.object.player.PlayerObject;
import fr.hyriode.hydrion.response.HydrionResponse;
import fr.hyriode.hydrion.response.error.HydrionParameterError;
import fr.hyriode.hydrion.response.error.HydrionUUIDError;
import fr.hyriode.hydrion.util.UUIDUtil;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:38
 */
public class PlayerHandler extends HydrionHandler {

    private static final String UUID_PARAMETER = "uuid";

    @Override
    public HydrionResponse handle(HttpRequest request, HttpContext response) {
        if (!request.hasParameter(UUID_PARAMETER)) {
            return new HydrionParameterError(UUID_PARAMETER);
        }

        final String uuidInput = request.getParameter(UUID_PARAMETER).getValue();

        if (!UUIDUtil.isUUID(uuidInput)) {
            return new HydrionUUIDError();
        }

        final UUID uuid = UUID.fromString(uuidInput);

        // TODO Fetch player account

        return new HydrionResponse(true, new PlayerObject("AstFaster", "AstFaster", uuid));
    }

}
