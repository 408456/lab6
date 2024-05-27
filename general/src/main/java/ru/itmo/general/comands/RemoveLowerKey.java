package ru.itmo.general.comands;

import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для удаления всех элементов коллекции, ключ которых меньше, чем заданный.
 */
public class RemoveLowerKey extends Command {
    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public RemoveLowerKey() {
        super("remove_lower_key", " <id> - удалить из коллекции все элементы, ключ которых меньше, чем заданный");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public RemoveLowerKey(CollectionManager collectionManager) {
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
            if (arguments.length != 2 || arguments[1].isEmpty()) {
                throw new InvalidAmountException();
            }
            Long id = Long.parseLong(arguments[1]);
            return new Request(getName(), id);
        } catch (InvalidAmountException e) {
            return new Request(false, getName(), "Неправильное количество аргументов!");
        } catch (NumberFormatException e) {
            return new Request(false, getName(), "Неверный формат id! Введите целое число.");
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
            Long id = (Long) request.getData();
            collectionManager.removeLowerKey(id);
            return new Response(true, "Продукты успешно удалены!");
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
