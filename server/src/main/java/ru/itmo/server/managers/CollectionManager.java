package ru.itmo.server.managers;

import ru.itmo.general.data.Person;
import ru.itmo.general.data.Product;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Интерфейс коллекции продуктов
 */
public interface CollectionManager {

    /**
     * Возвращает коллекцию продуктов.
     *
     * @return коллекция продуктов
     */
    ConcurrentHashMap<Long, Product> getCollection();

    /**
     * Возвращает время последней инициализации коллекции.
     *
     * @return время последней инициализации коллекции
     */
    LocalDateTime getLastInitTime();

    /**
     * Возвращает время последнего сохранения коллекции.
     *
     * @return время последнего сохранения коллекции
     */
    LocalDateTime getLastSaveTime();

    /**
     * Возвращает тип коллекции.
     *
     * @return тип коллекции
     */
    String getType();

    /**
     * Возвращает размер коллекции.
     *
     * @return размер коллекции
     */
    int getSize();

    /**
     * Возвращает продукт по указанному идентификатору.
     *
     * @param id идентификатор продукта
     * @return продукт по указанному идентификатору
     */
    Product getById(long id);

    /**
     * Добавляет продукт в коллекцию.
     *
     * @param product добавляемый продукт
     */
    void insertToCollection(Product product);

    /**
     * Очищает коллекцию.
     *
     * @param userId идентификатор пользователя
     */
    void clearCollection(int userId);

    /**
     * Удаляет из коллекции все элементы, ключ которых меньше заданного.
     *
     * @param id идентификатор продукта
     * @param userId идентификатор пользователя
     */
    void removeLowerKey(Long id, int userId);

    /**
     * Удаляет из коллекции все элементы, ключ которых больше заданного.
     *
     * @param id идентификатор продукта
     * @param userId идентификатор пользователя
     */
    void removeGreaterKey(Long id, int userId);

    /**
     * Удаляет из коллекции все элементы, значение которых больше заданного.
     *
     * @param product продукт для сравнения
     * @param userId идентификатор пользователя
     */
    void removeGreater(Product product, int userId);

    /**
     * Возвращает количество продуктов, владельцы которых имеют идентификатор, меньший, чем у указанного владельца.
     *
     * @param owner владелец для сравнения
     * @return количество продуктов
     */
    long countLessThanOwner(Person owner);

    /**
     * Проверяет, содержится ли элемент с указанным ключом в коллекции.
     *
     * @param key ключ для проверки
     * @return true, если элемент содержится в коллекции, иначе false
     */
    boolean contains(Long key);

    /**
     * Проверяет право собственности на продукт по его идентификатору и идентификатору пользователя.
     *
     * @param productId идентификатор продукта
     * @param userId идентификатор пользователя
     * @return true, если продукт принадлежит пользователю, иначе false
     */
    boolean checkOwnership(long productId, int userId);

    /**
     * Обновляет продукт в коллекции.
     *
     * @param product продукт для обновления
     * @param userId идентификатор пользователя
     */
    void updateProduct(Product product, int userId);

    /**
     * Удаляет продукт по идентификатору, если он принадлежит пользователю.
     *
     * @param productId идентификатор продукта
     * @param userId идентификатор пользователя
     * @return true, если продукт был успешно удален, иначе false
     */
    boolean removeById(long productId, int userId);
}
