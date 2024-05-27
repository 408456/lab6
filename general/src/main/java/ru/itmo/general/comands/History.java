package ru.itmo.lab5.comands;

import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CommandManager;

/**
 * Команда для вывода последних 8 команд.
 */
public class History extends Command {
    private final Console console;          // Консоль для взаимодействия с пользователем
    private final CommandManager commandManager; // Менеджер команд

    /**
     * Конструктор класса.
     *
     * @param console         объект класса Console для взаимодействия с пользователем
     * @param commandManager объект класса CommandManager для управления командами
     */
    public History(Console console, CommandManager commandManager) {
        super("history", "вывести последние 8 команд");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду вывода последних 8 команд.
     *
     * @param args аргументы команды (в данном случае не используются)
     * @return true, так как команда успешно выполнена
     */
    @Override
    public boolean execute(String[] args) {
        if (!args[1].isEmpty()) {
            console.println("Пожалуйста введите команду в правильном формате");
            return false;
        }
        console.println(String.join("\n", commandManager.get8CommandHistory()));
        return true;
    }
}
