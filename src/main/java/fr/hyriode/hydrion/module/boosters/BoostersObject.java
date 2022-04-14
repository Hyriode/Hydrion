package fr.hyriode.hydrion.module.boosters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fr.hyriode.hydrion.api.object.HydrionObject;

import java.util.List;
import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 21:00
 */
public class BoostersObject extends HydrionObject {

    private final List<BasicDBObject> boosters;

    public BoostersObject(List<BasicDBObject> boosters) {
        this.boosters = boosters;

        if (this.boosters != null) {
            for (BasicDBObject booster : this.boosters) {
                booster.removeField(ID_KEY);
            }
        }
    }

    public List<BasicDBObject> getBoosters() {
        return this.boosters;
    }

}
