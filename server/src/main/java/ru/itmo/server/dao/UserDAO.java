package ru.itmo.server.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.data.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

import static ru.itmo.server.managers.ConnectionManager.*;
import static ru.itmo.server.utility.PasswordHashing.generateSalt;
import static ru.itmo.server.utility.PasswordHashing.hashPassword;

/**
 * Класс UserDAO предоставляет методы для взаимодействия с таблицей пользователей в базе данных.
 * Он обрабатывает создание, извлечение, обновление пользователей и проверку паролей.
 */
public class UserDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger("UserDAO");
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = UserDAO.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                LOGGER.error("Извините, не удалось найти database.properties");
                throw new IOException("Не удалось найти database.properties");
            }
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            LOGGER.error("Ошибка загрузки файла свойств", ex);
        }
    }

    private static final String CREATE_USERS_TABLE_SQL = properties.getProperty("create_users_table");
    private static final String SELECT_ALL_USERS_SQL = properties.getProperty("select_all_users");
    private static final String SELECT_USER_BY_USERNAME_SQL = properties.getProperty("select_user_by_username");
    private static final String INSERT_USER_BY_SQL = properties.getProperty("insert_user");
    private static final String UPDATE_USER_BY_ID_SQL = properties.getProperty("update_user_by_id");
    private static final String SELECT_USER_BY_ID_SQL = properties.getProperty("select_user_by_id");
    private static final String UPDATE_USER_BY_USERNAME_AND_PASSWORD_SQL = properties.getProperty("update_user_by_username_and_password");
    private static final String SELECT_SALT_BY_USERNAME_SQL = properties.getProperty("select_salt_by_username");

    /**
     * Вставляет нового пользователя в базу данных.
     *
     * @param username Имя пользователя нового пользователя.
     * @param password Пароль нового пользователя.
     * @return Созданный объект User в случае успеха, в противном случае null.
     */
    public User insertUser(String username, String password) {
        // Генерируем случайную соль
        String salt = generateSalt(16);

        // Хешируем пароль с солью
        String hashedPassword = hashPassword(password, salt);

        // Создаем нового пользователя с предоставленными данными
        LocalDateTime registrationDate = LocalDateTime.now();
        User user = new User(username, hashedPassword, salt, registrationDate);

        // Вставляем пользователя в базу данных
        if (insertUser(user.getUsername(), user.getPasswordHash(),
                user.getSalt(), registrationDate, registrationDate)) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * Вставляет нового пользователя в базу данных с указанными данными.
     *
     * @param username         Имя пользователя нового пользователя.
     * @param passwordHash     Хешированный пароль нового пользователя.
     * @param salt             Соль, использованная для хеширования пароля.
     * @param registrationDate Дата регистрации нового пользователя.
     * @param lastLoginDate    Дата последнего входа нового пользователя.
     * @return true, если пользователь был успешно вставлен, в противном случае false.
     */
    public boolean insertUser(String username, String passwordHash,
                              String salt, LocalDateTime registrationDate,
                              LocalDateTime lastLoginDate) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER_BY_SQL)) {
            statement.setString(1, username);
            statement.setString(2, passwordHash);
            statement.setString(3, salt);
            statement.setObject(4, registrationDate);
            statement.setObject(5, lastLoginDate);
            return executePrepareUpdate(statement) > 0;
        } catch (SQLException e) {
            LOGGER.error("Ошибка при вставке пользователя: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Извлекает пользователя из базы данных по его имени пользователя.
     *
     * @param username Имя пользователя для извлечения.
     * @return Объект User, если найден, в противном случае null.
     */
    public User getUserByUsername(String username) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_USERNAME_SQL)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String storedUsername = resultSet.getString("username");
                    String passwordHash = resultSet.getString("password_hash");
                    String salt = resultSet.getString("salt");
                    LocalDateTime registrationDate = resultSet.getTimestamp("registration_date").toLocalDateTime();

                    return new User(id, storedUsername, passwordHash, salt, registrationDate);
                } else {
                    return null; // Пользователь не найден
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при извлечении пользователя по имени пользователя: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Обновляет данные пользователя в базе данных.
     *
     * @param userId           ID пользователя для обновления.
     * @param newUsername      Новое имя пользователя.
     * @param newPasswordHash  Новый хешированный пароль пользователя.
     * @param newLastLoginDate Новая дата последнего входа пользователя.
     * @return true, если пользователь был успешно обновлен, в противном случае false.
     */
    public boolean updateUser(int userId, String newUsername, String newPasswordHash, LocalDateTime newLastLoginDate) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_BY_ID_SQL)) {
            statement.setString(1, newUsername);
            statement.setString(2, newPasswordHash);
            statement.setObject(3, newLastLoginDate);
            statement.setInt(4, userId);
            return executePrepareUpdate(statement) > 0;
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            return false;
        }
    }

