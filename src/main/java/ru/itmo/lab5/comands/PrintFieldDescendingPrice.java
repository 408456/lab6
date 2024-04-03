package ru.itmo.lab5.comands;

import ru.itmo.lab5.data.Product;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда для вывода значения поля price всех элементов в порядке убывания
 */
public class PrintFieldDescendingPrice extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintFieldDescendingPrice(Console console, CollectionManager collectionManager) {
        super("print_field_descending_price", "вывести значения поля price всех элементов в порядке убывания");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 1) {
                throw new InvalidAmountException();
            }
            List<Product> productList = new ArrayList<>(collectionManager.getCollection().values());
            productList.sort((product1, product2) -> product2.getPrice().compareTo(product1.getPrice()));
            productList.forEach(product -> console.println(product.getPrice()));
            return true;
        } catch (InvalidAmountException e) {
            console.printError("Неправильное количество аргументов!");
        } catch (Exception e) {
            console.printError("Произошла ошибка при выполнении команды: " + e.getMessage());
        }
        return false;
    }


}
