package ru.itmo.lab5.comands;

import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CommandManager;

/**
 * Команда для вывода справки о доступных командах.
 */
public class Help extends Command {
    private final Console console;          // Консоль для взаимодействия с пользователем
    private final CommandManager commandManager; // Менеджер команд

    /**
     * Конструктор класса.
     *
     * @param console         объект класса Console для взаимодействия с пользователем
     * @param commandManager объект класса CommandManager для управления командами
     */
    public Help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду вывода справки о доступных командах.
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
        commandManager.getCommands().values()
                .forEach(command -> console.printTable(command.getName(), command.getDescription()));
        return true;
    }
}
