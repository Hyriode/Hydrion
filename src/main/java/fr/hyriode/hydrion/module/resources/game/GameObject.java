package fr.hyriode.hydrion.module.resources.game;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.api.object.HydrionObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 19:46
 */
public class GameObject extends HydrionObject {

    private BasicDBObject game;

    public GameObject(BasicDBObject game) {
        if (game != null) {
            this.game = game;

            game.removeField(ID_KEY);
        }
    }

    public BasicDBObject getGame() {
        return this.game;
    }

}
