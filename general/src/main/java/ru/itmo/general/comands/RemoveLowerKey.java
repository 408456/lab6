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

    public RemoveLowerKey(){
        super("remove_lower_key", " <id> - удалить из коллекции все элементы, ключ которых меньше, чем заданный");

    }
    public RemoveLowerKey(CollectionManager collectionManager) {
        this();
        this.collectionManager = collectionManager;
    }

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
