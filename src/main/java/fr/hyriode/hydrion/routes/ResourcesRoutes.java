package fr.hyriode.hydrion.routes;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.rotating.IHyriRotatingGame;
import fr.hyriode.api.impl.common.game.rotating.HyriRotatingGame;
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

        router.get("/rotating-game", (request, ctx) -> ctx.json(response -> {
            final IHyriRotatingGame rotatingGame = HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame();
            final RotatingGame result = new RotatingGame(rotatingGame.getInfo(), rotatingGame.sinceWhen());

            response.add("rotating_game", result);
        }));
        router.get("/rotating-games", (request, ctx) -> ctx.json(response -> response.add("rotating_games", HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGames())));
    }

    private static class RotatingGame {

        private final IHyriGameInfo info;
        private final long sinceWhen;

        public RotatingGame(IHyriGameInfo info, long sinceWhen) {
            this.info = info;
            this.sinceWhen = sinceWhen;
        }

    }

}