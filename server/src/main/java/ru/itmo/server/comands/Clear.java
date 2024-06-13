package ru.itmo.server.comands;

import ru.itmo.server.dao.UserDAO;
import ru.itmo.server.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда 'clear'. Очищает коллекцию.
 */
public class Clear extends Command {

    private CollectionManager collectionManager; /**< Менеджер коллекции */
    private UserDAO userDAO;
    /**
     * Конструктор класса.
     */
    public Clear(UserDAO userDAO, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.userDAO = userDAO;
        this.collectionManager = collectionManager;
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
            collectionManager.clearCollection(userDAO.getUserByUsername(request.getLogin()).getId());
            return new Response(true, "Коллекция очищена от продуктов.");
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }
    }
}
