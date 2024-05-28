package ru.itmo.general.comands;

import ru.itmo.general.data.Product;
import ru.itmo.general.utility.exceptions.IncorrectScriptException;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.general.utility.exceptions.InvalidFormException;
import ru.itmo.general.utility.exceptions.InvalidValueException;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.ProductInput;
import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для добавления элемента в коллекцию с заданным ключем.
 */
public class Insert extends Command {
    private CollectionManager collectionManager;
    private Console console;

    /**
     * Конструктор класса.
     */
    public Insert(){
        super("insert", "<id> {element} добавить новый элемент с заданным ключом");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public Insert(CollectionManager collectionManager) {
        this();
        this.collectionManager = collectionManager;
    }

    /**
     * Конструктор класса.
     *
     * @param console консоль для ввода
     */
    public Insert(Console console){
        this();
        this.console = console;
    }

    /**
     * Выполняет команду на клиенте.
     *
     * @param arguments аргументы команды
     * @return запрос для выполнения на сервере
     */
    @Override
    public Request execute(String[] arguments) {
        try {
            if (arguments.length != 2 || arguments[1].isEmpty()) throw new InvalidAmountException();

            long id = Long.parseLong(arguments[1]);

            Product product = new ProductInput(console).make();
            product.setId(id);

            return new Request(getName(), product);
        } catch (InvalidAmountException e) {
            return new Request(false, getName(), "Неправильное количество аргументов!");
        } catch (NumberFormatException e) {
            return new Request(false, getName(), "Некорректный формат ключа! Ожидается целое число.");
        } catch (InvalidFormException | InvalidValueException e) {
            return new Request(false, getName(), "Поля продукта не валидны! Продукт не создан.");
        } catch (IncorrectScriptException e) {
            return new Request(false, getName(), "Ошибка выполнения скрипта: " + e.getMessage());
        }
    }

    /**
     * Выполняет команду на сервере.
     *
     * @param request запрос, содержащий данные для выполнения команды
     * @return ответ с результатом выполнения команды
     */
    @Override
    public Response execute(Request request) {
        try {
            Product product = (Product) request.getData();
            if (collectionManager.contains(product.getId())) {
                return new Response(false, "Элемент с заданным ключом уже существует!");
            }
            collectionManager.addToCollection(product);
            return new Response(true, "Продукт с ключом " + product.getId() + " успешно добавлен!");
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }
    }
}
