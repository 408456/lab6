package ru.itmo.general.comands;

import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.time.LocalDateTime;

/**
 * Команда для вывода информации о коллекции.
 */
public class Info extends Command {
    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public Info(){
        super("info", " - вывести информацию о коллекции");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public Info(CollectionManager collectionManager) {
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
        if (arguments.length != 2) {
            return new Request(false, getName(), "Пожалуйста введите команду в правильном формате");
        }
        return new Request(getName(), null);
    }

    /**
     * Выполняет команду на сервере.
     *
     * @param request запрос, содержащий данные для выполнения команды
     * @return ответ с результатом выполнения команды
     */
    @Override
    public Response execute(Request request) {
        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeStr = (lastInitTime == null) ? "Инициализации в данной сессии еще не происходило" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();
        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeStr = (lastSaveTime == null) ? "Сохранения в данной сессии еще не происходило" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        String info = "Информация о коллекции:\n" +
                "Тип коллекции: " + collectionManager.getType() + "\n" +
                "Размер коллекции (количество элементов в коллекции): " + collectionManager.getSize() + "\n" +
                "Дата и время последней инициализации: " + lastInitTimeStr + "\n" +
                "Дата и время последнего сохранения: " + lastSaveTimeStr;
        return new Response(true, "Информация о коллекции:", info);
    }
}
