package fr.hyriode.hydrion.command;

import com.google.common.reflect.ClassPath;
import fr.hyriode.hydrion.Hydrion;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 12:39
 */
public class CommandManager {

    private static final String PACKAGE = "fr.hyriode.hydrion";

    private Thread commandThread;

    private final List<Command> commands;

    private final Hydrion hydrion;

    public CommandManager(Hydrion hydrion) {
        this.hydrion = hydrion;
        this.commands = new ArrayList<>();
    }

    public void start() {
        System.out.println("Starting command manager...");

        this.autoRegisterCommands();

        this.commandThread = new Thread(() -> {
            while (this.hydrion.isRunning()) {
                String line = null;

                try {
                    line = this.hydrion.getConsoleReader().readLine(">");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (line != null) {
                    this.inputCommand(line);
                }
            }
        });
        this.commandThread.start();
    }

    private void autoRegisterCommands() {
        System.out.println("Searching for commands in '" + PACKAGE + "' package...");

        try {
            final ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());

            for(ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(PACKAGE)) {
                final Class<?> clazz = Class.forName(classInfo.getName());

                if(this.checkSuperClass(clazz)) {
                    if(this.hasParameterLessConstructor(clazz)) {
                        final Command command = (Command) clazz.getConstructor().newInstance();

                        command.setHydrion(this.hydrion);
                        command.addArguments();

                        System.out.println("Registering '" + command.getName() + "' command");

                        this.addCommand(command);
                    } else {
                        System.out.println(clazz.getSimpleName() + " inherit of " + Command.class.getSimpleName() + " but doesn't have a parameter less constructor!");
                    }
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private boolean checkSuperClass(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null) {
            if(superClass.equals(Command.class)) {
                return true;
            }
            clazz = superClass;
            superClass = clazz.getSuperclass();
        }
        return false;
    }

    private boolean hasParameterLessConstructor(Class<?> clazz) {
        for(Constructor<?> constructor : clazz.getConstructors()) {
            if(constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    public void shutdown() {
        System.out.println("Stopping command manager...");

        this.commandThread.interrupt();
    }

    public void inputCommand(String data) {
        String[] args = data.split(" ");
        final String commandLabel = args[0];

        args = Arrays.copyOfRange(args, 1, args.length);

        for (Command command : this.commands) {
            if (command.getName().equalsIgnoreCase(commandLabel)) {
                if (!command.execute(args)) {
                    Hydrion.getLogger().log(Level.SEVERE, String.format("An error occurred while executing %s command !", command.getName()));
                }
                return;
            }
        }

        System.out.println("Command '" + commandLabel + "' doesn't exist !");
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public void removeCommand(Command command) {
        this.commands.remove(command);
    }

    public List<Command> getCommands() {
        return this.commands;
    }

}
