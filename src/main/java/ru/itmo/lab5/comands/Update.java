package ru.itmo.lab5.comands;

import ru.itmo.lab5.data.Product;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.exceptions.InvalidFormException;
import ru.itmo.lab5.exceptions.InvalidValueException;
import ru.itmo.lab5.exceptions.MustBeNotEmptyException;
import ru.itmo.lab5.exceptions.IncorrectScriptException;
import ru.itmo.lab5.input.ProductInput;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для обновления значения элемента коллекции по ID.
 */
public class Update extends Command {
    private final Console console; // Консоль для взаимодействия с пользователем
    private final CollectionManager collectionManager; // Менеджер коллекции

    /**
     * Конструктор класса.
     *
     * @param console           объект класса Console для взаимодействия с пользователем
     * @param collectionManager объект класса CollectionManager для управления коллекцией
     */
    public Update(Console console, CollectionManager collectionManager) {
        super("update <id> {element}", "обновить значение элемента коллекции по ID");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length < 2 || args[1].isEmpty()) throw new InvalidAmountException();

            long id;
            try {
                id = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                console.printError("ID продукта должен быть целым числом!");
                return false;
            }

            var product = collectionManager.getById(id);
            if (product == null) throw new MustBeNotEmptyException();

            console.println("Введите новые данные продукта:");
            console.ps1();

            Product newProduct = (new ProductInput(console)).make();
            product.update(newProduct);

            console.println("Продукт успешно обновлен!");
            return true;
        } catch (InvalidAmountException exception) {
            console.printError("Неправильное количество аргументов!");
        } catch (InvalidFormException | InvalidValueException e) {
            console.printError("Поля продукта не валидны! Продукт не обновлен!");
        } catch (MustBeNotEmptyException exception) {
            console.printError("Продукта с таким ID в коллекции нет!");
        } catch (IncorrectScriptException ignored) {
        }
        return false;
    }

}
