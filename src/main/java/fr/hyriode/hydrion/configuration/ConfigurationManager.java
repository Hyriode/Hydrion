package fr.hyriode.hydrion.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.hydrion.util.IOUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:52
 */
public class ConfigurationManager {

    public static final Path CONFIG_FILE = Paths.get("config.json");

    private Configuration configuration;

    public Configuration loadConfiguration() {
        System.out.println("Loading configuration...");

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        final String json = IOUtil.loadFile(CONFIG_FILE);

        if (!json.equals("")) {
            return this.configuration = gson.fromJson(json, Configuration.class);
        } else {
            this.configuration = new Configuration(8080);
            IOUtil.save(CONFIG_FILE, gson.toJson(configuration));

            System.err.println("Please fill configuration file before continue!");
            System.exit(0);

            return this.configuration;
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

}
