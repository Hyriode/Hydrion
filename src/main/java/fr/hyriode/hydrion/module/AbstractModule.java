package fr.hyriode.hydrion.module;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.cache.CacheManager;
import fr.hyriode.hydrion.cache.CachedData;
import fr.hyriode.hydrion.database.mongodb.MongoDB;
import fr.hyriode.hydrion.database.mongodb.subscriber.DataSubscriber;
import fr.hyriode.hydrion.database.mongodb.subscriber.OperationSubscriber;
import fr.hyriode.hydrion.network.http.HttpRouter;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 19:21
 */
public abstract class AbstractModule {

    protected final MongoDB mongoDB;
    protected final CacheManager cacheManager;
    protected final HttpRouter router;

    protected final Hydrion hydrion;

    public AbstractModule(Hydrion hydrion) {
        this.hydrion = hydrion;
        this.mongoDB = this.hydrion.getMongoDB();
        this.cacheManager = this.hydrion.getCacheManager();
        this.router = this.hydrion.getNetworkManager().getServer().getRouter();

        this.init();
    }

    protected abstract void init();

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
