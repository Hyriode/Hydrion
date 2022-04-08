package fr.hyriode.hydrion;

import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.cache.ICacheManager;
import fr.hyriode.hydrion.api.database.mongodb.MongoDB;
import fr.hyriode.hydrion.api.module.IModuleManager;
import fr.hyriode.hydrion.api.object.network.INetworkManager;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 20:06
 */
public class HydrionImpl extends HydrionAPI {

    private final Hydrion hydrion;

    public HydrionImpl(Hydrion hydrion) {
        this.hydrion = hydrion;
    }

    @Override
    public UUID getAPIKey() {
        return this.hydrion.getConfiguration().getAPIKey();
    }

    @Override
    public INetworkManager getNetworkManager() {
        return this.hydrion.getNetworkManager();
    }

    @Override
    public ICacheManager getCacheManager() {
        return this.hydrion.getCacheManager();
    }

    @Override
    public IModuleManager getModuleManager() {
        return this.hydrion.getModuleManager();
    }

    @Override
    public MongoDB getMongoDB() {
        return this.hydrion.getMongoDB();
    }

}
