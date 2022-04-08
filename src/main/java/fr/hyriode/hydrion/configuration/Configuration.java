package fr.hyriode.hydrion.configuration;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:50
 */
public class Configuration {

    private final int port;
    private final String mongoDBUrl;
    private final UUID apiKey;

    public Configuration(int port, String mongoDBUrl, UUID apiKey) {
        this.port = port;
        this.mongoDBUrl = mongoDBUrl;
        this.apiKey = apiKey;
    }

    public int getPort() {
        return this.port;
    }

    public String getMongoDBUrl() {
        return this.mongoDBUrl;
    }

    public UUID getAPIKey() {
        return this.apiKey;
    }

}
