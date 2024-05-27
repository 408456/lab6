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
 * Команда для удаления из коллекции элементов, превышающих заданный.
 */
public class RemoveGreater extends Command {
    private CollectionManager collectionManager;
    private Console console;
    public RemoveGreater() {
        super("remove_greater", " {element} - удалить из коллекции все элементы, превышающие заданный");
    }
    public RemoveGreater(Console console) {
        this();
        this.console = console;
    }
    public RemoveGreater(CollectionManager collectionManager) {
        this();
        this.collectionManager = collectionManager;
    }

    @Override
    public Request execute(String[] arguments) {
        try {
            if (arguments.length != 2) throw new InvalidAmountException();

            Product product = new ProductInput(console).make();
            return new Request(getName(), product);
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
            Product product = (Product) request.getData();
            collectionManager.removeGreater(product);
            return new Response(true, "Продукты успешно удалены!");
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}