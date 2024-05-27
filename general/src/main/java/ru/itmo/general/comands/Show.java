package ru.itmo.general.comands;

import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class Show extends Command {
    private CollectionManager collectionManager;

    public Show(){
        super("show", " - вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }
    public Show(CollectionManager collectionManager) {
        this();
        this.collectionManager = collectionManager;
    }

    @Override
    public Request execute(String[] arguments) {
        if (arguments.length != 2) {
            return new Request(false, getName(), "Пожалуйста, введите команду в правильном формате");
        }
        return new Request(getName(), null);
    }

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
