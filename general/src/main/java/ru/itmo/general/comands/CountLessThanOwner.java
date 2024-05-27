package ru.itmo.general.comands;

import ru.itmo.general.data.Person;
import ru.itmo.general.utility.exceptions.IncorrectScriptException;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.ProductInput;
import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для вывода количества элементов, значение поля owner которых меньше заданного.
 */
public class CountLessThanOwner extends Command {

    private CollectionManager collectionManager; /**< Менеджер коллекции */
    private Console console; /**< Консоль */

    /**
     * Конструктор класса.
     */
    public CountLessThanOwner() {
        super("count_less_than_owner", " <owner> вывести количество элементов, значение поля owner которых меньше заданного");
    }

    /**
     * Конструктор класса.
     *
     * @param console консоль
     */
    public CountLessThanOwner(Console console){
        this();
        this.console = console;
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public CountLessThanOwner(CollectionManager collectionManager) {
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
            Person owner = (Person) request.getData();
            long countProduct = collectionManager.countLessThanOwner(owner);
            if (countProduct == 0) {
                return new Response(true, "Продуктов, у которых owner меньше указанного, не найдено!");
            } else {
                return new Response(true, "Найдено " + countProduct + " продуктов, у которых owner меньше указанного.");
            }
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
        try {
            ProductInput productInput = new ProductInput(console); // Необходимо обновить класс ProductInput, чтобы он мог работать без консоли
            Person owner = productInput.inputPersonForCountLess();
            return new Request(getName(), owner);
        } catch (IllegalArgumentException | IncorrectScriptException e) {
            return new Request(false, getName(), e.getMessage());
        }
    }
}
