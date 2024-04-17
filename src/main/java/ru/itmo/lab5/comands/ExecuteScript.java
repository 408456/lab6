package ru.itmo.lab5.comands;

import ru.itmo.lab5.input.Console;

/**
 * Команда для исполнения скрипта.
 */
public class ExecuteScript extends Command {
    private final Console console; // Консоль для взаимодействия с пользователем

    /**
     * Конструктор класса.
     *
     * @param console объект класса Console для взаимодействия с пользователем
     */
    public ExecuteScript(Console console) {
        super("execute_script <file_name>", "исполнить скрипт из указанного файла");
        this.console = console;
    }

    /**
     * Выполняет команду исполнения скрипта.
     *
     * @param args аргументы команды
     * @return true, если команда выполнена успешно, иначе false
     */
    @Override
    public boolean execute(String[] args) {
        if (args[1].isEmpty()) {
            console.println("Пожалуйста введите команду в правильном формате!");
            return false;
        }
        console.println("Выполнение скрипта '" + args[1] + "':");
        return true;
    }
}
