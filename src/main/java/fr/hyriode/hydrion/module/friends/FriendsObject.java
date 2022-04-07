package fr.hyriode.hydrion.module.friends;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fr.hyriode.hydrion.object.HydrionObject;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 21:00
 */
public class FriendsObject extends HydrionObject {

    private final UUID uuid;
    private final BasicDBObject friends;

    public FriendsObject(UUID uuid, BasicDBObject friends) {
        this.uuid = uuid;
        this.friends = friends;

        if (this.friends != null) {
            this.friends.removeField(ID_KEY);
        }
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public DBObject getFriends() {
        return this.friends;
    }

}
