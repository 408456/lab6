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

    /**
     * Конструктор класса.
     */
    public RemoveGreater() {
        super("remove_greater", " {element} - удалить из коллекции все элементы, превышающие заданный");
    }

    /**
     * Конструктор класса.
     *
     * @param console объект для ввода данных пользователя
     */
    public RemoveGreater(Console console) {
        this();
        this.console = console;
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveGreater(CollectionManager collectionManager) {
        this();
        this.collectionManager = collectionManager;
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
            collectionManager.removeGreater(product);
            return new Response(true, "Продукты успешно удалены!");
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
