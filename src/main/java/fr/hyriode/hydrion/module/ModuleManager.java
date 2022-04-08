package fr.hyriode.hydrion.module;

import fr.hyriode.hydrion.api.module.HydrionModule;
import fr.hyriode.hydrion.api.module.IModuleManager;
import fr.hyriode.hydrion.module.friends.FriendsModule;
import fr.hyriode.hydrion.module.network.NetworkModule;
import fr.hyriode.hydrion.module.player.PlayerModule;
import fr.hyriode.hydrion.module.resources.ResourcesModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 19:58
 */
public class ModuleManager implements IModuleManager {

    private final Map<String, HydrionModule> modules;

    public ModuleManager() {
        this.modules = new HashMap<>();
    }

    public void registerDefaults() {
        this.registerModule("player", new PlayerModule());
        this.registerModule("friends", new FriendsModule());
        this.registerModule("resources", new ResourcesModule());
        this.registerModule("network", new NetworkModule());
    }

    @Override
    public void registerModule(String name, HydrionModule module) {
        this.modules.putIfAbsent(name, module);

        System.out.println("Registered '" + name + "' module.");
    }

    @Override
    public void unregisterModule(String name) {
        this.modules.remove(name);

        System.out.println("Unregistered '" + name + "' module.");
    }

    @Override
    public HydrionModule getModule(String name) {
        return this.modules.get(name);
    }

    @Override
    public <T extends HydrionModule> T getModule(Class<T> moduleClass) {
        for (Map.Entry<String, HydrionModule> entry : this.modules.entrySet()) {
            final HydrionModule module = entry.getValue();

            if (module.getClass() == moduleClass) {
                return moduleClass.cast(module);
            }
        }
        return null;
    }

    @Override
    public List<HydrionModule> getModules() {
        return new ArrayList<>(this.modules.values());
    }

}
