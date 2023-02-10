package fr.hyriode.hydrion.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:55
 */
public class IOUtil {

    public static String loadFile(Path path) {
        final StringBuilder sb = new StringBuilder();

        if (Files.exists(path)) {
            try (final BufferedReader reader = Files.newBufferedReader(path)) {
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(Path path, String content) {
        try {
            Files.createFile(path);
            Files.writeString(path, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
