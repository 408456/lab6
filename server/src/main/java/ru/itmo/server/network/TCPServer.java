package ru.itmo.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Класс для запуска TCP-сервера.
 */
public class TCPServer {
    private final int port;
    private static final Logger logger = LoggerFactory.getLogger(TCPServer.class);
    private ExecutorService readPool;
    private ExecutorService handlePool;

    /**
     * Конструктор с параметрами.
     *
     * @param port порт для прослушивания подключений
     */
    public TCPServer(int port) {
        this.port = port;
        readPool = Executors.newCachedThreadPool(); // Кэшированный пул потоков для чтения
        handlePool = Executors.newFixedThreadPool(10); // Фиксированный пул потоков для обработки запросов (10 потоков)
        TCPReader.setHandlePool(handlePool);
    }
    /**
     * Метод для запуска сервера.
     */
    public void start() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Сервер запущен на порту {}", port);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (selector.select() > 0) processSelectedKeys(selector);
                } catch (IOException e) {
                    logger.error("Ошибка в процессе выбора ключей: ", e);
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка при запуске сервера: ", e);
        }
    }

    private void processSelectedKeys(Selector selector) throws IOException {
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            try {
                if (key.isAcceptable()) {
                    acceptConnection(key);
                } else if (key.isReadable()) {
                    key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
                    logger.info("Starting read thread from selection key: {}", ((SocketChannel) key.channel()).getRemoteAddress());
                    readPool.submit(() -> {
                        try {
                            new TCPReader(key).parseRequest(); // Обработка запроса
                        } finally {
                            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                            selector.wakeup(); // Пробуждение селектора
                        }
                    });
                }
            } catch (IOException e) {
                logger.error("Ошибка при обработке ключа: ", e);
                key.cancel();
                try {
                    key.channel().close();
                } catch (IOException ex) {
                    logger.error("Ошибка при закрытии канала: ", ex);
                }
            }

            keyIterator.remove();
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel client = serverSocketChannel.accept();
        if (client != null) {
            client.configureBlocking(false);
            client.register(key.selector(), SelectionKey.OP_READ);
            logger.info("Новое подключение: {}", client.getRemoteAddress());
        }
    }
}
