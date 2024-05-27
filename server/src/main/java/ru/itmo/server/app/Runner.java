package ru.itmo.server.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.managers.CommandManager;
import ru.itmo.general.network.Request;
import ru.itmo.server.network.TCPServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Класс для запуска серверного приложения.
 */
public class Runner extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);
    private final TCPServer server; /**< Сервер TCP */
    private final ThreadGroup requestReaders; /**< Группа потоков для чтения запросов */
    private final ThreadGroup requestHandlers; /**< Группа потоков для обработки запросов */

    /**
     * Конструктор без параметров.
     * Создает новые группы потоков для чтения и обработки запросов.
     */
    public Runner() {
        server = null;
        requestReaders = new ThreadGroup("RequestReaders");
        requestHandlers = new ThreadGroup("RequestHandlers");
    }

    /**
     * Конструктор с параметрами.
     *
     * @param server          Сервер TCP
     * @param requestReaders  Группа потоков для чтения запросов
     * @param requestHandlers Группа потоков для обработки запросов
     */
    public Runner(TCPServer server, ThreadGroup requestReaders, ThreadGroup requestHandlers) {
        this.server = server;
        this.requestReaders = requestReaders;
        this.requestHandlers = requestHandlers;
    }

    /**
     * Метод запуска потока.
     * Ожидает ввода команд с консоли и выполняет их обработку.
     */
    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (true) {
            try {
                input = in.readLine();
                if (input.equals("exit")) {
                    CommandManager.handleServer(new Request(true, input, null));
                    System.exit(0);
                    break;
                } else if (input.equals("save")) {
                    CommandManager.handleServer(new Request(true, input, null));
                    logger.info("Билеты сохранены в файл");
                }
            } catch (Exception e) {
                logger.error("Ошибка чтения с консоли");
                System.exit(0);
                break;
            }
        }
    }
}
