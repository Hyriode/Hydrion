package fr.hyriode.hydrion.network;

import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.api.http.HttpContext;
import fr.hyriode.hydrion.api.http.IHttpRouter;
import fr.hyriode.hydrion.api.http.request.HttpRequest;
import fr.hyriode.hydrion.api.network.INetworkManager;
import fr.hyriode.hydrion.http.HttpRouter;
import fr.hyriode.hydrion.http.server.HttpServer;
import fr.hyriode.hydrion.routes.LeaderboardRoutes;
import fr.hyriode.hydrion.routes.NetworkRoutes;
import fr.hyriode.hydrion.routes.PlayerRoutes;
import fr.hyriode.hydrion.routes.ResourcesRoutes;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 24/03/2022 at 17:16
 */
public class NetworkManager implements INetworkManager {

    private final Map<String, HttpRouter> routers;

    private final HttpServer server;

    private final Hydrion hydrion;

    public NetworkManager(Hydrion hydrion) {
        this.hydrion = hydrion;
        this.server = new HttpServer(this, Hydrion.NAME, this.hydrion.getConfiguration().getPort());
        this.routers = new HashMap<>();
    }

    public void start() {
        System.out.println("Starting network manager...");

        final IHttpRouter router = this.newRouter("/");

        router.get("/", (request, ctx) -> ctx.text("Welcome on Hydrion!"));

        new PlayerRoutes();
        new NetworkRoutes();
        new ResourcesRoutes();
        new LeaderboardRoutes();

        this.server.start();
    }

    public void shutdown() {
        System.out.println("Stopping network manager...");

        this.server.stop();
    }

    public void dispatch(HttpRequest request, HttpContext context) {
        try {
            final UUID apiKey = UUID.fromString(request.headers().get("API-Key"));
            final UUID realKey = this.hydrion.getConfiguration().getAPIKey();

            if (!apiKey.equals(realKey)) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            context.error("Invalid API-Key!", HttpResponseStatus.UNAUTHORIZED);
            return;
        }

        final int parametersStart = request.uri().indexOf("?");
        final String uri = request.uri().substring(0, parametersStart == -1 ? request.uri().length() : parametersStart);
        final String[] path = uri.split("((?=/))");

        HttpRouter router = this.routers.get(path[0]);

        if (router == null) {
            router = this.routers.get("/");

            if (router == null) {
                context.error("Invalid endpoint!", HttpResponseStatus.NOT_FOUND);
                return;
            }

            router.dispatch(path, request, context);
            return;
        }

        router.dispatch(Arrays.copyOfRange(path, 1, path.length), request, context);
    }

    @Override
    public IHttpRouter newRouter(String path) {
        final HttpRouter router = new HttpRouter(path);

        this.routers.put(path.toLowerCase(), router);

        return router;
    }

    @Override
    public IHttpRouter getRouter(String path) {
        return this.routers.get(path.toLowerCase());
    }

}
