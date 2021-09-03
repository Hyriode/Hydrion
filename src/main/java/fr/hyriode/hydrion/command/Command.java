package fr.hyriode.hydrion.command;

import fr.hyriode.hydrion.Hydrion;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 12:40
 */
public abstract class Command {

    protected final Set<CommandArgument> arguments;

    protected Hydrion hydrion;

    protected String name;

    public Command(String name) {
        this.name = name;
        this.arguments = new HashSet<>();
    }

    public boolean execute(String[] args) {
        this.handleArguments(args);
        return true;
    }

    protected void handleArguments(String[] args) {
        if (args.length > 0) {
            final String firstArg = args[0];
            final CommandArgument argument = this.getArgumentByName(firstArg);

            if (argument != null) {
                argument.handle(Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }

    protected void addArguments() {}

    public boolean addArgument(CommandArgument argument) {
        if (this.getArgumentByName(argument.getName()) == null) {
            this.arguments.add(argument);

            return true;
        }
        return false;
    }

    public boolean removeArgument(String name) {
        final CommandArgument argument = this.getArgumentByName(name);

        if (argument != null) {
            this.arguments.remove(argument);

            return true;
        }
        return false;
    }

    public CommandArgument getArgumentByName(String name) {
        for (CommandArgument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                return argument;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    void setHydrion(Hydrion hydrion) {
        this.hydrion = hydrion;
    }

    public Set<CommandArgument> getArguments() {
        return this.arguments;
    }

}
