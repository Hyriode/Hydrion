package fr.hyriode.hydrion.command.model;

import fr.hyriode.hydrion.command.Command;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 12:54
 */
public class StopCommand extends Command {

    public StopCommand() {
        super("stop");
    }

    @Override
    public boolean execute(String[] args) {
        System.exit(0);
        return true;
    }

}
