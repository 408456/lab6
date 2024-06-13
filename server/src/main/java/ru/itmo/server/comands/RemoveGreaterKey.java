package ru.itmo.server.comands;

import ru.itmo.general.data.User;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.server.dao.UserDAO;
import ru.itmo.server.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для удаления всех элементов коллекции, ключ которых больше, чем заданный.
 */
public class RemoveGreaterKey extends Command {
    private CollectionManager collectionManager;
    private UserDAO userDAO;
    /**
     * Конструктор класса.
     */
    public RemoveGreaterKey() {
        super("remove_greater_key", "<id> удалить из коллекции все элементы, ключ которых больше, чем заданный");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveGreaterKey(CollectionManager collectionManager, UserDAO userDAO) {
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
            Long id = (Long) request.getData();
            collectionManager.removeGreaterKey(id, userDAO.getUserByUsername(request.getLogin()).getId());
            return new Response(true, "Продукты успешно удалены!");
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
