package ru.itmo.server.network;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.data.User;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;
import ru.itmo.server.dao.UserDAO;
import ru.itmo.server.managers.CommandManager;
import ru.itmo.server.utility.PasswordHashing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

/**
 * Класс для чтения данных из сокета.
 */
public class TCPReader implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TCPReader.class);
    private final SelectionKey key;
    private static final int BUFFER_SIZE = 8192; // Размер буфера
    @Setter
    private static ExecutorService handlePool;
    @Setter
    private static UserDAO userDAO;

    /**
     * Конструктор с параметрами.
     *
     * @param key Ключ выборки
     */
    public TCPReader(final SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        parseRequest();
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
        logger.info("Client {} disconnected.", clientSocketChannel.getRemoteAddress());
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
            User user = null;
            if (request.getLogin() != null) {
                user = userDAO.getUserByUsername(request.getLogin());
            }

            if ((user == null || !PasswordHashing.verifyPassword(request.getPassword(), user.getSalt(), user.getPasswordHash()))
                    && !"register".equals(request.getCommand())
                    && !"login".equals(request.getCommand())
                    && !"help".equals(request.getCommand())) {
                sendUnauthorizedResponse(clientSocketChannel);
                return;
            } else if (user != null) {
                request.setUserId(user.getId());
            }
            handlePool.submit(() ->
                    {
                        handleRequest(clientSocketChannel, request);
                    }
            );
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error processing request: {} {}", e, e.getMessage());
            Response response = new Response(false, "Invalid request");
            new TCPWriter(clientSocketChannel, response).start();
        }
    }

    private void handleRequest(SocketChannel clientSocketChannel, Request request) {
        if ("exit".equals(request.getCommand())) {
            try {
                logger.info("Client {} terminated", clientSocketChannel.getRemoteAddress());
                clientSocketChannel.close();
            } catch (IOException e) {
                logger.error("Error closing channel for {}: {}", getRemoteAddress(clientSocketChannel), e.getMessage());
            }
        } else {
            Response response = CommandManager.handle(request);
            new TCPWriter(clientSocketChannel, response).start();
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

    /**
     * Sends an unauthorized response to the client, indicating that authentication is required.
     *
     * @param channel The socket channel to send the response to.
     */
    private void sendUnauthorizedResponse(SocketChannel channel) {
        Response response = new Response(false, "Вы не вошли в систему." + '\n' +
                "Введите register для регистрации или login для входа");
        new TCPWriter(channel, response).start();
    }
}
