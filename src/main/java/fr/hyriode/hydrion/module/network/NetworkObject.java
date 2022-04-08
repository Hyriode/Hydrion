package fr.hyriode.hydrion.module.network;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.object.HydrionObject;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 07/04/2022 at 21:06
 */
public class NetworkObject extends HydrionObject {

    private final BasicDBObject network;

    public NetworkObject(BasicDBObject network) {
        this.network = network;

        if (this.network != null) {
            this.network.removeField(ID_KEY);
        }
    }

}
