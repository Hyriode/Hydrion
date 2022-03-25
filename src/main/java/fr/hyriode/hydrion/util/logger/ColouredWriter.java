package fr.hyriode.hydrion.util.logger;

import jline.console.ConsoleReader;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 10:05
 */
public class ColouredWriter extends Handler {

    private final Map<LogColor, String> replacements = new EnumMap<>(LogColor.class);
    private final LogColor[] colors = LogColor.values();
    private final ConsoleReader console;

    public ColouredWriter(ConsoleReader console) {
        this.console = console;

        this.replacements.put(LogColor.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        this.replacements.put(LogColor.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        this.replacements.put(LogColor.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        this.replacements.put(LogColor.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        this.replacements.put(LogColor.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        this.replacements.put(LogColor.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        this.replacements.put(LogColor.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        this.replacements.put(LogColor.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        this.replacements.put(LogColor.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        this.replacements.put(LogColor.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        this.replacements.put(LogColor.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        this.replacements.put(LogColor.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        this.replacements.put(LogColor.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        this.replacements.put(LogColor.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        this.replacements.put(LogColor.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        this.replacements.put(LogColor.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        this.replacements.put(LogColor.MAGIC, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
        this.replacements.put(LogColor.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
        this.replacements.put(LogColor.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
        this.replacements.put(LogColor.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
        this.replacements.put(LogColor.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
        this.replacements.put(LogColor.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());
    }

    public void print(String s) {
        for (LogColor color : this.colors) {
            s = s.replaceAll("(?i)" + color, this.replacements.get(color));
        }

        try {
            this.console.print(ConsoleReader.RESET_LINE + s + Ansi.ansi().reset());
            this.console.drawLine();
            this.console.flush();
        } catch (IOException ex) {}
    }

    @Override
    public void publish(LogRecord record) {
        if (this.isLoggable(record)) {
            this.print(this.getFormatter().format(record));
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}

}
