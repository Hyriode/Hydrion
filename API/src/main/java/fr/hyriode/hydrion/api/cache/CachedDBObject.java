package fr.hyriode.hydrion.api.cache;

import com.mongodb.BasicDBObject;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 14/04/2022 at 09:38
 */
public class CachedDBObject extends CachedData<BasicDBObject> {

    public CachedDBObject(BasicDBObject value) {
        super(value);
    }

}
