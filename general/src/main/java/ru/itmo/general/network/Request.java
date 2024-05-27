package ru.itmo.general.network;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс для представления запроса к серверу.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Request extends Sendable {
    private static final long serialVersionUID = 1L;

    /**
     * Конструктор с параметрами.
     *
     * @param success Флаг успешности выполнения
     * @param name    Название запроса
     * @param data    Данные запроса
     */
    public Request(boolean success, String name, Object data) {
        super(success, name, data);
    }

    /**
     * Конструктор с параметрами.
     *
     * @param name Название запроса
     * @param data Данные запроса
     */
    public Request(String name, Object data) {
        this(true, name, data);
    }

    /**
     * Получить название команды.
     *
     * @return Название команды
     */
    public String getCommand() {
        return getMessage();
    }

    /**
     * Переопределенный метод toString.
     *
     * @return Строковое представление объекта Request
     */
    @Override
    public String toString() {
        return "Request{" +
                (isSuccess() ? "" : "Ошибка при выполнении команды") +
                "command='" + getCommand() + '\'' +
                (getData() != null ? "data=" + getData() : "") +
                '}';
    }
}
