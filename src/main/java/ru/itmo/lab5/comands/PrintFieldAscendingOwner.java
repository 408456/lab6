package ru.itmo.lab5.comands;

import ru.itmo.lab5.data.Product; // Добавлен импорт
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;
import ru.itmo.lab5.data.Person; // Добавлен импорт

import java.util.Comparator;
import java.util.Objects;

/**
 * Команда для вывода полей owner всех элементов в порядке возрастания
 */
public class PrintFieldAscendingOwner extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintFieldAscendingOwner(Console console, CollectionManager collectionManager) {
        super("print_field_ascending_owner", "вывести значения поля owner всех элементов в порядке возрастания");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 1) {
                throw new InvalidAmountException();
            }

            StringBuilder output = new StringBuilder();
            collectionManager.getCollection().values().stream()
                    .map(Product::getOwner)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted(Comparator.comparing(Person::getPassportID)) // Сортировка по passportID
                    .forEach(owner -> output.append(owner.getName()).append(System.lineSeparator()));

            console.println(output.toString());
            return true;
        } catch (InvalidAmountException e) {
            console.printError("Неправильное количество аргументов!");
            return false;
        }
    }
}
