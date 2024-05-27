package ru.itmo.general.network;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * Абстрактный класс для представления отправляемых объектов.
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class Sendable implements Serializable {
    protected final boolean success; /**< Флаг успешности операции */
    protected final String message; /**< Сообщение */
    protected final Object data; /**< Данные */
    protected Integer userId; /**< Идентификатор пользователя */

    /**
     * Конструктор с параметрами.
     *
     * @param success Флаг успешности операции
     * @param name    Название сообщения
     * @param data    Данные
     */
    public Sendable(boolean success, String name, Object data) {
        this.success = success;
        this.message = name;
        this.data = data;
    }
}
