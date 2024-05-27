package ru.itmo.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.network.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TCPWriter {
    private static final Logger logger = LoggerFactory.getLogger(TCPWriter.class);

    public static void sendResponse(SocketChannel clientSocketChannel, Response response) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            logger.debug("Отправка ответа клиенту {}", getRemoteAddress(clientSocketChannel));
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException e) {
            logger.error("Ошибка при сериализации ответа: {}", e.getMessage());
            return;  // Прекращаем выполнение, так как сериализация не удалась
        }

        byte[] responseBytes = byteArrayOutputStream.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(responseBytes);

        while (buffer.hasRemaining()) {
            try {
                clientSocketChannel.write(buffer);
            } catch (IOException e) {
                logger.error("Ошибка при отправке данных клиенту {}: {}", getRemoteAddress(clientSocketChannel), e.getMessage());
                handleIOException(clientSocketChannel, e);  // Обработка ошибки отправки данных
                return;
            }
        }
    }

    private static String getRemoteAddress(SocketChannel clientSocketChannel) {
        try {
            return clientSocketChannel.getRemoteAddress().toString();
        } catch (IOException e) {
            logger.error("Не удалось получить удаленный адрес: {}", e.getMessage());
            return "unknown";
        }
    }

    private static void handleIOException(SocketChannel clientSocketChannel, IOException e) {
        try {
            logger.error("Закрытие соединения с клиентом {} из-за ошибки: {}", getRemoteAddress(clientSocketChannel), e.getMessage());
            clientSocketChannel.close();
        } catch (IOException ex) {
            logger.error("Ошибка при закрытии соединения с клиентом {}: {}", getRemoteAddress(clientSocketChannel), ex.getMessage());
        }
    }
}
