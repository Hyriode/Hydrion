package fr.hyriode.hydrion.api.cache;

import com.mongodb.BasicDBObject;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 18:57
 */
public interface ICacheManager {

    void addCachedData(Object key, CachedData data, boolean replace);

    void addCachedData(Object key, BasicDBObject dbObject, boolean replace);

    void addCachedData(Object key, BasicDBObject dbObject);

    void removeCachedData(Object key);

    CachedData getCachedData(Object key);

}
