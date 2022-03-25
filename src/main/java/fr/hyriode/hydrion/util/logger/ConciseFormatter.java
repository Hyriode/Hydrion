package fr.hyriode.hydrion.util.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 10:05c
 */
public class ConciseFormatter extends Formatter {

    private final Logger logger;
    private final boolean colored;

    public ConciseFormatter(Logger logger, boolean colored) {
        this.logger = logger;
        this.colored = colored;
    }

    @Override
    @SuppressWarnings("ThrowableResultIgnored")
    public String format(LogRecord record) {
        final StringBuilder formatted = new StringBuilder();

        formatted.append("[")
                .append(new SimpleDateFormat("hh:mm:ss").format(new Date(record.getMillis())))
                .append("] [");

        this.appendLevel(formatted, record.getLevel());

        formatted.append("] ")
                .append(this.formatMessage(record))
                .append("\n");

        if (record.getThrown() != null) {
            final StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            formatted.append(writer);
        }

        return formatted.toString();
    }

    private void appendLevel(StringBuilder builder, Level level) {
        if (this.colored) {

           LogColor color;
            if (level == Level.INFO) {
                color = LogColor.AQUA;
            } else if (level == Level.WARNING) {
                color = LogColor.YELLOW;
            } else if (level == Level.SEVERE) {
                color = LogColor.RED;
            } else {
                color = LogColor.BLUE;
            }

            builder.append(color).append(level.getLocalizedName()).append(LogColor.RESET);
        } else {
            builder.append(level.getLocalizedName());
        }
    }
}
