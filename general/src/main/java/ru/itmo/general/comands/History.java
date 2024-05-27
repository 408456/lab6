package ru.itmo.general.comands;

import ru.itmo.general.managers.CommandManager;
import ru.itmo.general.network.Request;

/**
 * Команда для вывода последних 8 команд.
 */
public class History extends Command {
    private final CommandManager commandManager;

    public History(CommandManager commandManager) {
        super("history", " - вывести последние 8 команд");
        this.commandManager = commandManager;
    }

    @Override
    public Request execute(String[] arguments) {
        if (arguments.length != 2) {
            return new Request(false, getName(), "Пожалуйста введите команду в правильном формате");
        }

        return new Request(getName(), commandManager.getCommandHistory());
    }
}
