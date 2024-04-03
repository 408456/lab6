package ru.itmo.lab5.comands;

import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.managers.CollectionManager;

import java.time.LocalDateTime;

/**
 * Команда для вывода информации о коллекции
 *
 */
public class Info extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionController) {
        super("info", "вывести информацию о коллекции");
        this.console = console;
        this.collectionManager = collectionController;
    }

    @Override
    public boolean execute(String[] args) {
        if (!args[1].isEmpty()) {
            console.println("Пожалуйста введите команду в правильном формате");
            return false;
        }
        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeStr = (lastInitTime == null) ? "Инициализации в данной сессии еще не происходило" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();
        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeStr = (lastSaveTime == null) ? "Сохранения в данной сессии еще не происходило" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();
        console.println("Информация о коллекции:");
        console.println("Тип коллекции: " + collectionManager.getType());
        console.println("Размер коллекции (количество элементов в коллекции): " + collectionManager.getSize());
        console.println("Дата и время последней инициализации: " + lastInitTimeStr);
        console.println("Дата и время последнего сохранения: " + lastSaveTimeStr);
        return true;
    }
}

