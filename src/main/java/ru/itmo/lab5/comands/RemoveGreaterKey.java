package ru.itmo.lab5.comands;

import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для удаления всех элементов коллекции, ключ которых больше, чем заданный.
 */
public class RemoveGreaterKey extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveGreaterKey(Console console, CollectionManager collectionManager) {
        super("remove_greater_key <id>", "удалить из коллекции все элементы, ключ которых больше, чем заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 2) throw new InvalidAmountException();

            Long id = Long.parseLong(args[1]);
            collectionManager.removeGreaterKey(id);
            console.println("Продукты успешно удалены!");
            return true;

        } catch (NumberFormatException e) {
            console.printError("Неверный формат id! Введите целое число.");
        } catch (InvalidAmountException exception) {
            console.printError("Неправильное количество аргументов!");
        }
        return false;
    }
}

