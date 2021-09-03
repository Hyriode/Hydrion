package fr.hyriode.hydrion.command;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 12:41
 */
public abstract class CommandArgument {

    protected final String name;
    protected final String usageMessage;

    public CommandArgument(String name) {
        this(name, "");
    }

    public CommandArgument(String name, String usageMessage) {
        this.name = name;
        this.usageMessage = usageMessage;
    }

    public abstract void handle(String[] args);

    public String getName() {
        return this.name;
    }

    public String getUsageMessage() {
        return this.usageMessage;
    }

}
