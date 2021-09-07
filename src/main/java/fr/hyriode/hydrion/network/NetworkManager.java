package fr.hyriode.hydrion.network;

import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.configuration.Configuration;
import fr.hyriode.hydrion.network.api.HttpServer;
import fr.hyriode.hydrion.network.api.Router;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 18:24
 */
public class NetworkManager {

    private HttpServer httpServer;

    private final Hydrion hydrion;

    public NetworkManager(Hydrion hydrion) {
        this.hydrion = hydrion;
    }

    public void start() {
        System.out.println("Starting network manager...");

        final Configuration configuration = this.hydrion.getConfiguration();

        final Router router = new Router("", (ctx, request) -> ctx.html("Default"))
                .addHandler("/test", (ctx, request) -> ctx.json(new Response("Test")))
                .setFavicon("/favicon.ico");

        this.httpServer = new HttpServer(configuration.getHost(), configuration.getPort(), router);

        this.httpServer.start();
    }

    public void shutdown() {
        System.out.println("Stopping network manager...");

        this.httpServer.stop();
    }

}
