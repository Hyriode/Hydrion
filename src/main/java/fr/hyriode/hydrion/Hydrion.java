package fr.hyriode.hydrion;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.application.HyriAPIImpl;
import fr.hyriode.api.impl.application.config.HyriAPIConfig;
import fr.hyriode.hydrion.config.Config;
import fr.hyriode.hydrion.config.ConfigManager;
import fr.hyriode.hydrion.extension.ExtensionManager;
import fr.hyriode.hydrion.network.NetworkManager;
import fr.hyriode.hydrion.util.IOUtil;
import fr.hyriode.hydrion.util.logger.ColoredLogger;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 01/09/2021 at 21:16
 */
public class Hydrion {

    public static final String NAME = "Hydrion";

    /** Logger */
    private ConsoleReader consoleReader;
    private static ColoredLogger logger;

    /** Configuration */
    private ConfigManager configManager;
    private Config config;

    /** Network */
    private NetworkManager networkManager;

    /** API */
    private HydrionImpl api;
    private HyriAPI hyriAPI;

    /** Extension */
    private ExtensionManager extensionManager;

    /** Global information */
    private boolean running;

    public void start() {
        ColoredLogger.printHeaderMessage();

        this.setupLogger();

        System.out.println("Starting " + NAME + "...");

        this.configManager = new ConfigManager();
        this.config = this.configManager.loadConfiguration();
        this.api = new HydrionImpl(this);
        this.hyriAPI = new HyriAPIImpl(new HyriAPIConfig.Builder()
                .withDevEnvironment(false)
                .withHyggdrasil(false)
                .withRedisConfig(this.config.getRedisConfig())
                .withMongoDBConfig(this.config.getMongoDBConfig())
                .build(), NAME);
        this.networkManager = new NetworkManager(this);
        this.extensionManager = new ExtensionManager();
        this.extensionManager.start();

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

        IOUtil.createDirectory(Paths.get("logs"));

        logger = new ColoredLogger(NAME, Paths.get("logs/hydrion.log"));
    }

    public void stop() {
        if (!this.running) {
            return;
        }

        this.running = false;

        this.extensionManager.disableExtensions();
        this.networkManager.shutdown();

        System.out.println(NAME + " is now down. See you soon!");
    }

    public boolean isRunning() {
        return this.running;
    }

    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    public ConfigManager getConfigurationManager() {
        return this.configManager;
    }

    public Config getConfiguration() {
        return this.config;
    }

    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    public HydrionImpl getAPI() {
        return this.api;
    }

    public HyriAPI getHyriAPI() {
        return this.hyriAPI;
    }

    public static Logger getLogger() {
        return logger;
    }

}
