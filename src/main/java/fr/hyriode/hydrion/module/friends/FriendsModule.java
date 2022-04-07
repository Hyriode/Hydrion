package fr.hyriode.hydrion.module.friends;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.module.AbstractModule;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 18:32
 */
public class FriendsModule extends AbstractModule {

    private static final String UUID_KEY = "uuid";

    private MongoCollection<BasicDBObject> friends;

    public FriendsModule(Hydrion hydrion) {
        super(hydrion);
    }

    protected void init() {
        this.router.addHandler("/friends", new FriendsHandler(hydrion, this));

        this.friends = this.hydrion.getPlayerModule().getDatabase().getCollection("friends", BasicDBObject.class);
    }

    public void addFriends(UUID uuid, BasicDBObject dbObject) {
        this.addData(this.friends, uuid, dbObject);
    }

    public void updateFriends(UUID uuid, BasicDBObject dbObject) {
        this.updateData(this.friends, UUID_KEY, uuid, dbObject);
    }

    public BasicDBObject getFriends(UUID uuid) {
        return this.getData(this.friends, UUID_KEY, uuid);
    }

    public MongoCollection<BasicDBObject> getFriends() {
        return this.friends;
    }

}
