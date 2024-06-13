package ru.itmo.server.comands;

import ru.itmo.general.data.Person;
import ru.itmo.general.data.Product;
import ru.itmo.server.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Команда для вывода полей owner всех элементов в порядке возрастания.
 */
public class PrintFieldAscendingOwner extends Command {
    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public PrintFieldAscendingOwner() {
        super("print_field_ascending_owner", "вывести значения поля owner всех элементов в порядке возрастания");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public PrintFieldAscendingOwner(CollectionManager collectionManager) {
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
            if (collectionManager.getCollection().isEmpty()) {
                return new Response(false, getName(), "Коллекция пуста!");
            }
            List<Person> owners = collectionManager.getCollection().values().stream()
                    .map(Product::getOwner)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(Person::getPassportID))
                    .distinct()
                    .toList();
            StringBuilder output = new StringBuilder();
            owners.forEach(owner -> output.append(owner.toString()).append(System.lineSeparator()));

            return new Response(true, output.toString());
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
