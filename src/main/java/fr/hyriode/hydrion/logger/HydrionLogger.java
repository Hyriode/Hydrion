package fr.hyriode.hydrion.logger;

import fr.hyriode.hydrion.Hydrion;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 10:05
 */
public class HydrionLogger extends Logger {

    private final LogDispatcher dispatcher = new LogDispatcher(this);

    @SuppressWarnings( { "CallToPrintStackTrace", "CallToThreadStartDuringObjectConstruction" } )
    public HydrionLogger(Hydrion hydrion, String name, String filePattern) {
        super(name, null);
        this.setLevel(Level.ALL);

        try {
            final FileHandler fileHandler = new FileHandler(filePattern);
            fileHandler.setFormatter(new ConciseFormatter(this, false));

            this.addHandler(fileHandler);

            final ColouredWriter consoleHandler = new ColouredWriter(hydrion.getConsoleReader());
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new ConciseFormatter(this, true));

            this.addHandler(consoleHandler);
        } catch (IOException e) {
            System.err.println("Couldn't register logger !");

            hydrion.stop();
        }

        this.dispatcher.start();
    }

    @Override
    public void log(LogRecord record) {
        this.dispatcher.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
    }

    public static void printHeaderMessage() {
        final String text = "" +
                "██╗  ██╗██╗   ██╗██████╗ ██████╗ ██╗ ██████╗ ███╗   ██╗\n" +
                "██║  ██║╚██╗ ██╔╝██╔══██╗██╔══██╗██║██╔═══██╗████╗  ██║\n" +
                "███████║ ╚████╔╝ ██║  ██║██████╔╝██║██║   ██║██╔██╗ ██║\n" +
                "██╔══██║  ╚██╔╝  ██║  ██║██╔══██╗██║██║   ██║██║╚██╗██║\n" +
                "██║  ██║   ██║   ██████╔╝██║  ██║██║╚██████╔╝██║ ╚████║\n" +
                "╚═╝  ╚═╝   ╚═╝   ╚═════╝ ╚═╝  ╚═╝╚═╝ ╚═════╝ ╚═╝  ╚═══╝";

        System.out.println(text);
    }

    public LogDispatcher getDispatcher() {
        return this.dispatcher;
    }

}
