package fr.hyriode.hydrion.util.logger;

import com.google.common.base.Charsets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 10:05
 */
public class LoggingOutputStream extends ByteArrayOutputStream {

    private final Logger logger;
    private final Level level;

    public LoggingOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void flush() throws IOException {
        final String contents = this.toString(Charsets.UTF_8.name());

        super.reset();

        if (!contents.isEmpty() && !contents.equals(System.getProperty("line.separator"))) {
            this.logger.logp(level, "", "", contents.substring(0, contents.length() - 1));
        }
    }

}
