package fr.hyriode.hydrion.module.friends;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.module.HydrionModule;
import fr.hyriode.hydrion.module.player.PlayerModule;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 18:32
 */
public class FriendsModule extends HydrionModule {

    private static final String UUID_KEY = "uuid";

    private MongoCollection<BasicDBObject> friends;

    public FriendsModule() {
        super("friends");
    }

    protected void init() {
        this.addHandler("/friends", new FriendsHandler(this));

        this.friends = HydrionAPI.get().getModuleManager().getModule(PlayerModule.class).getDatabase().getCollection("friends", BasicDBObject.class);
    }

    public void addFriends(BasicDBObject dbObject) {
        this.addData(this.friends, dbObject);
    }

    public void updateFriends(UUID uuid, BasicDBObject dbObject) {
        this.updateData(this.friends, UUID_KEY, uuid.toString(), dbObject);
    }

    public BasicDBObject getFriends(UUID uuid) {
        return this.getData(this.friends, UUID_KEY, uuid.toString());
    }

    public MongoCollection<BasicDBObject> getFriends() {
        return this.friends;
    }

}
