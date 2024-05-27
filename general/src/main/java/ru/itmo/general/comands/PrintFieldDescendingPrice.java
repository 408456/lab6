package ru.itmo.general.comands;

import ru.itmo.general.data.Product;
import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда для вывода значения поля price всех элементов в порядке убывания.
 */
public class PrintFieldDescendingPrice extends Command {
    private CollectionManager collectionManager;

    public PrintFieldDescendingPrice() {
        super("print_field_descending_price", " - вывести значения поля price всех элементов в порядке убывания");
    }

    public PrintFieldDescendingPrice(CollectionManager collectionManager) {
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
