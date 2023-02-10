package fr.hyriode.hydrion.util.netty;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 09:52
 */
public enum NettyGroupType {

    BOSS("Boss"),
    WORKER("Worker");

    private final String name;

    NettyGroupType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
