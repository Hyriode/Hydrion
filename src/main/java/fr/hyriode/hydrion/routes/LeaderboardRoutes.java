package fr.hyriode.hydrion.routes;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leaderboard.HyriLeaderboardScope;
import fr.hyriode.api.leaderboard.HyriLeaderboardScore;
import fr.hyriode.api.leaderboard.IHyriLeaderboard;
import fr.hyriode.hydrion.api.http.IHttpRouter;
import fr.hyriode.hydrion.api.http.request.HttpRequestParameter;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 23/09/2022 at 07:20
 */
public class LeaderboardRoutes extends Routes {

    public LeaderboardRoutes() {
        final IHttpRouter router = this.mainRouter.subRouter("/leaderboard");

        router.get("/", (request, ctx) -> {
            try {
                final String type = request.parameter("type").getValue();
                final String name = request.parameter("name").getValue();
                final IHyriLeaderboard leaderboard = HyriAPI.get().getLeaderboardProvider().getLeaderboard(type, name);
                final HttpRequestParameter fromParameter = request.parameter("from");
                final HttpRequestParameter toParameter = request.parameter("to");
                final HttpRequestParameter scopeParameter = request.parameter("scope");

                if (scopeParameter == null) {
                    final Map<HyriLeaderboardScope, List<HyriLeaderboardScore>> scores = new HashMap<>();

                    if (fromParameter != null && toParameter != null) {
                        final long from = Long.parseLong(fromParameter.getValue());
                        final long to = Long.parseLong(toParameter.getValue());

                        for (HyriLeaderboardScope scope : HyriLeaderboardScope.values()) {
                            scores.put(scope, leaderboard.getScores(scope, from, to));
                        }

                        ctx.json(scores);
                        return;
                    }

                    for (HyriLeaderboardScope scope : HyriLeaderboardScope.values()) {
                        scores.put(scope, leaderboard.getScores(scope));
                    }

                    ctx.json(scores);
                    return;
                }

                final HyriLeaderboardScope scope = HyriLeaderboardScope.valueOf(scopeParameter.getValue());

                if (fromParameter != null && toParameter != null) {
                    final long from = Long.parseLong(fromParameter.getValue());
                    final long to = Long.parseLong(toParameter.getValue());

                    ctx.json(leaderboard.getScores(scope, from, to));
                } else {
                    ctx.json(leaderboard.getScores(scope));
                }
            } catch (Exception e) {
                ctx.error("Bad request!", HttpResponseStatus.BAD_REQUEST);
            }
        });
    }

}
