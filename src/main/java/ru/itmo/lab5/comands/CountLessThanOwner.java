package ru.itmo.lab5.comands;

import ru.itmo.lab5.data.Person;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для вывода количества элементов, значение поля age которых меньше заданного
 *
 */
public class CountLessThanOwner extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public CountLessThanOwner(Console console, CollectionManager collectionManager) {
        super("count_less_than_owner <owner>", "вывести количество элементов, значение поля owner которых меньше заданного");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 2) throw new InvalidAmountException();

            Person owner = new Person(args[1]);

            Integer countProduct = collectionManager.countLessThanOwner(owner);
            if (countProduct == 0)
                console.println(", у которых поле owner меньше заданной подстроки не обнаружено!");
            else
                console.println(countProduct);

        } catch (InvalidAmountException e) {
            console.printError("Неправильное количество аргументов!");
        } catch (NumberFormatException e) {
            console.printError("Подстрока должна быть представлена числом!");
        }
        return false;
    }
}