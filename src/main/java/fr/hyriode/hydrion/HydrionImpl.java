package fr.hyriode.hydrion;

import fr.hyriode.hydrion.api.HydrionAPI;
import fr.hyriode.hydrion.api.network.INetworkManager;

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

}
