package ru.itmo.server.comands;

import ru.itmo.server.managers.CommandManager;
import ru.itmo.general.network.Request;

/**
 * Команда для вывода последних 8 команд.
 */
public class History extends Command {
    private final CommandManager commandManager;

    /**
     * Конструктор класса.
     *
     * @param commandManager менеджер команд
     */
    public History(CommandManager commandManager) {
        super("history", "вывести последние 8 команд");
        this.commandManager = commandManager;
    }
}
