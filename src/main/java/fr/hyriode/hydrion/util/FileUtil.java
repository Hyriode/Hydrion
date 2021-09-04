package fr.hyriode.hydrion.util;

import java.io.*;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:55
 */
public class FileUtil {

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

    public static String loadFile(File file) {
        final StringBuilder sb = new StringBuilder();
        if (file.exists()) {
            try {
                String line;
                final BufferedReader reader = new BufferedReader(new FileReader(file));

                while ((line = reader.readLine()) != null) sb.append(line);

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void save(File file, String content) {
        try {
            if (file.getParentFile() != null) file.getParentFile().mkdirs();
            file.createNewFile();

            final FileWriter writer = new FileWriter(file);

            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
