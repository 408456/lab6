package ru.itmo.lab5.comands;

import ru.itmo.lab5.data.Product;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда для вывода значения поля price всех элементов в порядке убывания.
 */
public class PrintFieldDescendingPrice extends Command {
    private final Console console;          // Консоль для взаимодействия с пользователем
    private final CollectionManager collectionManager; // Менеджер коллекции

    /**
     * Конструктор класса.
     *
     * @param console            объект класса Console для взаимодействия с пользователем
     * @param collectionManager объект класса CollectionManager для управления коллекцией
     */
    public PrintFieldDescendingPrice(Console console, CollectionManager collectionManager) {
        super("print_field_descending_price", "вывести значения поля price всех элементов в порядке убывания");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода значения поля price всех элементов в порядке убывания.
     *
     * @param args аргументы команды (в данном случае не используются)
     * @return true, если команда выполнена успешно, иначе false
     */
    @Override
    public boolean execute(String[] args) {
        try {
            List<Product> productList = new ArrayList<>(collectionManager.getCollection().values());
            productList.sort((product1, product2) -> product2.getPrice().compareTo(product1.getPrice()));
            productList.forEach(product -> console.println(product.getPrice()));
            return true;
        } catch (Exception e) {
            console.printError("Произошла ошибка при выполнении команды: " + e.getMessage());
            return false;
        }
    }
}
