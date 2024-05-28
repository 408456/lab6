package ru.itmo.general.comands;

import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда 'clear'. Очищает коллекцию.
 */
public class Clear extends Command {

    private CollectionManager collectionManager; /**< Менеджер коллекции */

    /**
     * Конструктор класса.
     */
    public Clear() {
        super("clear", "очистить коллекцию");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public Clear(CollectionManager collectionManager) {
        this();
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
            collectionManager.clearCollection();
            return new Response(true, "Коллекция очищена от продуктов.");
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }
    }

    /**
     * Выполняет команду на клиенте.
     *
     * @param arguments аргументы команды
     * @return запрос для выполнения на сервере
     */
    @Override
    public Request execute(String[] arguments) {
        if (arguments.length != 2 || arguments[0].isEmpty()) {
            return new Request(false, getName(), getUsingError());
        }
        return new Request(getName(), null);
    }
}
