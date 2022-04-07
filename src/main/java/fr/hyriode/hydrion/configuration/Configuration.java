package fr.hyriode.hydrion.configuration;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:50
 */
public class Configuration {

    private final int port;
    private final String mongoDBUrl;

    public Configuration(int port, String mongoDBUrl) {
        this.port = port;
        this.mongoDBUrl = mongoDBUrl;
    }

    public int getPort() {
        return this.port;
    }

    public String getMongoDBUrl() {
        return this.mongoDBUrl;
    }

}
