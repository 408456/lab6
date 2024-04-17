package ru.itmo.lab5.comands;

import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для удаления из коллекции элементов, превышающих заданную цену.
 */
public class RemoveGreater extends Command {
    private final Console console;          // Консоль для взаимодействия с пользователем
    private final CollectionManager collectionManager; // Менеджер коллекции

    /**
     * Конструктор класса.
     *
     * @param console            объект класса Console для взаимодействия с пользователем
     * @param collectionManager объект класса CollectionManager для управления коллекцией
     */
    public RemoveGreater(Console console, CollectionManager collectionManager) {
        super("remove_greater {price}", "удалить из коллекции все элементы, цена которых превышает заданную");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления из коллекции элементов, цена которых превышает заданную.
     *
     * @param args аргументы команды
     * @return true, если команда выполнена успешно, иначе false
     */
    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length != 2) throw new InvalidAmountException();
            Integer price = Integer.parseInt(args[1]);
            collectionManager.removeGreater(price);
            console.println("Продукты успешно удалены!");
            return true;
        } catch (NumberFormatException e) {
            console.printError("Неверный формат цены! Введите целое число.");
        } catch (InvalidAmountException exception) {
            console.printError("Неправильное количество аргументов!");
        }
        return false;
    }
}
