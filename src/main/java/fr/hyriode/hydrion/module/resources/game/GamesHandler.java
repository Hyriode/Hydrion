package fr.hyriode.hydrion.module.resources.game;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.handler.HydrionHandler;
import fr.hyriode.hydrion.module.resources.ResourcesModule;
import fr.hyriode.hydrion.response.HydrionResponse;
import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 21:35
 */
public class GamesHandler extends HydrionHandler {

    private static final String NAME = "name";

    private final ResourcesModule resourcesManager;

    public GamesHandler(Hydrion hydrion, ResourcesModule resourcesManager) {
        super(hydrion);
        this.resourcesManager = resourcesManager;

        this.addMethodHandler(HttpMethod.GET, (ctx, response) -> {
            final Map<String, BasicDBObject> gamesMap = new HashMap<>();
            final List<BasicDBObject> games = this.resourcesManager.getGames();

            if (games != null) {
                for (BasicDBObject game : games) {
                    gamesMap.put(game.getString(NAME), game);

                    game.removeField(NAME);
                }
            }

            return new HydrionResponse(true, new GamesObject(gamesMap));
        });
    }

}
