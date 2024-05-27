package ru.itmo.general.comands;

import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для завершения работы программы.
 */
public class Exit extends Command {
    private CollectionManager collectionManager;
    public Exit() {
        super("exit", " - завершить программу (без сохранения в файл)");
    }
    public Exit(CollectionManager collectionManager){
        this();
        this.collectionManager = collectionManager;
    }
                @Override
    public Request execute(String[] arguments) {
        if (arguments.length != 2) {
            return new Request(false, getName(), "Пожалуйста введите команду в правильном формате");
        }
        return new Request(getName(), null);
    }
    @Override
    public Response execute(Request request) {
        collectionManager.saveCollection();
        System.exit(0);
        return new Response(true, getName());
    }
}
