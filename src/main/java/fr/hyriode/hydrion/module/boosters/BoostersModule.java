package fr.hyriode.hydrion.module.boosters;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.module.HydrionModule;

import java.util.List;
import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 18:32
 */
public class BoostersModule extends HydrionModule {

    public static final String IDENTIFIER_KEY = "identifier";
    private static final String KEY = "boosters";

    private MongoDatabase database;
    private MongoCollection<BasicDBObject> actives;

    protected void init() {
        this.addHandler("/boosters", new BoostersHandler(this));
        this.addHandler("/booster", new BoosterHandler(this));

        this.database = this.mongoDB.getDatabase("boosters");
        this.actives = this.database.getCollection("actives", BasicDBObject.class);
    }

    public void addBooster(BasicDBObject dbObject) {
        this.addData(this.actives, dbObject);

        HydrionAPI.POOL.execute(() -> {
            final List<BasicDBObject> dbObjects = this.getBoosters();

            dbObjects.add(dbObject);

            this.updateAllCachedData(KEY, dbObjects);
        });
    }

    public void updateBooster(UUID uuid, BasicDBObject dbObject) {
        this.updateData(this.actives, IDENTIFIER_KEY, uuid.toString(), dbObject);

        HydrionAPI.POOL.execute(() -> {
            final List<BasicDBObject> dbObjects = this.getBoosters();

            for (BasicDBObject object : dbObjects) {
                if (object.getObjectId(ID).toString().equals(dbObject.getObjectId(ID).toString())) {
                    dbObjects.remove(object);
                    dbObjects.add(dbObject);
                }
            }

            this.updateAllCachedData(KEY, dbObjects);
        });
    }

    public void removeBooster(UUID uuid) {
        final String uuidStr = uuid.toString();
        this.removeData(this.actives, IDENTIFIER_KEY, uuidStr);

        HydrionAPI.POOL.execute(() -> {
            final List<BasicDBObject> dbObjects = this.getBoosters();

            dbObjects.removeIf(object -> object.getString(IDENTIFIER_KEY).equals(uuidStr));

            this.updateAllCachedData(KEY, dbObjects);
        });
    }

    public BasicDBObject getBooster(UUID uuid) {
        return this.getData(this.actives, IDENTIFIER_KEY, uuid.toString());
    }

    public List<BasicDBObject> getBoosters() {
        return this.getAllData(this.actives, KEY);
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public MongoCollection<BasicDBObject> getActives() {
        return this.actives;
    }

}
