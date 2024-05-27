package ru.itmo.client.app;

import ru.itmo.general.comands.*;
import ru.itmo.general.managers.CommandManager;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.InputSteamer;

import java.util.Scanner;

/**
 * Основной класс клиентского приложения.
 */
public class ClientApp {
    private static final int PORT = 3333; /**< Порт для подключения к серверу */
    private static final String SERVER_ADDRESS = "localhost"; /**< Адрес сервера */

    /**
     * Точка входа в программу.
     * Инициализирует сканер для чтения пользовательского ввода,
     * создает объект Console и CommandManager с набором команд,
     * и запускает Executor для обработки команд из консоли.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        InputSteamer.setScanner(new Scanner(System.in));
        Console console = new Console();

        CommandManager commandManager = new CommandManager() {
            {
                commandAdd("clear", new Clear());
                commandAdd("count_less_than_owner", new CountLessThanOwner(console));
                commandAdd("execute_script", new ExecuteScript());
                commandAdd("exit", new Exit());
                commandAdd("help", new Help(this));
                commandAdd("history", new History(this));
                commandAdd("info", new Info());
                commandAdd("insert", new Insert(console));
                commandAdd("print_field_ascending_owner", new PrintFieldAscendingOwner());
                commandAdd("print_field_descending_price", new PrintFieldDescendingPrice());
                commandAdd("remove_greater", new RemoveGreater(console));
                commandAdd("remove_greater_key", new RemoveGreaterKey());
                commandAdd("remove_lower_key", new RemoveLowerKey());
                commandAdd("show", new Show());
                commandAdd("update", new Update(console));
            }
        };

        new Executor(console, commandManager, PORT, SERVER_ADDRESS).fromConsole();
    }
}
