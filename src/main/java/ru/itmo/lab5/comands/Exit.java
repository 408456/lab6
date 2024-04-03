package ru.itmo.lab5.comands;

import ru.itmo.lab5.input.Console;

/**
 * Команда для завершения работы программы
 *
 */
public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.console = console;
    }

    @Override
    public boolean execute(String[] args) {
        if (!args[1].isEmpty()) {
            console.println("Пожалуйста введите команду в правильном формате");
            return false;
        }
        console.println("Завершение выполнения");
        return true;
    }
}
