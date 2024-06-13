package ru.itmo.server.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.general.data.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static ru.itmo.server.managers.ConnectionManager.*;

/**
 * Класс ProductDAO предоставляет методы для взаимодействия с таблицей продуктов в базе данных.
 * Он обрабатывает создание, получение, обновление и удаление продуктов.
 */
public class ProductDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger("ProductDAO");
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ProductDAO.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                LOGGER.error("Sorry, unable to find database.properties");
                throw new IOException("Unable to find database.properties");
            }
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            LOGGER.error("Error loading properties file", ex);
        }
    }

    private static final String SELECT_ALL_PRODUCTS_SQL = properties.getProperty("select_all_products");
    private static final String CREATE_PRODUCTS_TABLE_SQL = properties.getProperty("create_products_table");
    private static final String INSERT_PRODUCT_SQL = properties.getProperty("insert_product");
    private static final String REMOVE_PRODUCT_SQL = properties.getProperty("remove_product");
    private static final String REMOVE_PRODUCTS_BY_USER_ID_SQL = properties.getProperty("remove_products_by_user_id");
    private static final String CHECK_PRODUCT_OWNERSHIP_SQL = properties.getProperty("check_product_ownership");
    private static final String UPDATE_PRODUCT_SQL = properties.getProperty("update_product");

    /**
     * Добавляет новый продукт в базу данных.
     *
     * @param product Продукт для добавления.
     * @param userId ID пользователя, добавляющего продукт.
     * @return ID добавленного продукта в случае успеха, иначе -1.
     */
    public boolean insertProduct(Product product, int userId) {
        String insertProductWithIdSql = properties.getProperty("insert_product_with_id");

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertProductWithIdSql)) {

            statement.setLong(1, product.getId());
            set(statement, product);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (NullPointerException exception) {
            LOGGER.error("Null pointer exception while inserting product, continuing without inserting product");
            return false;
        } catch (SQLException e) {
            LOGGER.error("Error while inserting product {}", e.getMessage());
            return false;
        }
    }

    /**
     * Добавляет коллекцию продуктов в базу данных.
     *
     * @param products Коллекция продуктов для добавления.
     * @param userId ID пользователя, добавляющего продукты.
     */
    public void addProducts(List<Product> products, int userId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {
            for (Product product : products) {
                set(statement, product);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (NullPointerException exception) {
            LOGGER.error("Null pointer exception while adding products, continuing without adding products");
        } catch (SQLException e) {
            LOGGER.error("Error while adding products {}", e.getMessage());
        }
    }

    /**
     * Устанавливает параметры PreparedStatement на основе данных продукта.
     *
     * @param statement PreparedStatement для установки параметров.
     * @param product Продукт, данные которого используются для установки параметров.
     * @throws SQLException в случае ошибки при установке параметров.
     */
    private void set(PreparedStatement statement, Product product) throws SQLException {
        statement.setString(2, product.getName());
        statement.setInt(3, product.getCoordinates().getX());
        statement.setDouble(4, product.getCoordinates().getY());
        statement.setTimestamp(5, new Timestamp(product.getCreationDate().getTime()));
        if (product.getPrice() != null) {
            statement.setInt(6, product.getPrice());
        } else {
            statement.setNull(6, Types.INTEGER);
        }
        if (product.getUnitOfMeasure() != null) {
            statement.setString(7, product.getUnitOfMeasure().toString());
        } else {
            statement.setNull(7, Types.VARCHAR);
        }
        Person owner = product.getOwner();
        statement.setString(8, owner.getName());
        statement.setString(9, owner.getPassportID());
        if (owner.getHairColor() != null) {
            statement.setString(10, owner.getHairColor().toString());
        } else {
            statement.setNull(10, Types.VARCHAR);
        }
        statement.setString(11, owner.getNationality().toString());
        statement.setLong(12, owner.getLocation().getX());
        statement.setInt(13, owner.getLocation().getY());
        statement.setString(14, owner.getLocation().getName());
        statement.setInt(15, product.getUserId());
    }

    /**
     * Извлекает продукт из ResultSet.
     *
     * @param resultSet ResultSet для извлечения данных продукта.
     * @return Извлеченный продукт.
     * @throws SQLException в случае ошибки при извлечении данных.
     */
    private Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        int coordinatesX = resultSet.getInt("coordinates_x");
        double coordinatesY = resultSet.getDouble("coordinates_y");
        Timestamp creationDateTimestamp = resultSet.getTimestamp("creation_date");
        java.util.Date creationDate = new java.util.Date(creationDateTimestamp.getTime());
        Integer price = resultSet.getInt("price");
        if (resultSet.wasNull()) {
            price = null;
        }
        String unitOfMeasureStr = resultSet.getString("unit_of_measure");
        UnitOfMeasure unitOfMeasure = unitOfMeasureStr != null ? UnitOfMeasure.valueOf(unitOfMeasureStr) : null;

        String personName = resultSet.getString("person_name");
        String personPassportID = resultSet.getString("person_passport_id");
        String personHairColorStr = resultSet.getString("person_hair_color");
        Color personHairColor = personHairColorStr != null ? Color.valueOf(personHairColorStr) : null;
        String personNationalityStr = resultSet.getString("person_nationality");
        Country personNationality = Country.valueOf(personNationalityStr);
        long personLocationX = resultSet.getLong("person_location_x");
        int personLocationY = resultSet.getInt("person_location_y");
        String personLocationName = resultSet.getString("person_location_name");
        Location personLocation = new Location(personLocationX, personLocationY, personLocationName);

        Person owner = new Person(personName, personPassportID, personHairColor, personNationality, personLocation);

        int userId = resultSet.getInt("user_id");

        return new Product(id, name, new Coordinates(coordinatesX, coordinatesY), creationDate,
                price, unitOfMeasure, owner, userId);
    }

    /**
     * Получает все продукты из базы данных.
     *
     * @return Список всех продуктов, извлеченных из базы данных.
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PRODUCTS_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = extractProductFromResultSet(resultSet);
                products.add(product);
            }
        } catch (NullPointerException exception) {
            LOGGER.error("Null pointer exception while getting all products, continuing without getting all products");
        } catch (SQLException e) {
            LOGGER.error("Error while retrieving products from the database: {}", e.getMessage());
        }
        return products;
    }

    /**
     * Удаляет продукт из базы данных по его ID.
     *
     * @param productId ID продукта для удаления.
     * @return true, если продукт успешно удален, иначе false.
     */
    public boolean removeProductById(long productId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(REMOVE_PRODUCT_SQL)) {
            statement.setLong(1, productId);
            return statement.executeUpdate() > 0;
        } catch (NullPointerException exception) {
            LOGGER.error("Null pointer exception while removing product, continuing without removing product");
            return false;
        } catch (SQLException e) {
            LOGGER.error("Error while deleting product with ID {}: {}", productId, e.getMessage());
            return false;
        }
    }

    /**
     * Обновляет продукт в базе данных.
     *
     * @param product Продукт с обновленной информацией.
     * @return true, если продукт успешно обновлен, иначе false.
     */
    public boolean updateProduct(Product product) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {

            setUpdate(statement, product);
            return statement.executeUpdate() > 0;
        } catch (NullPointerException exception) {
            LOGGER.error("Null pointer exception while updating product, continuing without updating product");
            return false;
        } catch (SQLException e) {
            LOGGER.error("Error while updating product {}: {}", product.getId(), e.getMessage());
            return false;
        }
    }

    /**
     * Создает таблицу продуктов в базе данных, если она еще не существует.
     */
    public void createTablesIfNotExist() {
        Connection connection = getConnection();
        executeUpdate(connection, CREATE_PRODUCTS_TABLE_SQL);
    }

    /**
     * Проверяет, принадлежит ли продукт определенному пользователю.
     *
     * @param productId ID продукта.
     * @param userId ID пользователя.
     * @return true, если продукт принадлежит пользователю, иначе false.
     */
    public boolean checkOwnership(long productId, int userId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_PRODUCT_OWNERSHIP_SQL)) {
            statement.setLong(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int ownerId = resultSet.getInt("user_id");
                return ownerId == userId;
            } else {
                return false;
            }
        } catch (NullPointerException exception) {
            LOGGER.error("Null pointer exception while checking ownership of product with ID {}: {}",
                    productId, exception.getMessage());
            return false;
        } catch (SQLException e) {
            LOGGER.error("Error while checking ownership of product with ID {}: {}", productId, e.getMessage());
            return false;
        }
    }

    /**
     * Удаляет все продукты, принадлежащие определенному пользователю, из базы данных.
     *
     * @param userId ID пользователя.
     * @return true, если продукты успешно удалены, иначе false.
     */
    public boolean removeProductsByUserId(int userId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(REMOVE_PRODUCTS_BY_USER_ID_SQL)) {
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (NullPointerException exception) {
            LOGGER.error("Null pointer exception while removing products for user with ID {}: {}",
                    userId, exception.getMessage());
            return false;
        } catch (SQLException e) {
            LOGGER.error("Error while removing products for user with ID {}: {}", userId, e.getMessage());
            return false;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Устанавливает параметры PreparedStatement для обновления продукта.
     *
     * @param statement PreparedStatement для установки параметров.
     * @param product Продукт с обновленными данными.
     * @throws SQLException в случае ошибки при установке параметров.
     */
    private void setUpdate(PreparedStatement statement, Product product) throws SQLException {
        statement.setString(1, product.getName());
        statement.setInt(2, product.getCoordinates().getX());
        statement.setDouble(3, product.getCoordinates().getY());
        statement.setTimestamp(4, new Timestamp(product.getCreationDate().getTime()));
        if (product.getPrice() != null) {
            statement.setInt(5, product.getPrice());
        } else {
            statement.setNull(5, Types.INTEGER);
        }
        if (product.getUnitOfMeasure() != null) {
            statement.setString(6, product.getUnitOfMeasure().toString());
        } else {
            statement.setNull(6, Types.VARCHAR);
        }
        Person owner = product.getOwner();
        statement.setString(7, owner.getName());
        statement.setString(8, owner.getPassportID());
        if (owner.getHairColor() != null) {
            statement.setString(9, owner.getHairColor().toString());
        } else {
            statement.setNull(9, Types.VARCHAR);
        }
        statement.setString(10, owner.getNationality().toString());
        statement.setLong(11, owner.getLocation().getX());
        statement.setInt(12, owner.getLocation().getY());
        statement.setString(13, owner.getLocation().getName());
        statement.setLong(14, product.getId()); // Установка ID продукта для условия WHERE
    }

}
