package ru.itmo.lab5.comands;

import ru.itmo.lab5.input.Console;

/**
 * Команда для завершения работы программы.
 */
public class Exit extends Command {
    private final Console console; // Консоль для взаимодействия с пользователем

    /**
     * Конструктор класса.
     *
     * @param console объект класса Console для взаимодействия с пользователем
     */
    public Exit(Console console) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.console = console;
    }

    /**
     * Выполняет команду завершения работы программы.
     *
     * @param args аргументы команды (в данном случае не используются)
     * @return true, так как команда завершает работу программы
     */
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
