package fr.hyriode.hydrion.routes;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leaderboard.HyriLeaderboardScope;
import fr.hyriode.api.leaderboard.HyriLeaderboardScore;
import fr.hyriode.api.leaderboard.IHyriLeaderboard;
import fr.hyriode.hydrion.api.http.IHttpRouter;
import fr.hyriode.hydrion.api.http.request.HttpRequestParameter;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.*;

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
                final long from = Long.parseLong(request.parameter("from").getValue());
                final long to = Long.parseLong(request.parameter("to").getValue());
                final HttpRequestParameter scopeParameter = request.parameter("scope");

                if (scopeParameter == null) {
                    final Map<HyriLeaderboardScope, Leaderboard> result = new HashMap<>();

                    for (HyriLeaderboardScope scope : HyriLeaderboardScope.values()) {
                        final List<HyriLeaderboardScore> scores = leaderboard.getScores(scope);
                        final Leaderboard resultLeaderboard = new Leaderboard(type, name, scope, leaderboard.getSize(scope), scores.size(), from, to);

                        for (HyriLeaderboardScore score : scores) {
                            resultLeaderboard.addScore(new Leaderboard.Score(score.getId(), score.getValue()));
                        }

                        result.put(scope, resultLeaderboard);
                    }

                    ctx.json(response -> response.add("leaderboards", result));
                    return;
                }

                final HyriLeaderboardScope scope = HyriLeaderboardScope.valueOf(scopeParameter.getValue());
                final List<HyriLeaderboardScore> scores = leaderboard.getScores(scope);
                final Leaderboard result = new Leaderboard(type, name, scope, leaderboard.getSize(scope), scores.size(), from, to);

                for (HyriLeaderboardScore score : scores) {
                    result.addScore(new Leaderboard.Score(score.getId(), score.getValue()));
                }

                ctx.json(response -> response.add("leaderboard", result));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });
    }

    private static class Leaderboard {

        private final String type;
        private final String name;
        private final HyriLeaderboardScope scope;

        private final long totalSize;
        private final long currentSize;

        private final long from;
        private final long to;

        private final List<Score> scores = new ArrayList<>();

        public Leaderboard(String type, String name, HyriLeaderboardScope scope, long totalSize, long currentSize, long from, long to) {
            this.type = type;
            this.name = name;
            this.scope = scope;
            this.totalSize = totalSize;
            this.currentSize = currentSize;
            this.from = from;
            this.to = to;
        }

        public void addScore(Score score) {
            this.scores.add(score);
        }

        private record Score(UUID playerId, Number score) {}

    }

}
