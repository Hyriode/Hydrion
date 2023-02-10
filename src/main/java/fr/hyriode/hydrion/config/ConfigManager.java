package fr.hyriode.hydrion.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.api.config.MongoDBConfig;
import fr.hyriode.api.config.RedisConfig;
import fr.hyriode.hydrion.util.IOUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:52
 */
public class ConfigManager {

    public static final Path CONFIG_FILE = Paths.get("config.json");

    private Config config;

    public Config loadConfiguration() {
        System.out.println("Loading configuration...");

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        final String json = IOUtil.loadFile(CONFIG_FILE);

        if (!json.equals("")) {
            return this.config = gson.fromJson(json, Config.class);
        } else {
            this.config = new Config(8080, new RedisConfig("localhost", 6378, "p@ssw0rd"), new MongoDBConfig("user", "p@ssw0rd", "localhost", 27017), UUID.randomUUID());

            IOUtil.save(CONFIG_FILE, gson.toJson(config));

            System.err.println("Please fill configuration file before continue!");
            System.exit(0);

            return this.config;
        }
    }

    public Config getConfiguration() {
        return this.config;
    }

}
