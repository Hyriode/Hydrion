package fr.hyriode.hydrion.extension;

import java.io.File;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 20:26
 */
public class ExtensionMeta {

    private String name;

    private final File file;
    private final String mainClass;

    public ExtensionMeta(File file, String mainClass) {
        this.file = file;
        this.mainClass = mainClass;
    }

    public File getFile() {
        return this.file;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
