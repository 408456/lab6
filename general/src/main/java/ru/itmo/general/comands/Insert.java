package ru.itmo.lab5.comands;

import ru.itmo.lab5.exceptions.IncorrectScriptException;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.exceptions.InvalidFormException;
import ru.itmo.lab5.exceptions.InvalidValueException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.input.ProductInput;
import ru.itmo.lab5.managers.CollectionManager;
import ru.itmo.lab5.data.Product;

/**
 * Команда для добавления элемента в коллекцию с заданным ключем.
 */
public class Insert extends Command {
    private final Console console;          // Консоль для взаимодействия с пользователем
    private final CollectionManager collectionManager; // Менеджер коллекции

    /**
     * Конструктор класса.
     *
     * @param console            объект класса Console для взаимодействия с пользователем
     * @param collectionManager объект класса CollectionManager для управления коллекцией
     */
    public Insert(Console console, CollectionManager collectionManager) {
        super("insert <id> {element}", "добавить новый элемент с заданным ключом");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления элемента в коллекцию с заданным ключем.
     *
     * @param args аргументы команды
     * @return true, если команда выполнена успешно, иначе false
     */
    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 2) throw new InvalidAmountException();
//            if (!collectionManager.canWriteToFile()) {
//                console.printError("Нет прав на запись в файл! Выполнить команду " + getName() + " невозможно!");
//                return false;
//            }
            long id = Long.parseLong(args[1]);
            if (collectionManager.contains(id)) {
                console.printError("Элемент с заданным ключом уже существует!");
                return false;
            }

            Product product = new ProductInput(console).make();
            product.setId(id);
            collectionManager.addToCollection(product);
            console.println("Продукт с ключом " + id + " успешно добавлен!");
            return true;
        } catch (InvalidAmountException e) {
            console.printError("Неправильное количество аргументов!");
        } catch (NumberFormatException e) {
            console.printError("Некорректный формат ключа! Ожидается целое число.");
        } catch (InvalidFormException | InvalidValueException e) {
            console.printError("Поля продукта не валидны! Продукт не создан.");
        } catch (IncorrectScriptException e) {
            console.printError("Ошибка выполнения скрипта: " + e.getMessage());
        }
        return false;
    }
}
