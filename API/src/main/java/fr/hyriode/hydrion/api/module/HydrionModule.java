package fr.hyriode.hydrion.api.module;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.cache.CachedDBObject;
import fr.hyriode.hydrion.api.cache.CachedDBObjectList;
import fr.hyriode.hydrion.api.cache.ICacheManager;
import fr.hyriode.hydrion.api.database.mongodb.MongoDB;
import fr.hyriode.hydrion.api.database.mongodb.subscriber.CallbackSubscriber;
import fr.hyriode.hydrion.api.database.mongodb.subscriber.DataCallbackSubscriber;
import fr.hyriode.hydrion.api.database.mongodb.subscriber.DataSubscriber;
import fr.hyriode.hydrion.api.handler.HydrionHandler;
import org.bson.BsonValue;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 19:21
 */
public abstract class HydrionModule {

    protected static final String ID = "_id";

    protected final MongoDB mongoDB;
    protected final ICacheManager cacheManager;

    protected final String cachedDataKey;

    public HydrionModule(String cachedDataKey) {
        this.cachedDataKey = cachedDataKey + ":";
        final HydrionAPI api = HydrionAPI.get();

        this.mongoDB = api.getMongoDB();
        this.cacheManager = api.getCacheManager();

        this.init();
    }

    protected abstract void init();

    protected void addHandler(String path, HydrionHandler handler) {
        HydrionAPI.get().getNetworkManager().addHandler(path, handler);
    }

    protected void addData(MongoCollection<BasicDBObject> collection, BasicDBObject dbObject) {
        final CallbackSubscriber<InsertOneResult> subscriber = new CallbackSubscriber<>();

        collection.insertOne(dbObject).subscribe(subscriber);

        if (dbObject != null) {
            subscriber.whenComplete(result -> {
                final BsonValue insertedId = result.getInsertedId();

                if (insertedId != null) {
                    this.cacheManager.addCachedData(this.cachedDataKey + insertedId.asObjectId().getValue().toString(), new CachedDBObject(dbObject), true);
                }
            });
        }
    }

    protected void removeData(MongoCollection<BasicDBObject> collection, String key, String value) {
        final DataCallbackSubscriber subscriber = new DataCallbackSubscriber();

        collection.findOneAndDelete(Filters.eq(key, value)).subscribe(subscriber);

        subscriber.whenComplete(dbObject -> {
            if (dbObject != null) {
                this.cacheManager.removeCachedData(this.cachedDataKey + dbObject.getObjectId(ID).toString());
            }
        });
    }

    protected void updateData(MongoCollection<BasicDBObject> collection, String key, String value, BasicDBObject dbObject) {
        final DataCallbackSubscriber subscriber = new DataCallbackSubscriber();

        collection.findOneAndReplace(Filters.eq(key, value), dbObject).subscribe(subscriber);

        if (dbObject != null) {
            subscriber.whenComplete(result -> {
                if (result != null) {
                    this.cacheManager.addCachedData(this.cachedDataKey + result.getObjectId(ID).toString(), new CachedDBObject(dbObject), true);
                }
            });
        }
    }

    protected BasicDBObject getData(MongoCollection<BasicDBObject> collection, String key, String value) {
        final CachedDBObject cachedData = this.cacheManager.getCachedDBObject(this.cachedDataKey, key, value);

        if (cachedData != null) {
            final BasicDBObject dbObject = cachedData.getValue();

            if (dbObject != null) {
                return dbObject;
            }
        }

        final DataSubscriber subscriber = new DataSubscriber();

        collection.find(Filters.eq(key, value))
                .first()
                .subscribe(subscriber);

        final BasicDBObject dbObject = subscriber.get();

        if (dbObject != null) {
            this.cacheManager.addCachedData(key, dbObject);
        }

        return dbObject;
    }

    protected List<BasicDBObject> getAllData(MongoCollection<BasicDBObject> collection, String key) {
        final CachedDBObjectList cachedData = this.cacheManager.getCachedDBObjectList(key);

        if (cachedData != null) {
            final List<BasicDBObject> dbObjects = cachedData.getValue();

            if (dbObjects != null) {
                return dbObjects;
            }
        }

        final DataSubscriber subscriber = new DataSubscriber();

        collection.find().subscribe(subscriber);

        final List<BasicDBObject> dbObjects = subscriber.getAll();

        this.cacheManager.addCachedData(key, new CachedDBObjectList(dbObjects), true);

        return dbObjects;
    }

    protected void updateAllCachedData(String key, List<BasicDBObject> dbObjects) {
        final CachedDBObjectList cachedData = this.cacheManager.getCachedDBObjectList(key);

        if (cachedData != null) {
            this.cacheManager.addCachedData(key, new CachedDBObjectList(dbObjects), true);
        }
    }

}
