package ru.itmo.lab5.comands;

import ru.itmo.lab5.comands.Command;
import ru.itmo.lab5.data.Person;
import ru.itmo.lab5.exceptions.IncorrectScriptException;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.input.ProductInput;
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
     * @param console           объект класса Console для взаимодействия с пользователем
     * @param collectionManager объект класса CollectionManager для управления коллекцией
     */
    public CountLessThanOwner(Console console, CollectionManager collectionManager) {
        super("count_less_than_owner_by_name <owner>", "вывести количество элементов, значение поля owner которых меньше заданного");
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
            String ownerName;

            if (args.length != 2) {
                throw new InvalidAmountException();
            }

            ownerName = args[1];

            if (ownerName == null || ownerName.isEmpty()) {
                throw new IllegalArgumentException("Имя владельца не может быть null или пустой строкой!");
            }
            ProductInput productInput = new ProductInput(console);
            Person owner = productInput.inputPersonForCountLess(ownerName);
            Long countProduct = collectionManager.countLessThanOwner(owner);

            if (countProduct == 0) {
                console.println("Продуктов, у которых owner меньше указанного, не найдено!");
            } else {
                console.println("Найдено " + countProduct + " продуктов, у которых owner меньше указанного.");
            }

        } catch (InvalidAmountException e) {
            console.printError("Неправильное количество аргументов!");
        } catch (NumberFormatException e) {
            console.printError("Подстрока должна быть представлена числом!");
        } catch (IllegalArgumentException e) {
            console.printError(e.getMessage());
        } catch (IncorrectScriptException e) {
            console.printError("Ошибка при выполнении скрипта: " + e.getMessage());
        }
        return false;
    }

}
