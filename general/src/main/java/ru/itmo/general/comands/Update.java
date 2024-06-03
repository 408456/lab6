package ru.itmo.general.comands;

import ru.itmo.general.data.Product;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.general.utility.exceptions.InvalidFormException;
import ru.itmo.general.utility.exceptions.InvalidValueException;
import ru.itmo.general.utility.exceptions.IncorrectScriptException;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.ProductInput;
import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для обновления значения элемента коллекции по ID.
 */
public class Update extends Command {
    private CollectionManager collectionManager;
    private Console console;

    /**
     * Конструктор класса.
     */
    public Update(){
        super("update", "<id> {element} обновить значение элемента коллекции по ID");
    }

    /**
     * Конструктор класса.
     *
     * @param console консоль для ввода данных
     */
    public Update(Console console){
        this();
        this.console = console;
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public Update(CollectionManager collectionManager) {
        this();
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду на сервере.
     *
     * @param arguments аргументы команды
     * @return запрос для выполнения на сервере
     */
    @Override
    public Request execute(String[] arguments) {
        try {
            if (arguments.length != 2 || arguments[0].isEmpty() || arguments[1].isEmpty())
                throw new InvalidAmountException();

            long id;
            try {
                id = Long.parseLong(arguments[1]);
            } catch (NumberFormatException e) {
                return new Request(false, getName(), "ID продукта должен быть целым числом!");
            }

            Product newProduct = new ProductInput(console).make();
            newProduct.setId(id);

            return new Request(getName(), newProduct);
        } catch (InvalidAmountException e) {
            return new Request(false, getName(), "Неправильное количество аргументов!");
        } catch (InvalidFormException | InvalidValueException e) {
            return new Request(false, getName(), "Поля продукта не валидны! Продукт не создан!");
        } catch (IncorrectScriptException e) {
            return new Request(false, getName(), "Ошибка выполнения скрипта: " + e.getMessage());
        }
    }

    /**
     * Выполняет команду на клиенте.
     *
     * @param request запрос, содержащий данные для выполнения команды
     * @return ответ с результатом выполнения команды
     */
    @Override
    public Response execute(Request request) {
        try {
//            Product newProduct = new ProductInput(console).make();
//            newProduct.setId(id);
            Product newProduct = (Product) request.getData();
            var product = collectionManager.getById(newProduct.getId());

            if (product == null) {
                return new Response(false, "Продукта с таким ID в коллекции нет!");
            }

            product.update(newProduct);
            return new Response(true, "Продукт успешно обновлен!");
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
