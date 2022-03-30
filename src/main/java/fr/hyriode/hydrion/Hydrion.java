package fr.hyriode.hydrion;

import com.google.gson.Gson;
import fr.hyriode.hydrion.configuration.Configuration;
import fr.hyriode.hydrion.configuration.ConfigurationManager;
import fr.hyriode.hydrion.handler.player.PlayerHandler;
import fr.hyriode.hydrion.network.http.HttpRouter;
import fr.hyriode.hydrion.util.logger.HydrionLogger;
import fr.hyriode.hydrion.util.logger.LoggingOutputStream;
import fr.hyriode.hydrion.network.NetworkManager;
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
    public static final Gson GSON = new Gson();

    /** Network */
    private NetworkManager networkManager;

    /** Configuration */
    private ConfigurationManager configurationManager;
    private Configuration configuration;

    /** Logger */
    private ConsoleReader consoleReader;
    private static HydrionLogger logger;

    /** Global information */
    private boolean running;

    public void start() {
        HydrionLogger.printHeaderMessage();

        this.setupLogger();

        System.out.println("Starting " + NAME + "...");

        this.configurationManager = new ConfigurationManager();
        this.configuration = this.configurationManager.loadConfiguration();

        this.networkManager = new NetworkManager(this);

        this.registerHandlers();

        this.running = true;

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        this.networkManager.start();
    }

    private void registerHandlers() {
        final HttpRouter router = this.networkManager.getServer().getRouter();

        router.addHandler("/player", new PlayerHandler());
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

    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    public static Logger getLogger() {
        return logger;
    }

}
