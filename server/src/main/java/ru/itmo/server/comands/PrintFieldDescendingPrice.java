package ru.itmo.server.comands;

import ru.itmo.general.data.Product;
import ru.itmo.server.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда для вывода значения поля price всех элементов в порядке убывания.
 */
public class PrintFieldDescendingPrice extends Command {
    private CollectionManager collectionManager;

    /**
     * Конструктор класса.
     */
    public PrintFieldDescendingPrice() {
        super("print_field_descending_price", "вывести значения поля price всех элементов в порядке убывания");
    }

    /**
     * Конструктор класса.
     *
     * @param collectionManager менеджер коллекции
     */
    public PrintFieldDescendingPrice(CollectionManager collectionManager) {
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
            List<Product> productList = new ArrayList<>(collectionManager.getCollection().values());
            productList.sort((product1, product2) -> product2.getPrice().compareTo(product1.getPrice()));

            List<Integer> prices = new ArrayList<>();
            for (Product product : productList) {
                prices.add(product.getPrice());
            }
            StringBuilder output = new StringBuilder();
            for (Integer price : prices) {
                output.append(price).append(System.lineSeparator());
            }

            return new Response(true, output.toString());
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
