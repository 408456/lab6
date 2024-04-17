package ru.itmo.lab5.comands;

import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class Show extends Command {
    private final Console console; // Консоль для взаимодействия с пользователем
    private final CollectionManager collectionManager; // Менеджер коллекции

    /**
     * Конструктор класса.
     *
     * @param console            объект класса Console для взаимодействия с пользователем
     * @param collectionManager объект класса CollectionManager для управления коллекцией
     */
    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода всех элементов коллекции.
     *
     * @param args аргументы команды (в данном случае не используются)
     * @return true, если команда выполнена успешно, иначе false
     */
    @Override
    public boolean execute(String[] args) {
        if (!args[1].isEmpty()) {
            console.println("Пожалуйста, введите команду в правильном формате");
            return false;
        }
        console.println(collectionManager);
        return true;
    }
}
