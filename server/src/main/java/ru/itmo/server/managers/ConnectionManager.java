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
 * Manages database connections and statements.
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
     * Retrieves a database connection.
     *
     * @return A database connection.
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            logError("Connection failed", e);
            return null;
        }
    }

    /**
     * Closes a database connection.
     *
     * @param connection The database connection to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logError("Error closing connection", e);
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
            logError("Connection is null", null);
            return null;
        }
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            logError("Error creating statement", e);
            return null;
        }
    }

    /**
     * Executes an SQL update statement using a given statement.
     *
     * @param statement The statement to execute.
     * @param sql       The SQL statement to execute.
     */
    public static void executeUpdate(Statement statement, String sql) {
        if (statement == null) {
            logError("Statement is null", null);
            return;
        }
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logError("Error executing update", e);
        }
    }

    /**
     * Executes an SQL update statement using a given connection.
     *
     * @param connection The database connection.
     * @param sql        The SQL statement to execute.
     */
    public static void executeUpdate(Connection connection, String sql) {
        Statement statement = createStatement(connection);
        executeUpdate(statement, sql);
    }

    /**
     * Executes a prepared SQL update statement.
     *
     * @param statement The prepared statement to execute.
     * @return The result of executing the statement.
     */
    public static int executePrepareUpdate(PreparedStatement statement) {
        if (statement == null) {
            logError("Statement is null", null);
            return -1;
        } else {
            try {
                return statement.executeUpdate();
            } catch (SQLException e) {
                logError("Error executing update", e);
                return -1;
            }
        }
    }
}
