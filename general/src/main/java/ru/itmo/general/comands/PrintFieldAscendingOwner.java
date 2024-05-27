package ru.itmo.lab5.comands;

import ru.itmo.lab5.data.Product;
import ru.itmo.lab5.data.Person; // Добавлен импорт для Person
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

import java.util.Comparator;

/**
 * Команда для вывода полей owner всех элементов в порядке возрастания.
 */
public class PrintFieldAscendingOwner extends Command {
    private final Console console;          // Консоль для взаимодействия с пользователем
    private final CollectionManager collectionManager; // Менеджер коллекции

    public PrintFieldAscendingOwner(Console console, CollectionManager collectionManager) {
        super("print_field_ascending_owner", "вывести значения поля owner всех элементов в порядке возрастания");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            StringBuilder output = new StringBuilder();

            if (collectionManager.getCollection().isEmpty()) {
                console.printError("Коллекция пуста!");
                return false;
            }

            collectionManager.getCollection().values().stream()
                    .filter(product -> product.getOwner() != null)  // Фильтрация для null owner
                    .map(Product::getOwner)
                    .sorted(Comparator.comparing(Person::getPassportID)) // Сортировка по passportID
                    .distinct()
                    .forEach(owner -> output.append(owner.toString()).append(System.lineSeparator()));

            console.println(output.toString());
            return true;
        } catch (Exception e) {
            console.printError("Произошла ошибка при выполнении команды: " + e.getMessage());
            return false;
        }
    }

}
