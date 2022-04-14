package fr.hyriode.hydrion.extension;

import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.api.extension.HydrionExtension;
import fr.hyriode.hydrion.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 20:18
 */
public class ExtensionManager {

    private static final String MAIN_CLASS = "Extension-Main-Class";
    private static final Path FOLDER = Paths.get(Hydrion.DATA_FOLDER.toString(), "extensions");

    private final List<ExtensionMeta> extensionMetas;
    private final List<HydrionExtension> extensions;

    public ExtensionManager() {
        this.extensionMetas = new ArrayList<>();
        this.extensions = new ArrayList<>();
    }

    public void start() {
        IOUtil.createDirectory(FOLDER);

        this.detectExtensions();
        this.loadExtensions();
    }

    private void detectExtensions() {
        try {
            Files.list(FOLDER).forEach(path -> {
                final File file = path.toFile();

                if (file.isFile() && file.getName().endsWith(".jar")) {
                    try (final JarFile jar = new JarFile(file)) {
                        this.extensionMetas.add(new ExtensionMeta(file, jar.getManifest().getMainAttributes().getValue(MAIN_CLASS)));
                    } catch (IOException e) {
                        System.err.println("Couldn't load extension from file: " + file.getName());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadExtension(ExtensionMeta meta) {
        try {
            final URLClassLoader classLoader = new URLClassLoader(new URL[]{meta.getFile().toURI().toURL()});
            final Class<?> extensionClass = classLoader.loadClass(meta.getMainClass());
            final HydrionExtension extension = (HydrionExtension) extensionClass.getDeclaredConstructor().newInstance();
            final String name = extension.getName();

            meta.setName(name);

            this.extensions.add(extension);

            System.out.println("Enabling '" + name + "' extension.");

            extension.onEnable();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private void loadExtensions() {
        for (ExtensionMeta meta : this.extensionMetas) {
            this.loadExtension(meta);
        }
    }

    public void disableExtensions() {
        for (HydrionExtension extension : this.extensions) {
            System.out.println("Disabling '" + extension.getName() + "' extension...");

            extension.onDisable();
        }
    }

}
