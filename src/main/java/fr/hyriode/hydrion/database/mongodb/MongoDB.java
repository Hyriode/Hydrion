package fr.hyriode.hydrion.database.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.hydrion.database.IDatabaseConnection;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 16:15
 */
public class MongoDB implements IDatabaseConnection {

    private MongoClient client;

    private final String url;

    public MongoDB(String url) {
        this.url = url;
    }

    @Override
    public void start() {
        System.out.println("Starting MongoDB connection...");

        final ConnectionString connectionString = new ConnectionString(this.url);
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();

        this.client = MongoClients.create(settings);
    }

    @Override
    public void stop() {
        System.out.println("Stopping MongoDB connection...");

        this.client.close();
    }

    public MongoDatabase getDatabase(String name) {
        return this.client.getDatabase(name);
    }

    public MongoClient getClient() {
        return this.client;
    }

}
