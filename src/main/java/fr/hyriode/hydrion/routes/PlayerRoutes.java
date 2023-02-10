package fr.hyriode.hydrion.routes;

import com.google.gson.JsonObject;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.hyriplus.HyriPlus;
import fr.hyriode.api.rank.hyriplus.HyriPlusTransaction;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hydrion.api.http.IHttpRouter;
import fr.hyriode.hydrion.api.util.UUIDUtil;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 20/09/2022 at 20:56
 */
public class PlayerRoutes extends Routes {

    public PlayerRoutes() {
        final IHttpRouter router = this.mainRouter.subRouter("/player");

        router.get("/", (request, ctx) -> {
            try {
                final UUID playerId = UUIDUtil.parseString(request.parameter("uuid").getValue());

                ctx.json(IHyriPlayer.get(playerId));
            } catch (Exception e) {
                ctx.error("Invalid uuid!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.get("/uuid", (request, ctx) -> {
            final String name = request.parameter("name").getValue();
            final UUID playerId = HyriAPI.get().getPlayerManager().getPlayerId(name);

            ctx.json(playerId);
        });

        router.get("/rank", (request, ctx) -> {
            final UUID playerId = UUIDUtil.parseString(request.parameter("uuid").getValue());
            final IHyriPlayer account = IHyriPlayer.get(playerId);

            ctx.json(account.getRank());
        });

        router.post("/rank", (request, ctx) -> {
            try {
                final JsonObject body = request.jsonBody();
                final UUID playerId = UUIDUtil.parseString(body.get("uuid").getAsString());
                final HyriPlayerRankType rank = HyriPlayerRankType.valueOf(body.get("rank").getAsString());
                final IHyriPlayer account = IHyriPlayer.get(playerId);

                if (account == null) {
                    ctx.error("Invalid player!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return;
                }

                if (account.getRank().isSuperior(rank)) {
                    ctx.error("Invalid rank!", HttpResponseStatus.BAD_REQUEST);
                    return;
                }

                account.setPlayerRank(rank);
                account.update();

                ctx.json(account.getRank());
            } catch (Exception e) {
                ctx.error("Invalid body!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.get("/hyriplus", (request, ctx) -> {
            final UUID playerId = UUIDUtil.parseString(request.parameter("uuid").getValue());
            final IHyriPlayer account = IHyriPlayer.get(playerId);

            if (account == null) {
                ctx.error("Invalid player!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                return;
            }

            ctx.json(account.getHyriPlus());
        });

        router.post("/hyriplus", (request, ctx) -> {
            try {
                final JsonObject body = request.jsonBody();
                final UUID playerId = UUIDUtil.parseString(body.get("uuid").getAsString());
                final IHyriPlayer account = IHyriPlayer.get(playerId);

                if (account == null) {
                    ctx.error("Invalid player!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return;
                }

                final long currentTime = System.currentTimeMillis();
                final long duration = body.get("duration").getAsLong();
                final HyriPlus hyriPlus = account.getHyriPlus();
                final boolean expired = hyriPlus.hasExpire();

                hyriPlus.setDuration(hyriPlus.getDuration() + duration);

                if (expired) {
                    hyriPlus.enable();
                }

                account.addTransaction(HyriPlusTransaction.TRANSACTION_TYPE, String.valueOf(currentTime), new HyriPlusTransaction(duration));

                ctx.json(body);
            } catch (Exception e) {
                ctx.error("Invalid body!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.get("/friends", (request, ctx) -> {
            try {
                final UUID playerId = UUIDUtil.parseString(request.parameter("uuid").getValue());

                List<IHyriFriend> friends = HyriAPI.get().getFriendManager().getFriends(playerId);

                if (friends == null) {
                    friends = new ArrayList<>();
                }

                ctx.json(friends);
            } catch (Exception e) {
                ctx.error("Bad request!", HttpResponseStatus.BAD_REQUEST);
            }
        });
    }

}
