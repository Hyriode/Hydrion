package fr.hyriode.hydrion.module.player;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.hydrion.api.module.HydrionModule;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 18:32
 */
public class PlayerModule extends HydrionModule {

    private static final String UUID_KEY = "uuid";

    private MongoDatabase database;
    private MongoCollection<BasicDBObject> accounts;

    protected void init() {
        this.addHandler("/player", new PlayerHandler(this));

        this.database = this.mongoDB.getDatabase("players");
        this.accounts = this.database.getCollection("accounts", BasicDBObject.class);
    }

    public void addPlayer(BasicDBObject dbObject) {
        this.addData(this.accounts, dbObject);
    }

    public void updatePlayer(UUID uuid, BasicDBObject dbObject) {
        this.updateData(this.accounts, UUID_KEY, uuid.toString(), dbObject);
    }

    public BasicDBObject getPlayer(UUID uuid) {
        return this.getData(this.accounts, UUID_KEY, uuid.toString());
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public MongoCollection<BasicDBObject> getPlayers() {
        return this.accounts;
    }

}
