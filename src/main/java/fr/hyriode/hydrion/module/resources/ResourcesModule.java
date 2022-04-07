package fr.hyriode.hydrion.module.resources;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.module.AbstractModule;
import fr.hyriode.hydrion.module.resources.game.GameHandler;
import fr.hyriode.hydrion.module.resources.game.GamesHandler;
import fr.hyriode.hydrion.network.http.HttpRouter;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 18:32
 */
public class ResourcesModule extends AbstractModule {

    private static final String NAME_KEY = "name";

    private static final String NAME = "resources";
    private static final String PATH = "/" + NAME + "/";

    private MongoCollection<BasicDBObject> gamesCollection;

    private MongoDatabase database;

    public ResourcesModule(Hydrion hydrion) {
        super(hydrion);
    }

    protected void init() {
        final HttpRouter router = this.hydrion.getNetworkManager().getServer().getRouter();

        router.addHandler(PATH + "games", new GamesHandler(this.hydrion, this));
        router.addHandler(PATH + "game", new GameHandler(this.hydrion, this));

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
