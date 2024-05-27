package ru.itmo.general.network;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс для представления ответа от сервера.
 */
@Getter
@Setter
@ToString
public class Response extends Sendable {

    /**
     * Конструктор с параметрами.
     *
     * @param success Флаг успешности выполнения
     * @param message Сообщение
     * @param data    Данные ответа
     */
    public Response(boolean success, String message, Object data) {
        super(success, message, data);
    }

    /**
     * Конструктор с параметрами.
     *
     * @param success Флаг успешности выполнения
     * @param message Сообщение
     */
    public Response(boolean success, String message) {
        super(success, message, null);
    }

    /**
     * Конструктор с параметрами.
     *
     * @param success Флаг успешности выполнения
     */
    public Response(boolean success) {
        super(success, null, null);
    }

    /**
     * Переопределенный метод toString.
     *
     * @return Строковое представление объекта Response
     */
    @Override
    public String toString() {
        return ((message != null) ? message : "") +
                (data != null ? ((message != null) ? '\n' + data.toString() : data.toString()) : "");
    }
}
