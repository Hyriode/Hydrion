package fr.hyriode.hydrion.routes;

import com.google.gson.annotations.Expose;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.IHyriGameType;
import fr.hyriode.api.limbo.IHyriLimboManager;
import fr.hyriode.api.network.IHyriNetwork;
import fr.hyriode.api.network.counter.IHyriCategoryCounter;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.api.server.ILobbyAPI;
import fr.hyriode.hydrion.api.http.IHttpRouter;
import fr.hyriode.hyggdrasil.api.limbo.HyggLimbo;

import java.util.ArrayList;
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

            ctx.json(response -> response.add("maintenance", network.getMaintenance()));
        });

        router.get("/slots", (request, ctx) -> {
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

            ctx.json(response -> response.add("slots", network.getSlots()));
        });

        router.get("/counter", (request, ctx) -> {
            final IHyriNetwork network = HyriAPI.get().getNetworkManager().getNetwork();

            List<IHyriGameInfo> games = HyriAPI.get().getGameManager().getGamesInfo();
            if (games == null) {
                games = new ArrayList<>();
            }

            final IHyriGlobalCounter counter = network.getPlayerCounter();
            final Counter result = new Counter(counter.getPlayers());

            result.addGame(ILobbyAPI.TYPE, new Counter.Game(counter.getCategory(ILobbyAPI.TYPE).getPlayers()));

            final IHyriCategoryCounter limbosCounter = counter.getCategory(IHyriLimboManager.LIMBOS_ID);
            final Counter.TypesGame limbos = new Counter.TypesGame(limbosCounter.getPlayers());

            for (HyggLimbo.Type limboType : HyggLimbo.Type.values()) {
                limbos.addType(limboType.toString(), limbosCounter.getPlayers(limboType.toString()));
            }

            result.addGame(IHyriLimboManager.LIMBOS_ID, limbos);

            for (IHyriGameInfo game : games) {
                final IHyriCategoryCounter categoryCounter = counter.getCategory(game.getName());
                final Counter.TypesGame resultGame = new Counter.TypesGame(categoryCounter.getPlayers());

                for (IHyriGameType type : game.getTypes()) {
                    resultGame.addType(type.getName(), categoryCounter.getPlayers(type.getName()));
                }

                result.addGame(game.getName(), resultGame);
            }

            ctx.json(response -> response.add("counter", result));
        });
    }

    private static class Counter {

        @Expose
        private final int players;

        @Expose
        private final Map<String, Game> games = new HashMap<>();

        public Counter(int players) {
            this.players = players;
        }

        public void addGame(String gameName, Game game) {
            this.games.put(gameName, game);
        }

        private static class Game {

            @Expose
            private final int players;

            public Game(int players) {
                this.players = players;
            }

        }

        private static class TypesGame extends Game {

            @Expose
            private final Map<String, Integer> types = new HashMap<>();

            public TypesGame(int players) {
                super(players);
            }

            public void addType(String type, int players) {
                this.types.put(type, players);
            }

        }

    }

}
