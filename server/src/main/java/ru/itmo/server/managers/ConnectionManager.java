package ru.itmo.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;

/**
 * Управляет подключениями к базе данных и операциями с ними.
 */
public class ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("ConnectionManager");
    private static final Properties properties = new Properties();
    private static String DB_URL;
    static String DB_NAME;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                LOGGER.error("Sorry, unable to find database.properties");
                throw new IOException("Unable to find database.properties");
            }
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            DB_URL = properties.getProperty("db.url");
            DB_NAME = properties.getProperty("db.name");
            DB_USER = properties.getProperty("db.user");
            DB_PASSWORD = properties.getProperty("db.password");
        } catch (IOException ex) {
            LOGGER.error("Error loading properties file", ex);
        }
    }

    public static void createDatabase() {
        executeUpdate(getConnection(), "CREATE DATABASE " + DB_NAME);
    }

    /**
     * Извлекает подключение к базе данных.
     *
     * @return Подключение к базе данных.
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL + DB_NAME);
        } catch (SQLException e) {
            logError("Не удалось установить соединение", e);
            return null;
        }
    }

    /**
     * Закрывает подключение к базе данных.
     *
     * @param connection Подключение к базе данных для закрытия.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logError("Ошибка при закрытии подключения", e);
            }
        }
    }

    private static void logError(String message, SQLException e) {
        if (e == null) {
            LOGGER.error(message);
        } else {
            LOGGER.error("{}: {}", message, e.getMessage());
        }
    }

    private static Statement createStatement(Connection connection) {
        if (connection == null) {
            logError("Подключение равно null", null);
            return null;
        }
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            logError("Ошибка при создании оператора", e);
            return null;
        }
    }

    /**
     * Выполняет SQL-запрос на обновление с использованием заданного оператора.
     *
     * @param statement Оператор для выполнения запроса.
     * @param sql       SQL-запрос для выполнения.
     */
    public static void executeUpdate(Statement statement, String sql) {
        if (statement == null) {
            logError("Оператор равен null", null);
            return;
        }
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logError("Ошибка при выполнении обновления", e);
        }
    }

    /**
     * Выполняет SQL-запрос на обновление с использованием заданного подключения.
     *
     * @param connection Подключение к базе данных.
     * @param sql        SQL-запрос для выполнения.
     */
    public static void executeUpdate(Connection connection, String sql) {
        Statement statement = createStatement(connection);
        executeUpdate(statement, sql);
    }

    /**
     * Выполняет подготовленный SQL-запрос на обновление.
     *
     * @param statement Подготовленный оператор для выполнения.
     * @return Результат выполнения оператора.
     */
    public static int executePrepareUpdate(PreparedStatement statement) {
        if (statement == null) {
            logError("Оператор равен null", null);
            return -1;
        } else {
            try {
                return statement.executeUpdate();
            } catch (SQLException e) {
                logError("Ошибка при выполнении обновления", e);
                return -1;
            }
        }
    }
}
