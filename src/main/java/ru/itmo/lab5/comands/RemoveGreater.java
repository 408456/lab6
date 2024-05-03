package ru.itmo.lab5.comands;

import ru.itmo.lab5.exceptions.IncorrectScriptException;
import ru.itmo.lab5.exceptions.InvalidAmountException;
import ru.itmo.lab5.exceptions.InvalidFormException;
import ru.itmo.lab5.exceptions.InvalidValueException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.input.ProductInput;
import ru.itmo.lab5.managers.CollectionManager;

/**
 * Команда для удаления из коллекции элементов, превышающих заданный(сортировка по цени и по имени).
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
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления из коллекции элементов, превышающих заданный.
     *
     * @param args аргументы команды (в данном случае не используются)
     * @return true, если команда выполнена успешно, иначе false
     */
    @Override
    public boolean execute(String[] args) {
        try {
            if (!args[1].isEmpty()) throw new InvalidAmountException();
//            if (!collectionManager.canWriteToFile()) {
//                console.printError("Нет прав на запись в файл! Выполнить команду " + getName() + " невозможно!");
//                return false;
//            }
            ru.itmo.lab5.data.Product product = ((new ProductInput(console).make()));
            collectionManager.removeGreater(product);
            console.println("Продукты успешно удалены!");
            return true;
        } catch (InvalidAmountException exception) {
            console.printError("Неправильное количество аргументов!");
        } catch (InvalidFormException | InvalidValueException e) {
            console.printError("Поля продукта не валидны! Продукт не создан!");
        } catch (IncorrectScriptException ignored) {
        }
        return false;
    }

}
