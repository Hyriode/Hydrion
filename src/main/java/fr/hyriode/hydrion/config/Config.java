package fr.hyriode.hydrion.config;

import fr.hyriode.api.config.MongoDBConfig;
import fr.hyriode.api.config.RedisConfig;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:50
 */
public class Config {

    private final int port;
    private final RedisConfig redisConfig;
    private final MongoDBConfig mongoDBConfig;
    private final UUID apiKey;

    public Config(int port, RedisConfig redisConfig, MongoDBConfig mongoDBConfig, UUID apiKey) {
        this.port = port;
        this.redisConfig = redisConfig;
        this.mongoDBConfig = mongoDBConfig;
        this.apiKey = apiKey;
    }

    public int getPort() {
        return this.port;
    }

    public RedisConfig getRedisConfig() {
        return this.redisConfig;
    }

    public MongoDBConfig getMongoDBConfig() {
        return this.mongoDBConfig;
    }

    public UUID getAPIKey() {
        return this.apiKey;
    }

}
