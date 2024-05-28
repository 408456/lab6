package ru.itmo.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.managers.CommandManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import static ru.itmo.server.network.TCPWriter.sendResponse;

/**
 * Класс для чтения данных из сокета.
 */
public class TCPReader {

    private static final Logger logger = LoggerFactory.getLogger(TCPReader.class);
    private final SelectionKey key;
    private static final int BUFFER_SIZE = 8192; // Размер буфера

    /**
     * Конструктор с параметрами.
     *
     * @param key Ключ выборки
     */
    public TCPReader(final SelectionKey key) {
        this.key = key;
    }

    /**
     * Метод для разбора запроса.
     */
    public void parseRequest() {
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bytesRead;

        try {
            bytesRead = readFromChannel(clientSocketChannel, buffer, byteArrayOutputStream);
            if (bytesRead == -1) {
                handleClientDisconnect(clientSocketChannel);
                return;
            }
        } catch (IOException e) {
            handleReadError(clientSocketChannel, e);
            return;
        }

        processRequest(clientSocketChannel, byteArrayOutputStream.toByteArray());
    }

    private int readFromChannel(SocketChannel clientSocketChannel, ByteBuffer buffer, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        int bytesRead;
        while ((bytesRead = clientSocketChannel.read(buffer)) > 0) {
            buffer.flip();
            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);
            byteArrayOutputStream.write(byteArray);
            buffer.clear();
        }
        return bytesRead;
    }


    private void handleClientDisconnect(SocketChannel clientSocketChannel) throws IOException {
        key.cancel();
        clientSocketChannel.close();
//        CommandManager.handleServer(new Request(true, "save", null));
        logger.info("Client {} disconnected. Collection saved", clientSocketChannel.getRemoteAddress());
    }

    private void handleReadError(SocketChannel clientSocketChannel, IOException e) {
        logger.error("Error reading data from {}: {}", getRemoteAddress(clientSocketChannel), e.getMessage());
        key.cancel();
        try {
            clientSocketChannel.close();
        } catch (IOException ce) {
            logger.error("Error closing channel for {}: {}", getRemoteAddress(clientSocketChannel), ce.getMessage());
        }
    }

    private void processRequest(SocketChannel clientSocketChannel, byte[] requestBytes) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(requestBytes))) {
            Request request = (Request) objectInputStream.readObject();
            handleRequest(clientSocketChannel, request);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error processing request: {}", e.getMessage());
            Response response = new Response(false, "Invalid request");
            sendResponse(clientSocketChannel, response);
        }
    }

    private void handleRequest(SocketChannel clientSocketChannel, Request request) throws IOException {
        if ("exit".equals(request.getCommand())) {
            logger.info("Client {} terminated", clientSocketChannel.getRemoteAddress());
            clientSocketChannel.close();
        } else {
            Response response = CommandManager.handle(request);
            sendResponse(clientSocketChannel, response);
        }
    }

    private String getRemoteAddress(SocketChannel clientSocketChannel) {
        try {
            return clientSocketChannel.getRemoteAddress().toString();
        } catch (IOException e) {
            logger.error("Unable to get remote address: {}", e.getMessage());
            return "unknown";
        }
    }
}
