package fr.hyriode.hydrion.module.uuid;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.module.HydrionModule;
import fr.hyriode.hydrion.module.player.PlayerModule;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 14/04/2022 at 10:54
 */
public class UUIDFetcherModule extends HydrionModule {

    private static final String NAME = "name";

    private MongoCollection<BasicDBObject> uuids;

    public UUIDFetcherModule() {
        super("uuid");
    }

    protected void init() {
        this.addHandler("/uuid", new UUIDFetcherHandler(this));

        this.uuids = HydrionAPI.get().getModuleManager().getModule(PlayerModule.class).getDatabase().getCollection("uuid", BasicDBObject.class);
    }

    public void addUUID(String name, BasicDBObject dbObject) {
        this.addData(this.uuids, dbObject);
    }

    public void updateUUID(String name, BasicDBObject dbObject) {
        this.updateData(this.uuids, NAME, name, dbObject);
    }

    public BasicDBObject getUUID(String name) {
        return this.getData(this.uuids, NAME, name);
    }

}
