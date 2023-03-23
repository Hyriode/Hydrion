package fr.hyriode.hydrion.routes;

import com.google.gson.JsonObject;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.model.IHyriFriend;
import fr.hyriode.api.player.model.IHyriPlus;
import fr.hyriode.api.player.transaction.HyriPlusTransaction;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.hydrion.api.http.IHttpRouter;
import fr.hyriode.hydrion.api.util.NotchianUtil;
import io.netty.handler.codec.http.HttpResponseStatus;

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
                final UUID playerId = NotchianUtil.parseUUID(request.parameter("uuid").getValue());

                ctx.json(response -> response.add("uuid", playerId).add("player", IHyriPlayer.get(playerId)));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.get("/session", (request, ctx) -> {
            try {
                final UUID playerId = NotchianUtil.parseUUID(request.parameter("uuid").getValue());
                final IHyriPlayerSession session = IHyriPlayerSession.get(playerId);

                ctx.json(response -> response.add("uuid", playerId).add("online", session != null).add("session", session));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.get("/uuid", (request, ctx) -> {
            final String name = request.parameter("name").getValue();

            if (!NotchianUtil.isNameValid(name)) {
                ctx.error("Invalid name!", HttpResponseStatus.BAD_REQUEST);
                return;
            }

            final UUID playerId = HyriAPI.get().getPlayerManager().getPlayerId(name);

            ctx.json(response -> response.add("name", name).add("uuid", playerId));
        });

        router.get("/name", (request, ctx) -> {
            final UUID uuid = UUID.fromString(request.parameter("name").getValue());
            final IHyriPlayer account = IHyriPlayer.get(uuid);

            if (account == null) {
                ctx.error("Invalid player!", HttpResponseStatus.BAD_REQUEST);
                return;
            }

            ctx.json(response -> response.add("name", account.getName()).add("uuid", uuid));
        });

        router.get("/rank", (request, ctx) -> {
            try {
                final UUID playerId = NotchianUtil.parseUUID(request.parameter("uuid").getValue());
                final IHyriPlayer account = IHyriPlayer.get(playerId);

                ctx.json(response -> response.add("uuid", playerId).add("rank", account == null ? null : account.getRank()));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.post("/rank", (request, ctx) -> {
            try {
                final JsonObject body = request.jsonBody();
                final UUID playerId = NotchianUtil.parseUUID(body.get("uuid").getAsString());
                final PlayerRank rank = PlayerRank.valueOf(body.get("rank").getAsString());
                final IHyriPlayer account = IHyriPlayer.get(playerId);

                if (account == null) {
                    ctx.error("Invalid player!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return;
                }

                if (account.getRank().isSuperior(rank)) {
                    ctx.error("Invalid rank!", HttpResponseStatus.BAD_REQUEST);
                    return;
                }

                final PlayerRank oldRank = account.getRank().getPlayerType();

                account.getRank().setPlayerType(rank);
                account.update();

                ctx.json(response -> response.add("uuid", playerId).add("rank", rank).add("old_rank", oldRank));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.get("/hyriplus", (request, ctx) -> {
            try {
                final UUID playerId = NotchianUtil.parseUUID(request.parameter("uuid").getValue());
                final IHyriPlayer account = IHyriPlayer.get(playerId);

                if (account == null) {
                    ctx.error("Invalid player!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return;
                }

                ctx.json(response -> response.add("uuid", playerId).add("rank", account.getRank()).add("hyriplus", account.getHyriPlus()));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.post("/hyriplus", (request, ctx) -> {
            try {
                final JsonObject body = request.jsonBody();
                final UUID playerId = NotchianUtil.parseUUID(body.get("uuid").getAsString());
                final IHyriPlayer account = IHyriPlayer.get(playerId);

                if (account == null) {
                    ctx.error("Invalid player!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return;
                }

                if (account.getRank().getRealPlayerType().getId() != PlayerRank.EPIC.getId()) {
                    ctx.error("Player doesn't have the required rank (" + account.getRank().getRealPlayerType() + " < " + PlayerRank.EPIC + ")!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return;
                }

                final long duration = body.get("duration").getAsLong();
                final IHyriPlus hyriPlus = account.getHyriPlus();
                final boolean expired = hyriPlus.hasExpire();

                hyriPlus.setDuration(hyriPlus.getDuration() + duration);

                if (expired) {
                    hyriPlus.enable();
                }

                account.getTransactions().add(HyriPlusTransaction.TRANSACTIONS_TYPE, new HyriPlusTransaction(duration));

                ctx.json(response -> response.add("uuid", playerId).add("rank", account.getRank()).add("hyriplus", hyriPlus));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.post("/gems", (request, ctx) -> {
            try {
                final JsonObject body = request.jsonBody();
                final UUID playerId = NotchianUtil.parseUUID(body.get("uuid").getAsString());
                final long gems = body.get("gems").getAsLong();

                if (gems <= 0) {
                    ctx.error("Invalid gems (<= 0)!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return;
                }

                final IHyriPlayer account = IHyriPlayer.get(playerId);

                if (account == null) {
                    ctx.error("Invalid player!", HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return;
                }

                account.getGems().add(gems).withMultiplier(false).exec();
                account.update();

                ctx.json(response -> response.add("uuid", playerId).add("gems", account.getGems()));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });

        router.get("/friends", (request, ctx) -> {
            try {
                final UUID playerId = NotchianUtil.parseUUID(request.parameter("uuid").getValue());
                final List<IHyriFriend> friends = IHyriPlayer.get(playerId).getFriends().getAll();

                ctx.json(response -> response.add("uuid", playerId).add("friends", friends));
            } catch (Exception e) {
                ctx.error("Invalid request!", HttpResponseStatus.BAD_REQUEST);
            }
        });
    }

}
