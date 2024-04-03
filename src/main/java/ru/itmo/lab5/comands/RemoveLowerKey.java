package ru.itmo.lab5.comands;

import ru.itmo.lab5.exceptions.IncorrectScriptException;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.exceptions.InvalidFormException;
import ru.itmo.lab5.exceptions.InvalidValueException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.input.ProductInput;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для удаления всех элементов коллекции, ключ которых меньше, чем заданный
 */
public class RemoveLowerKey extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveLowerKey(Console console, CollectionManager collectionManager) {
        super("remove_lower_key {element}", "удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (!args[1].isEmpty()) throw new InvalidAmountException();
            var product = ((new ProductInput(console).make()));
            collectionManager.removeLowerKey(product);
            console.println("Продукты успешно удалены!");
            return true;
        } catch (InvalidAmountException exception) {
            console.printError("Неправильное количество аргументов!");
        } catch (InvalidFormException | InvalidValueException e) {
            console.printError("Поля продукта не валидны! Продукт не создан!");
        } catch (IncorrectScriptException ignored) {
        }
        return false;
    }
}
