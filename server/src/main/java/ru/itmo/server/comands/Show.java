package ru.itmo.server.comands;

import ru.itmo.server.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class Show extends Command {
    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public Show(){
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public Show(CollectionManager collectionManager) {
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
            String collectionString = collectionManager.toString();
            return new Response(true, collectionString);
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
