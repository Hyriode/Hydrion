package fr.hyriode.hydrion.network;

import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.api.handler.HydrionHandler;
import fr.hyriode.hydrion.api.object.network.INetworkManager;
import fr.hyriode.hydrion.network.http.server.HttpServer;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 24/03/2022 at 17:16
 */
public class NetworkManager implements INetworkManager {

    private final HttpServer server;

    public NetworkManager(Hydrion hydrion) {
        this.server = new HttpServer(Hydrion.NAME, hydrion.getConfiguration().getPort());
    }

    public void start() {
        System.out.println("Starting network manager...");

        this.server.getRouter().setFavicon("favicon.ico");

        this.server.start();
    }

    public void shutdown() {
        System.out.println("Stopping network manager...");

        this.server.stop();
    }

    public HttpServer getServer() {
        return this.server;
    }

    @Override
    public void addHandler(String path, HydrionHandler handler) {
        this.server.getRouter().addHandler(path, handler);
    }

}
