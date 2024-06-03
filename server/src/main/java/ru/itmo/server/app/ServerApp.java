package ru.itmo.server.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.managers.CommandManager;
import ru.itmo.server.managers.DumpManager;
import ru.itmo.server.managers.ProductCollectionManager;
import ru.itmo.server.network.TCPServer;

/**
 * Класс для запуска серверного приложения.
 */
public class ServerApp {
    private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);
    private static final int PORT = 8001;

    /**
     * Точка входа в приложение сервера.
     *
     * @param args Аргументы командной строки
     */
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

        CommandManager commandManager = new CommandManager();
        CommandManager.initServerCommands(productCollectionManager);

        // Добавляем shutdown hook для корректного завершения работы
        Runtime.getRuntime().addShutdownHook(new Thread(productCollectionManager::saveCollection));
        new Runner().start();
        new TCPServer(PORT).start();
    }
}
