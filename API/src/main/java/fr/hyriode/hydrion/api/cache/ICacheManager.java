package fr.hyriode.hydrion.api.cache;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 18:57
 */
public interface ICacheManager {

    void addCachedData(String key, CachedData<?> data, boolean replace);

    void addCachedData(String key, Object value, boolean replace);

    void addCachedData(String key, Object value);

    void removeCachedData(String key);

    CachedData<?> getCachedData(String key);

    CachedDBObject getCachedDBObject(String key, String value);

    CachedDBObjectList getCachedDBObjectList(String key);

}
