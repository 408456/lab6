package ru.itmo.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.server.dao.ProductDAO;
import ru.itmo.server.dao.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;

import static ru.itmo.server.managers.ConnectionManager.*;

/**
 * Управляет операциями с базой данных, включая создание базы данных, создание таблиц и управление пользователями.
 */
public class DatabaseManager {
    private static final UserDAO userDAO = new UserDAO();
    private static final ProductDAO productDAO = new ProductDAO();
    private static final Logger logger = LoggerFactory.getLogger("DatabaseManager");

    /**
     * Создает базу данных, если она еще не существует, и инициализирует таблицы.
     */
    public static void createDatabaseIfNotExists() {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                boolean databaseExists = checkDatabaseExists(connection);
                if (!databaseExists) {
                    executeUpdate(connection, "CREATE DATABASE " + DB_NAME);
                    logger.info("База данных и таблицы успешно созданы.");
                } else {
                    logger.info("База данных уже существует.");
                }
                createTablesIfNotExist(connection);
            } else {
                logger.error("Не удалось установить соединение с базой данных.");
            }
        } catch (SQLException e) {
            logger.error("Ошибка при создании базы данных: {}", e.getMessage());
        }
    }

    /**
     * Проверяет существование базы данных.
     *
     * @param connection Соединение с базой данных.
     * @return true, если база данных существует, в противном случае false.
     * @throws SQLException Если происходит ошибка SQL.
     */
    private static boolean checkDatabaseExists(Connection connection) throws SQLException {
        return connection.getMetaData().getCatalogs()
                .next(); // Проверяем существование базы данных, пытаясь перейти к первой записи
    }

    /**
     * Создает необходимые таблицы, если они еще не существуют.
     *
     * @param connection Соединение с базой данных.
     */
    public static void createTablesIfNotExist(Connection connection) {
        if (connection != null) {
            userDAO.createTablesIfNotExist();
            productDAO.createTablesIfNotExist();
            logger.info("Таблицы успешно созданы (если не существовали).");
        } else {
            logger.error("Соединение null.");
        }
    }

}
