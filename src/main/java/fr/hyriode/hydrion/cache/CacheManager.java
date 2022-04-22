package fr.hyriode.hydrion.cache;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.cache.CachedDBObject;
import fr.hyriode.hydrion.api.cache.CachedDBObjectList;
import fr.hyriode.hydrion.api.cache.CachedData;
import fr.hyriode.hydrion.api.cache.ICacheManager;

import java.util.List;
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

    private static final int EXPIRE_TIME = 600;

    private final ScheduledExecutorService scheduler;
    private final Map<String, CachedData<?>> cachedData;

    public CacheManager() {
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.cachedData = new ConcurrentHashMap<>();
    }

    @Override
    public void addCachedData(String key, CachedData<?> data, boolean replace) {
        final CachedData<?> oldData = this.getCachedData(key);

        if (oldData != null && replace) {
            this.removeCachedData(key);
        }

        data.setTask(this.scheduler.schedule(() -> {
            this.cachedData.remove(key);
        }, data.getExpirationTime(), TimeUnit.SECONDS));

        this.cachedData.put(key, data);
    }

    @Override
    public void addCachedData(String key, Object object, boolean replace) {
        this.addCachedData(key, new CachedData<>(EXPIRE_TIME, object), replace);
    }

    @Override
    public void addCachedData(String key, Object object) {
        this.addCachedData(key, new CachedData<>(EXPIRE_TIME, object), false);
    }

    @Override
    public void removeCachedData(String key) {
        if (key != null) {
            this.cachedData.remove(key).getTask().cancel(true);
        }
    }

    @Override
    public CachedData<?> getCachedData(String key) {
        return this.cachedData.get(key);
    }

    @Override
    public CachedDBObject getCachedDBObject(String cachedDataKey, String key, String value) {
        for (Map.Entry<String, CachedData<?>> entry : this.cachedData.entrySet()) {
            if (entry.getKey().startsWith(cachedDataKey)) {
                final CachedData<?> data = entry.getValue();

                if (data instanceof final CachedDBObject cachedData) {
                    final BasicDBObject dbObject = cachedData.getValue();

                    if (dbObject != null) {
                        final Object object = dbObject.get(key);

                        if (object != null && object.toString().equals(value)) {
                            return cachedData;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public CachedDBObjectList getCachedDBObjectList(String key) {
        final CachedData<?> data = this.cachedData.get(key);

        if (data instanceof final CachedDBObjectList cachedData) {
            return cachedData;
        }
        return null;
    }

}
