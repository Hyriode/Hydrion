package fr.hyriode.hydrion.routes;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.hydrion.api.http.IHttpRouter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 22/09/2022 at 19:00
 */
public class NetworkRoutes extends Routes {

    public NetworkRoutes() {
        final IHttpRouter router = this.mainRouter.subRouter("/network");

        router.get("/maintenance", (request, ctx) -> {
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

            ctx.json(network.getMaintenance());
        });

        router.get("/slots", (request, ctx) -> {
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

            ctx.json(network.getSlots());
        });

        router.get("/counter", (request, ctx) -> {
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();
            final Map<String, Map<String, Integer>> result = new HashMap<>();
            final List<IHyriGameInfo> games = HyriAPI.get().getGameManager().getGamesInfo();

            if (games == null) {
                ctx.json("");
                return;
            }

            final IHyriGlobalCounter counter = network.getPlayerCounter();

            for (IHyriGameInfo game : games) {
                final String gameName = game.getName();
                final Map<String, Integer> data = result.getOrDefault(gameName, new HashMap<>());

                for (String gameType : game.getTypes().keySet()) {
                    data.put(gameType, counter.getCategory(gameName).getPlayers(gameType));
                }

                result.put(gameName, data);
            }

            ctx.json(result);
        });
    }

}
