package fr.hyriode.hydrion.module.resources;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.hydrion.api.module.HydrionModule;
import fr.hyriode.hydrion.module.resources.game.GameHandler;
import fr.hyriode.hydrion.module.resources.game.GamesHandler;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 18:32
 */
public class ResourcesModule extends HydrionModule {

    private static final String NAME_KEY = "name";

    private static final String NAME = "resources";
    private static final String PATH = "/" + NAME + "/";

    private MongoCollection<BasicDBObject> gamesCollection;

    private MongoDatabase database;

    protected void init() {
        this.addHandler(PATH + "games", new GamesHandler(this));
        this.addHandler(PATH + "game", new GameHandler(this));

        this.database = this.mongoDB.getDatabase(NAME);
        this.gamesCollection = this.database.getCollection("games", BasicDBObject.class);
    }

    public void addGame(String name, BasicDBObject dbObject) {
        this.addData(this.gamesCollection, name, dbObject);
    }

    public void removeGame(String name) {
        this.removeData(this.gamesCollection, NAME_KEY, name);
    }

    public void updateGame(String name, BasicDBObject dbObject) {
        this.updateData(this.gamesCollection, NAME_KEY, name, dbObject);
    }

    public BasicDBObject getGame(String name) {
        return this.getData(this.gamesCollection, NAME_KEY, name);
    }

    public List<BasicDBObject> getGames() {
        return this.getAllData(this.gamesCollection);
    }

    public MongoCollection<BasicDBObject> getGamesCollection() {
        return this.gamesCollection;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

}
