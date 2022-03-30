package fr.hyriode.hydrion.database.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import fr.hyriode.hydrion.database.IDatabaseConnection;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 16:15
 */
public class MongoDBConnection implements IDatabaseConnection {

    private MongoClient client;

    private final String url;

    public MongoDBConnection(String url) {
        this.url = url;
    }

    @Override
    public void start() {
        System.out.println("Starting MongoDB connection...");

        final ConnectionString connectionString = new ConnectionString(this.url);
        final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        this.client = MongoClients.create(settings);
    }

    @Override
    public void stop() {
        System.out.println("Stopping MongoDB connection...");

        this.client.close();
    }

    public MongoClient getClient() {
        return this.client;
    }

}
