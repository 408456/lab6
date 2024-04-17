package ru.itmo.lab5;

import ru.itmo.lab5.comands.*;
import ru.itmo.lab5.managers.CollectionManager;
import ru.itmo.lab5.managers.CommandManager;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.input.InputSteamer;
import ru.itmo.lab5.managers.DumpManager;
import ru.itmo.lab5.utility.Executor;

import java.util.Scanner;

/**
 * Основной класс программы.
 */
public class Main {
    /**
     * Точка входа в программу.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        // Установка кодировки UTF-8 для корректной работы с символами Unicode
        System.setProperty("file.encoding", "UTF-8");

        // Чтение пользовательского ввода из консоли
        InputSteamer.setScanner(new Scanner(System.in));
        Console console = new Console();

        // Проверка наличия аргумента командной строки (пути к файлу)
        if (args.length == 0) {
            console.printError("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }
        DumpManager dumpManager = new DumpManager(args[0], console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);

        // Проверка валидности коллекции и её элементов
        collectionManager.validateAll(console);

        // Создание менеджера команд и добавление команд
        CommandManager commandManager = new CommandManager() {
            {
                commandAdd("clear", new Clear(console, collectionManager));
                commandAdd("count_less_than_owner", new CountLessThanOwner(console, collectionManager));
                commandAdd("execute_script", new ExecuteScript(console));
                commandAdd("exit", new Exit(console));
                commandAdd("help", new Help(console, this));
                commandAdd("history", new History(console, this));
                commandAdd("info", new Info(console, collectionManager));
                commandAdd("insert", new Insert(console, collectionManager));
                commandAdd("print_field_ascending_owner", new PrintFieldAscendingOwner(console, collectionManager));
                commandAdd("print_field_descending_price", new PrintFieldDescendingPrice(console, collectionManager));
                commandAdd("remove_greater", new RemoveGreater(console, collectionManager));
                commandAdd("remove_greater_key", new RemoveGreaterKey(console, collectionManager));
                commandAdd("remove_lower_key", new RemoveLowerKey(console, collectionManager));
                commandAdd("save", new Save(console, collectionManager));
                commandAdd("show", new Show(console, collectionManager));
                commandAdd("update", new Update(console, collectionManager));
            }
        };
        new Executor(console, commandManager).fromConsole();
    }
}
