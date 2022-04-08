package fr.hyriode.hydrion.module.network;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.api.module.HydrionModule;
import fr.hyriode.hydrion.network.http.HttpRouter;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 18:32
 */
public class NetworkModule extends HydrionModule {

    private static final String NAME_KEY = "name";
    private static final String INFO_KEY = "information";

    private MongoCollection<BasicDBObject> networkCollection;

    private MongoDatabase database;

    protected void init() {
        this.addHandler("/network", new NetworkHandler(this));

        this.database = this.mongoDB.getDatabase("network");
        this.networkCollection = this.database.getCollection("network", BasicDBObject.class);
    }

    public void setNetwork(BasicDBObject dbObject) {
        this.addData(this.networkCollection, INFO_KEY, dbObject);
    }

    public BasicDBObject getNetwork() {
        return this.getData(this.networkCollection, NAME_KEY, INFO_KEY);
    }

    public MongoCollection<BasicDBObject> getNetworkCollection() {
        return this.networkCollection;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

}
