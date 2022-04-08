package fr.hyriode.hydrion.module.resources.game;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fr.hyriode.hydrion.api.object.HydrionObject;

import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 21:36
 */
public class GamesObject extends HydrionObject {

    private final Map<String, BasicDBObject> games;

    public GamesObject(Map<String, BasicDBObject> games) {
        this.games = games;

        for (DBObject object : this.games.values()) {
            object.removeField(ID_KEY);
        }
    }

    public Map<String, ? extends DBObject> getGames() {
        return this.games;
    }

}
