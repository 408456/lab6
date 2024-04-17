package ru.itmo.lab5.comands;

import ru.itmo.lab5.data.Person;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для вывода количества элементов, значение поля age которых меньше заданного.
 */
public class CountLessThanOwner extends Command {
    private final Console console;          // Консоль для взаимодействия с пользователем
    private final CollectionManager collectionManager; // Менеджер коллекции

    /**
     * Конструктор класса.
     *
     * @param console          объект класса Console для взаимодействия с пользователем
     * @param collectionManager объект класса CollectionManager для управления коллекцией
     */
    public CountLessThanOwner(Console console, CollectionManager collectionManager) {
        super("count_less_than_owner <owner>", "вывести количество элементов, значение поля owner которых меньше заданного");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода количества элементов, значение поля owner которых меньше заданного.
     *
     * @param args аргументы команды
     * @return false, так как команда не завершает работу программы
     */
    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 2) throw new InvalidAmountException();

            // Создание объекта Person из аргумента команды
            Person owner = new Person(args[1]);

            // Получение количества элементов, значение поля owner которых меньше заданного
            Integer countProduct = collectionManager.countLessThanOwner(owner);
            if (countProduct == 0)
                console.println("Продуктов, у которых поле owner меньше заданной подстроки не обнаружено!");
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
