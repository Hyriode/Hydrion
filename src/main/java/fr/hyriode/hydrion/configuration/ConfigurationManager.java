package fr.hyriode.hydrion.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.hydrion.util.FileUtil;

import java.io.File;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:52
 */
public class ConfigurationManager {

    private Configuration configuration;

    private final File configFile;

    public ConfigurationManager(File configFile) {
        this.configFile = configFile;
    }

    public Configuration loadConfiguration() {
        System.out.println("Loading configuration...");

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        final String json = FileUtil.loadFile(this.configFile);

        if (!json.equals("")) {
            return this.configuration = gson.fromJson(json, Configuration.class);
        } else {
            this.configuration = new Configuration("localhost", 8080);
            FileUtil.save(this.configFile, gson.toJson(configuration));

            System.err.println("Please fill configuration file before continue !");
            System.exit(0);

            return this.configuration;
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

}
