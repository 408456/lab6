package ru.itmo.lab5.comands;

import ru.itmo.lab5.exceptions.IncorrectScriptException;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.exceptions.InvalidFormException;
import ru.itmo.lab5.exceptions.InvalidValueException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.input.ProductInput;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для удаления всех элементов коллекции, ключ которых больше, чем заданный.
 */
public class RemoveGreaterKey extends Command {
    private final Console console;          // Консоль для взаимодействия с пользователем
    private final CollectionManager collectionManager; // Менеджер коллекции

    /**
     * Конструктор класса.
     *
     * @param console            объект класса Console для взаимодействия с пользователем
     * @param collectionManager объект класса CollectionManager для управления коллекцией
     */
    public RemoveGreaterKey(Console console, CollectionManager collectionManager) {
        super("remove_greater_key {element}", "удалить из коллекции все элементы, ключ которых больше, чем заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления всех элементов коллекции, ключ которых больше, чем заданный.
     *
     * @param args аргументы команды (в данном случае не используются)
     * @return true, если команда выполнена успешно, иначе false
     */
    @Override
    public boolean execute(String[] args) {
        try {
            if (!args[1].isEmpty()) throw new InvalidAmountException();
            var product = ((new ProductInput(console).make()));
            collectionManager.removeGreaterKey(product);
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
