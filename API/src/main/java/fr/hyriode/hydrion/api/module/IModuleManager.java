package fr.hyriode.hydrion.api.module;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 19:55
 */
public interface IModuleManager {

    void registerModule(String name, HydrionModule module);

    void unregisterModule(String name);

    HydrionModule getModule(String name);

    <T extends HydrionModule> T getModule(Class<T> moduleClass);

    List<HydrionModule> getModules();

}
