package fr.hyriode.hydrion.module.boosters;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.object.HydrionObject;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 21:00
 */
public class BoosterObject extends HydrionObject {

    private final BasicDBObject booster;

    public BoosterObject(BasicDBObject booster) {
        this.booster = booster;

        if (this.booster != null) {
            this.booster.removeField(ID_KEY);
        }
    }

    public BasicDBObject getBooster() {
        return this.booster;
    }

}
