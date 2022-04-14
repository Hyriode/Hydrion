package fr.hyriode.hydrion.module.uuid;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.object.HydrionObject;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 14/04/2022 at 11:04
 */
public class UUIDObject extends HydrionObject {

    private final BasicDBObject uuid;

    public UUIDObject(BasicDBObject uuid) {
        this.uuid = uuid;

        if (this.uuid != null) {
            this.uuid.removeField(ID_KEY);
        }
    }

    public BasicDBObject getUUID() {
        return this.uuid;
    }

}
