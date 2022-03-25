package fr.hyriode.hydrion.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:55
 */
public class IOUtil {

    public static boolean exist(File folder, String name) {
        boolean exist = false;
        final File[] list = folder.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.getName().equals(name)) {
                    exist = true;
                    break;
                }
            }
        }
        return exist;
    }

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

    public static void save(Path path, String content) {
        try {
            Files.createFile(path);
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}