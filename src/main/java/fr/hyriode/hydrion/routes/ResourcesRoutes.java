package fr.hyriode.hydrion.routes;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.http.IHttpRouter;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 23/09/2022 at 07:12
 */
public class ResourcesRoutes extends Routes {

    public ResourcesRoutes() {
        final IHttpRouter router = this.mainRouter.subRouter("/resources");

        router.get("/games", (request, ctx) -> {
            final Map<String, IHyriGameInfo> result = new HashMap<>();
            final List<IHyriGameInfo> games = HyriAPI.get().getGameManager().getGamesInfo();

            for (IHyriGameInfo game : games) {
                result.put(game.getName(), game);
            }

            ctx.json(response -> response.add("games", result));
        });

        router.get("/game", (request, ctx) -> {
            try {
                final String gameName = request.parameter("name").getValue();
                final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(gameName);

                ctx.json(response -> response.add("game", gameInfo));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.get("/rotating-game", (request, ctx) -> ctx.json(response -> response.add("rotating_game", HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame())));
        router.get("/rotating-games", (request, ctx) -> ctx.json(response -> response.add("rotating_games", HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGames())));
    }

}