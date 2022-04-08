package fr.hyriode.hydrion.api.object.network;

import fr.hyriode.hydrion.api.handler.HydrionHandler;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 08/04/2022 at 19:04
 */
public interface INetworkManager {

    void addHandler(String path, HydrionHandler handler);

}
