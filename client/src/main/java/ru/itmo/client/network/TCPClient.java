package ru.itmo.client.network;

import ru.itmo.general.utility.io.Console;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * Класс для установления и управления TCP-соединением с сервером.
 */
public class TCPClient {

    private final Console output;
    /**
     * < Консоль для вывода информации и сообщений об ошибках
     */
    private final String serverAddress;
    /**
     * < Адрес сервера
     */
    private final int serverPort;
    /**
     * < Порт сервера
     */
    private SocketChannel socketChannel; /**< Канал сокета для TCP-соединения */

    /**
     * Конструктор класса.
     *
     * @param serverAddress Адрес сервера
     * @param serverPort    Порт сервера
     * @param output        Консоль для вывода информации и сообщений об ошибках
     */
    public TCPClient(String serverAddress, int serverPort, Console output) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.output = output;
    }

    /**
     * Устанавливает соединение с сервером.
     *
     * @return true, если соединение установлено успешно, в противном случае - false
     * @throws TimeoutException если истекло время ожидания подключения
     */
    public boolean connect() throws TimeoutException {
        Selector selector = null;
        boolean connectFlag = false;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress(serverAddress, serverPort);
            socketChannel.connect(address);

            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 10000) {
                if (selector.select(1000) == 0) {
                    continue;
                }

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isConnectable()) {
                        try {
                            connectFlag = socketChannel.finishConnect();
                        } catch (IOException ignored) {
                        }
                        if (connectFlag) {
                            output.println("Подключено к серверу: " + serverAddress + ":" + serverPort);
                            return true;
                        }
                    }
                }
            }
            throw new TimeoutException("Не удалось подключиться в течение 10 секунд");
        } catch (IOException e) {
            output.println("Ошибка при подключении к серверу: " + e.getMessage());
            return false;
        } finally {
            if (!connectFlag) {
                closeResources(socketChannel, selector);
            }
        }
    }

    /**
     * Проверяет текущее соединение с сервером.
     *
     * @return true, если есть активное соединение, в противном случае - false
     */
    public boolean ensureConnection() {
        if (!isConnected()) {
            output.println("Нет подключения к серверу.");
            try {
                output.println("Попытка повторного подключения к серверу...");
                connect();
            } catch (TimeoutException e) {
                output.printError("Ошибка переподключения: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Разрывает соединение с сервером.
     *
     * @throws IOException если возникает ошибка при разрыве соединения
     */
    public void disconnect() throws IOException {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }

    /**
     * Отправляет запрос на сервер.
     *
     * @param request объект запроса
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public void sendRequest(Request request) throws IOException {
        if (!ensureConnection()) throw new IOException("Соединение не установлено");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
        }
        byte[] requestBytes = byteArrayOutputStream.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(requestBytes);
        socketChannel.write(buffer);
    }

    /**
     * Получает ответ от сервера.
     *
     * @return объект ответа от сервера
     * @throws IOException            если возникает ошибка ввода-вывода
     * @throws ClassNotFoundException если класс не найден при десериализации
     */
    public Response receiveResponse() throws IOException, ClassNotFoundException {
        if (!ensureConnection()) throw new IOException("Соединение не установлено");
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        ByteBuffer buffer = ByteBuffer.allocate(16384);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 10000) {
            if (selector.select(10000) == 0) {
                continue;
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                if (key.isReadable()) {
                    int bytesRead;
                    while ((bytesRead = socketChannel.read(buffer)) > 0) {
                        buffer.flip();
                        byteArrayOutputStream.write(buffer.array(), 0, bytesRead);
                        buffer.clear();
                    }
                    if (bytesRead == -1) {
                        socketChannel.close();
                    }
                }
            }

            byte[] responseBytes = byteArrayOutputStream.toByteArray();
            if (responseBytes.length > 0) {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(responseBytes))) {
                    return (Response) objectInputStream.readObject();
                }
            }
        }
        throw new IOException("Не удалось получить ответ от сервера в течение 10 секунд");
    }

    /**
     * Отправляет команду на сервер и получает ответ.
     *
     * @param request объект запроса
     * @return объект ответа от сервера
     */
    public Response sendCommand(Request request) {
        try {
            sendRequest(request);
            return receiveResponse();
        } catch (IOException | ClassNotFoundException e) {
            output.printError("Ошибка при отправке команды: " + e.getMessage());
        }
        output.printError("Запрос не отправлен. Повторите попытку позже.");
        try {
            disconnect();
        } catch (IOException e) {
            output.printError("Не удалось закрыть соединение");
        }
        return new Response(false, "Команда не выполнена!", null);
    }

    /**
     * Проверяет, установлено ли соединение с сервером.
     *
     * @return true, если соединение установлено, в противном случае - false
     */
    public boolean isConnected() {
        return socketChannel != null && socketChannel.isConnected();
    }

    /**
     * Закрывает ресурсы канала сокета и селектора.
     *
     * @param socketChannel канал сокета
     * @param selector      селектор
     */
    private void closeResources(SocketChannel socketChannel, Selector selector) {
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            output.println("Ошибка при закрытии ресурсов: " + e.getMessage());
        }
    }
}
