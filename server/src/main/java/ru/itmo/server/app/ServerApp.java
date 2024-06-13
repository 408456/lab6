package ru.itmo.server.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.utility.io.Console;
import ru.itmo.server.dao.ProductDAO;
import ru.itmo.server.dao.UserDAO;
import ru.itmo.server.managers.CommandManager;
import ru.itmo.server.managers.DatabaseManager;
import ru.itmo.server.managers.ProductCollectionManager;
import ru.itmo.server.network.TCPReader;
import ru.itmo.server.network.TCPServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Класс для запуска серверного приложения.
 */
public class ServerApp {
    private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);
    private static final Properties properties = new Properties();
    private static int PORT;

    static {
        try (InputStream input = ServerApp.class.getClassLoader().getResourceAsStream("server.properties")) {
            if (input == null) {
                logger.error("Sorry, unable to find server.properties");
                throw new IOException("Unable to find server.properties");
            }
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            PORT = Integer.parseInt(properties.getProperty("server.port"));
        } catch (IOException ex) {
            logger.error("Error loading properties file", ex);
        }
    }

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
        DatabaseManager.createDatabaseIfNotExists();
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();
        TCPReader.setUserDAO(userDAO);
        ProductCollectionManager productCollectionManager = new ProductCollectionManager(productDAO, userDAO);

        CommandManager.initServerCommands(productCollectionManager, userDAO);

        new Runner().start();
        new TCPServer(PORT).start();
    }
}
