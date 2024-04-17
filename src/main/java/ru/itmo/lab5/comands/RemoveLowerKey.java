package ru.itmo.lab5.comands;

import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для удаления всех элементов коллекции, ключ которых меньше, чем заданный
 */
public class RemoveLowerKey extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveLowerKey(Console console, CollectionManager collectionManager) {
        super("remove_lower_key <id>", "удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 2) throw new InvalidAmountException();

            Long id = Long.parseLong(args[1]);
            collectionManager.removeLowerKey(id);
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
