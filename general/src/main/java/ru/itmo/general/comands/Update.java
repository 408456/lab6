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
    public Update(){
        super("update", " <id> {element} - обновить значение элемента коллекции по ID");
    }
    public Update(Console console){
        this();
        this.console = console;
    }
    public Update(CollectionManager collectionManager) {
        this();
        this.collectionManager = collectionManager;
    }

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

    @Override
    public Response execute(Request request) {
        try {
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
