package fr.hyriode.hydrion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.hydrion.cache.CacheManager;
import fr.hyriode.hydrion.configuration.Configuration;
import fr.hyriode.hydrion.configuration.ConfigurationManager;
import fr.hyriode.hydrion.database.mongodb.MongoDB;
import fr.hyriode.hydrion.module.friends.FriendsModule;
import fr.hyriode.hydrion.module.network.NetworkModule;
import fr.hyriode.hydrion.module.player.PlayerModule;
import fr.hyriode.hydrion.module.resources.ResourcesModule;
import fr.hyriode.hydrion.network.NetworkManager;
import fr.hyriode.hydrion.util.logger.HydrionLogger;
import fr.hyriode.hydrion.util.logger.LoggingOutputStream;
import jline.console.ConsoleReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 01/09/2021 at 21:16
 */
public class Hydrion {

    public static final String NAME = "Hydrion";
    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();

    /** Logger */
    private ConsoleReader consoleReader;
    private static HydrionLogger logger;

    /** Configuration */
    private ConfigurationManager configurationManager;
    private Configuration configuration;

    /** Databases */
    private MongoDB mongoDB;

    /** Cache */
    private CacheManager cacheManager;

    /** Network */
    private NetworkManager networkManager;

    /** Modules */
    private PlayerModule playerModule;
    private FriendsModule friendsModule;
    private ResourcesModule resourcesModule;
    private NetworkModule networkModule;

    /** Global information */
    private boolean running;

    public void start() {
        HydrionLogger.printHeaderMessage();

        this.setupLogger();

        System.out.println("Starting " + NAME + "...");

        this.configurationManager = new ConfigurationManager();
        this.configuration = this.configurationManager.loadConfiguration();
        this.mongoDB = new MongoDB(this.configuration.getMongoDBUrl());
        this.mongoDB.start();
        this.cacheManager = new CacheManager();

        this.networkManager = new NetworkManager(this);
        this.playerModule = new PlayerModule(this);
        this.friendsModule = new FriendsModule(this);
        this.resourcesModule = new ResourcesModule(this);
        this.networkModule = new NetworkModule(this);

        this.running = true;

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        this.networkManager.start();
    }

    private void setupLogger() {
        try {
            this.consoleReader = new ConsoleReader();
            this.consoleReader.setExpandEvents(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!new File("logs/").mkdirs()) {
            logger = new HydrionLogger(this, NAME, "logs/hydrion.log");

            System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
            System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
        }
    }

    public void stop() {
        if (!this.running) {
            return;
        }

        this.running = false;

        this.networkManager.shutdown();

        this.mongoDB.stop();

        System.out.println(NAME + " is now down. See you soon!");
    }

    public boolean isRunning() {
        return this.running;
    }

    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public MongoDB getMongoDB() {
        return this.mongoDB;
    }

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    public PlayerModule getPlayerModule() {
        return this.playerModule;
    }

    public FriendsModule getFriendsModule() {
        return this.friendsModule;
    }

    public ResourcesModule getResourcesModule() {
        return this.resourcesModule;
    }

    public NetworkModule getNetworkModule() {
        return this.networkModule;
    }

    public static Logger getLogger() {
        return logger;
    }

}
