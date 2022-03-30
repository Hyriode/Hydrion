package fr.hyriode.hydrion.network;

import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.network.http.server.HttpServer;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 24/03/2022 at 17:16
 */
public class NetworkManager {

    private final HttpServer server;

    private final Hydrion hydrion;

    public NetworkManager(Hydrion hydrion) {
        this.hydrion = hydrion;
        this.server = new HttpServer(Hydrion.NAME, this.hydrion.getConfiguration().getPort());
    }

    public void start() {
        System.out.println("Starting network manager...");

        this.server.start();
    }

    public void shutdown() {
        System.out.println("Stopping network manager...");

        this.server.stop();
    }

    public HttpServer getServer() {
        return this.server;
    }

}
