package ru.itmo.server.comands;

import ru.itmo.general.data.Product;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.general.utility.exceptions.InvalidFormException;
import ru.itmo.general.utility.exceptions.InvalidValueException;
import ru.itmo.general.utility.exceptions.IncorrectScriptException;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.ProductInput;
import ru.itmo.server.managers.CollectionManager;
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
            newProduct.setUserId(request.getUserId());
            var product = collectionManager.getById(newProduct.getId());

            if (product == null) {
                return new Response(false, "Продукта с таким ID в коллекции нет!");
            }
            if (request.getUserId() != product.getUserId()) {
                return new Response(false, "Продукт вам не принадлежит!");
            }
            collectionManager.updateProduct(newProduct, newProduct.getUserId());
            return new Response(true, "Продукт успешно обновлен!");
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
