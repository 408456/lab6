package ru.itmo.server.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.comands.*;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.managers.CommandManager;
import ru.itmo.server.managers.DumpManager;
import ru.itmo.server.managers.ProductCollectionManager;
import ru.itmo.server.network.TCPServer;

public class ServerApp {
    private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);
    private static final int PORT = 3333;

    public static void main(String[] args) {
        // Установка кодировки UTF-8 для корректной работы с символами Unicode
        System.setProperty("file.encoding", "UTF-8");

        // Проверка наличия аргумента командной строки (пути к файлу)
        if (args.length == 0) {
            logger.error("Необходимо ввести имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }
        Console console = new Console();
        DumpManager dumpManager = new DumpManager(args[0], console);
        ProductCollectionManager productCollectionManager = new ProductCollectionManager(dumpManager);
        CommandManager commandManager = new CommandManager() {
            {
                commandAdd("clear", new Clear(productCollectionManager));
                commandAdd("count_less_than_owner", new CountLessThanOwner(productCollectionManager));
                commandAdd("exit", new Exit(productCollectionManager));
                commandAdd("info", new Info(productCollectionManager));
                commandAdd("insert", new Insert(productCollectionManager));
                commandAdd("print_field_ascending_owner", new PrintFieldAscendingOwner(productCollectionManager));
                commandAdd("print_field_descending_price", new PrintFieldDescendingPrice(productCollectionManager));
                commandAdd("remove_greater", new RemoveGreater(productCollectionManager));
                commandAdd("remove_greater_key", new RemoveGreaterKey(productCollectionManager));
                commandAdd("remove_lower_key", new RemoveLowerKey(productCollectionManager));
                commandAdd("show", new Show(productCollectionManager));
                commandAdd("update", new Update(productCollectionManager));
            }
        };
        // Добавляем shutdown hook для корректного завершения работы
        Runtime.getRuntime().addShutdownHook(new Thread(productCollectionManager::saveCollection));
        new Runner().start();
        new TCPServer(PORT).start();
    }
}
