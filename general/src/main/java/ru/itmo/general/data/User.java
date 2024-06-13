package ru.itmo.general.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс {@code User} пользователя
 * Используется для управления аутентификацией и регистрацией пользователей.
 *
 */
@Getter
public class User {
    private final String username; // Имя пользователя
    private final String passwordHash; // Хеш пароля пользователя
    private final String salt; // Для хеширования
    private final LocalDateTime registrationDate; // Дата регистрации пользователя
    @Setter
    private Integer id; // Идентификатор пользователя

    /**
     * Создает новый объект пользователя с указанными параметрами.
     *
     * @param username         имя пользователя
     * @param passwordHash     хеш пароля пользователя
     * @param salt             СОЛЬ
     * @param registrationDate дата регистрации пользователя
     */
    public User(String username, String passwordHash, String salt, LocalDateTime registrationDate) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.registrationDate = registrationDate;
    }

    /**
     * Создает новый объект пользователя с указанными параметрами.
     *
     * @param id               идентификатор пользователя
     * @param username         имя пользователя
     * @param passwordHash     хеш пароля пользователя
     * @param salt             СОЛЬ
     * @param registrationDate дата регистрации пользователя
     */
    public User(Integer id, String username, String passwordHash, String salt, LocalDateTime registrationDate) {
        this(username, passwordHash, salt, registrationDate);
        this.id = id;
    }

    /**
     * Возвращает строковое представление объекта пользователя.
     *
     * @return строковое представление объекта пользователя
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", salt='" + salt + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }

    /**
     * Проверяет, является ли объект пользователя допустимым.
     * Проверяет, что хеш пароля и соль не являются null и имеют минимальную длину 8 символов,
     * а также что дата регистрации не является null.
     *
     * @return true, если объект пользователя допустим, false в противном случае
     */
    public boolean validate() {
        if (passwordHash == null || passwordHash.length() < 8) {
            return false;
        }
        if (salt == null || salt.length() < 8) {
            return false;
        }
        return registrationDate != null;
    }
}
