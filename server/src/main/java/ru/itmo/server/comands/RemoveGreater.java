package ru.itmo.server.comands;

import ru.itmo.general.data.Product;
import ru.itmo.general.data.User;
import ru.itmo.general.utility.exceptions.IncorrectScriptException;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.general.utility.exceptions.InvalidFormException;
import ru.itmo.general.utility.exceptions.InvalidValueException;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.ProductInput;
import ru.itmo.server.dao.UserDAO;
import ru.itmo.server.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для удаления из коллекции элементов, превышающих заданный.
 */
public class RemoveGreater extends Command {
    private CollectionManager collectionManager;
    private UserDAO userDAO;
    /**
     * Конструктор класса.
     */
    public RemoveGreater() {
        super("remove_greater", "<id> {element} удалить из коллекции все элементы, превышающие заданный");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveGreater(CollectionManager collectionManager, UserDAO userDAO) {
        this();
        this.collectionManager = collectionManager;
        this.userDAO = userDAO;
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
            product.setUserId(request.getUserId());
            collectionManager.removeGreater(product, userDAO.getUserByUsername(request.getLogin()).getId());
            return new Response(true, "Продукты успешно удалены!");
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
