package fr.hyriode.hydrion.api.cache;

import com.mongodb.BasicDBObject;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 14/04/2022 at 09:39
 */
public class CachedDBObjectList extends CachedData<List<BasicDBObject>> {

    public CachedDBObjectList(List<BasicDBObject> value) {
        super(value);
    }

}
