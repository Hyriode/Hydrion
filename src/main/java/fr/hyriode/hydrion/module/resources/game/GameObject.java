package fr.hyriode.hydrion.module.resources.game;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.object.HydrionObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 19:46
 */
public class GameObject extends HydrionObject {

    private static final String NAME = "name";

    private Map<String, BasicDBObject> game;

    public GameObject(BasicDBObject game) {
        if (game != null) {
            this.game = new HashMap<>();
            this.game.put(game.getString(NAME), game);

            game.removeField(ID_KEY);
            game.removeField(NAME);
        }
    }

}
