package fr.hyriode.hydrion.module.network;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.api.module.HydrionModule;
import fr.hyriode.hydrion.network.http.HttpRouter;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 18:32
 */
public class NetworkModule extends HydrionModule {

    private MongoCollection<BasicDBObject> networkCollection;

    private MongoDatabase database;

    protected void init() {
        this.addHandler("/network", new NetworkHandler(this));

        this.database = this.mongoDB.getDatabase("network");
        this.networkCollection = this.database.getCollection("network", BasicDBObject.class);
    }

    public void setNetwork(BasicDBObject dbObject) {
        this.addData(this.networkCollection, dbObject);
    }

    public BasicDBObject getNetwork() {
        final List<BasicDBObject> all = this.getAllData(this.networkCollection, "network");

        if (all != null && all.size() > 0) {
            return all.get(0);
        }
        return null;
    }

    public MongoCollection<BasicDBObject> getNetworkCollection() {
        return this.networkCollection;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

}
