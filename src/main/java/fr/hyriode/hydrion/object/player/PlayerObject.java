package fr.hyriode.hydrion.object.player;

import fr.hyriode.hydrion.object.HydrionObject;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:40
 */
public class PlayerObject extends HydrionObject {

    private final String name;
    private final String displayName;
    private final UUID uuid;

    public PlayerObject(String name, String displayName, UUID uuid) {
        this.name = name;
        this.displayName = displayName;
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public UUID getUuid() {
        return this.uuid;
    }

}
