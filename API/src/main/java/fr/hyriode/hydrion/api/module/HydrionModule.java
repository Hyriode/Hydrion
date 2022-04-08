package fr.hyriode.hydrion.api.module;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.cache.CachedData;
import fr.hyriode.hydrion.api.cache.ICacheManager;
import fr.hyriode.hydrion.api.database.mongodb.MongoDB;
import fr.hyriode.hydrion.api.database.mongodb.subscriber.DataSubscriber;
import fr.hyriode.hydrion.api.database.mongodb.subscriber.OperationSubscriber;
import fr.hyriode.hydrion.api.handler.HydrionHandler;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 19:21
 */
public abstract class HydrionModule {

    protected final MongoDB mongoDB;
    protected final ICacheManager cacheManager;

    public HydrionModule() {
        final HydrionAPI api = HydrionAPI.get();

        this.mongoDB = api.getMongoDB();
        this.cacheManager = api.getCacheManager();

        this.init();
    }

    protected abstract void init();

    protected void addHandler(String path, HydrionHandler handler) {
        HydrionAPI.get().getNetworkManager().addHandler(path, handler);
    }

    protected void addData(MongoCollection<BasicDBObject> collection, Object key, BasicDBObject dbObject) {
        collection.insertOne(dbObject).subscribe(new OperationSubscriber<>());

        if (dbObject != null) {
            this.cacheManager.addCachedData(key, dbObject, true);
        }
    }

    protected void removeData(MongoCollection<BasicDBObject> collection, String filter, Object key) {
        collection.deleteOne(Filters.eq(filter, key.toString())).subscribe(new OperationSubscriber<>());

        this.cacheManager.removeCachedData(key);
    }

    protected void updateData(MongoCollection<BasicDBObject> collection, String filter, Object key, BasicDBObject dbObject) {
        collection.replaceOne(Filters.eq(filter, key.toString()), dbObject).subscribe(new OperationSubscriber<>());

        if (dbObject != null) {
            this.cacheManager.addCachedData(key, dbObject, true);
        }
    }

    protected BasicDBObject getData(MongoCollection<BasicDBObject> collection, String filter, Object key) {
        final CachedData cachedData = this.cacheManager.getCachedData(key);

        if (cachedData != null) {
            final BasicDBObject dbObject = cachedData.getDBObject();

            if (dbObject != null) {
                return dbObject;
            }
        }

        final DataSubscriber subscriber = new DataSubscriber();

        collection.find(Filters.eq(filter, key.toString()))
                .first()
                .subscribe(subscriber);

        final BasicDBObject dbObject = subscriber.get();

        if (dbObject != null) {
            this.cacheManager.addCachedData(key, dbObject);
        }

        return dbObject;
    }

    protected List<BasicDBObject> getAllData(MongoCollection<BasicDBObject> collection) {
        final DataSubscriber subscriber = new DataSubscriber();

        collection.find().subscribe(subscriber);

        return subscriber.getAll();
    }

}
