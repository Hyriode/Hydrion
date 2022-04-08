package fr.hyriode.hydrion.module.player;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fr.hyriode.hydrion.api.object.HydrionObject;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 19:08
 */
public class PlayerObject extends HydrionObject {

    private final DBObject player;

    public PlayerObject(BasicDBObject dbObject) {
        this.player = dbObject;

        if (this.player != null) {
            this.player.removeField(ID_KEY);
        }
    }

    public DBObject getPlayer() {
        return this.player;
    }

}