//    /**
//     * Обновляет пароль пользователя в базе данных.
//     *
//     * @param username    Имя пользователя.
//     * @param newPassword Новый пароль для пользователя.
//     * @return true, если пароль был успешно обновлен, в противном случае false.
//     */
//    public boolean updateUser(String username, String newPassword) {
//
//        try {
//            // Извлекаем соль из базы данных
//            String salt = null;
//            try (Connection connection = getConnection();
//                 PreparedStatement selectStatement = connection.prepareStatement(SELECT_SALT_BY_USERNAME_SQL)) {
//                selectStatement.setString(1, username);
//                try (ResultSet resultSet = selectStatement.executeQuery()) {
//                    if (resultSet.next()) {
//                        salt = resultSet.getString("salt");
//                    } else {
//                        LOGGER.error("Пользователь с именем пользователя {} не найден.", username);
//                        return false; // Пользователь не найден
//                    }
//                }
//            } catch (NullPointerException exception) {
//                LOGGER.error("Ошибка Null pointer при извлечении пользователя по имени пользователя: {}", exception.getMessage());
//            }
//
//            // Хешируем новый пароль, используя извлеченную соль
//            String newPasswordHash = hashPassword(newPassword, salt);
//
//            // Обновляем пароль пользователя в базе данных
//            try (Connection connection = getConnection();
//                 PreparedStatement updateStatement =
//                         connection.prepareStatement(UPDATE_USER_BY_USERNAME_AND_PASSWORD_SQL)) {
//                updateStatement.setString(1, newPasswordHash);
//                updateStatement.setObject(2, LocalDateTime.now());
//                updateStatement.setString(3, username);
//
//                return executePrepareUpdate(updateStatement) > 0;
//            }
//        } catch (SQLException e) {
//            LOGGER.error("Ошибка при обновлении пользователя: {}", e.getMessage());
//            return false;
//        }
//    }

    /**
     * Создает таблицу пользователей в базе данных, если она не существует.
     */
    public void createTablesIfNotExist() {
        Connection connection = getConnection();
        executeUpdate(connection, CREATE_USERS_TABLE_SQL);
    }

    /**
     * Проверяет пароль пользователя по его имени пользователя.
     *
     * @param username Имя пользователя.
     * @param password Пароль для проверки.
     * @return true, если пароль верен, в противном случае false.
     */
    public boolean verifyUserPassword(String username, String password) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID_SQL)) {
            statement.setString(1, username
            );
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPasswordHash = resultSet.getString("password_hash");
                    String storedSalt = resultSet.getString("salt");
                    String enteredPasswordHash = hashPassword(password, storedSalt);
                    return storedPasswordHash.equals(enteredPasswordHash);
                } else {
                    return false; // Пользователь не найден
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при проверке пароля пользователя: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Проверяет пароль пользователя.
     *
     * @param user     Объект User, представляющий пользователя.
     * @param password Пароль для проверки.
     * @return true, если пароль верен, в противном случае false.
     */
    public boolean verifyUserPassword(User user, String password) {
        String storedPasswordHash = user.getPasswordHash();
        String storedSalt = user.getSalt();
        String enteredPasswordHash = hashPassword(password, storedSalt);
        return storedPasswordHash.equals(enteredPasswordHash);
    }
}