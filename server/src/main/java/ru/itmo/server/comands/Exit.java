package ru.itmo.server.comands;

import ru.itmo.server.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для завершения работы программы.
 */
public class Exit extends Command {
    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public Exit(CollectionManager collectionManager) {
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
        System.exit(0);
        return new Response(true, getName());
    }
}
