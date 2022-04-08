package fr.hyriode.hydrion.cache;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.cache.CachedData;
import fr.hyriode.hydrion.api.cache.ICacheManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 06/04/2022 at 17:44
 */
public class CacheManager implements ICacheManager {

    private static final int EXPIRE_TIME = 300;

    private final ScheduledExecutorService scheduler;
    private final Map<Object, CachedData> cachedData;

    public CacheManager() {
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.cachedData = new ConcurrentHashMap<>();
    }

    @Override
    public void addCachedData(Object key, CachedData data, boolean replace) {
        final CachedData oldData = this.getCachedData(key);

        if (oldData != null && replace) {
            this.removeCachedData(key);
        }

        data.setTask(this.scheduler.schedule(() -> {
            this.cachedData.remove(key);
        }, data.getExpireTime(), TimeUnit.SECONDS));

        this.cachedData.put(key, data);
    }

    @Override
    public void addCachedData(Object key, BasicDBObject dbObject, boolean replace) {
        this.addCachedData(key, new CachedData(EXPIRE_TIME, dbObject), replace);
    }

    @Override
    public void addCachedData(Object key, BasicDBObject dbObject) {
        this.addCachedData(key, new CachedData(EXPIRE_TIME, dbObject), false);
    }

    @Override
    public void removeCachedData(Object key) {
        if (key != null) {
            this.cachedData.remove(key).getTask().cancel(true);
        }
    }

    @Override
    public CachedData getCachedData(Object key) {
        return this.cachedData.get(key);
    }

}
