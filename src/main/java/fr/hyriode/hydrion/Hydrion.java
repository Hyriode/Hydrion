package fr.hyriode.hydrion;

import fr.hyriode.hydrion.command.CommandManager;
import fr.hyriode.hydrion.configuration.Configuration;
import fr.hyriode.hydrion.configuration.ConfigurationManager;
import fr.hyriode.hydrion.logger.HydrionLogger;
import fr.hyriode.hydrion.logger.LoggingOutputStream;
import fr.hyriode.hydrion.network.NetworkManager;
import fr.hyriode.hydrion.util.References;
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

    /** Network */
    private NetworkManager networkManager;

    /** Command */
    private CommandManager commandManager;

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

        System.out.println("Starting " + References.NAME + "...");

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        this.run();
    }

    private void setupLogger() {
        this.setupConsoleReader();

        if (!new File("logs/").mkdirs()) {
            logger = new HydrionLogger(this, References.NAME, "logs/hydrion.log");

            System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
            System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
        }
    }

    private void setupConsoleReader() {
        try {
            this.consoleReader = new ConsoleReader();
            this.consoleReader.setExpandEvents(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        this.running = true;

        this.configurationManager = new ConfigurationManager(new File("config.json"));
        this.configuration = this.configurationManager.loadConfiguration();

        this.commandManager = new CommandManager(this);
        this.commandManager.start();

        this.networkManager = new NetworkManager(this);
        this.networkManager.start();
    }

    public void stop() {
        this.running = false;

        this.commandManager.shutdown();
        this.networkManager.shutdown();

        System.out.println(References.NAME + " is now down. See you soon!");

        this.waitLogger();
    }

    private void waitLogger() {
        try {
            if (!logger.getDispatcher().getQueue().isEmpty()) {
                Thread.sleep(500);
                this.waitLogger();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    public static Logger getLogger() {
        return logger;
    }

}
