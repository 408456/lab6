package ru.itmo.general.managers;

import ru.itmo.general.data.Person;
import ru.itmo.general.data.Product;

import java.time.LocalDateTime;
import java.util.Hashtable;

/**
 * Интерфейс коллекции продуктов
 * */

public interface CollectionManager {

    // Возвращает коллекцию продуктов.
    Hashtable<Long, Product> getCollection();

    // Возвращает время последней инициализации коллекции.
    LocalDateTime getLastInitTime();

    // Возвращает время последнего сохранения коллекции.
    LocalDateTime getLastSaveTime();

    // Возвращает тип коллекции.
    String getType();

    // Возвращает размер коллекции.
    int getSize();

    // Возвращает продукт по указанному идентификатору.
    Product getById(long id);

    // Добавляет продукт в коллекцию.
    void addToCollection(Product product);

    // Очищает коллекцию.
    void clearCollection();

    // Сохраняет коллекцию.
    void saveCollection();

    // Удаляет из коллекции все элементы, ключ которых меньше заданного.
    void removeLowerKey(Long id);

    // Удаляет из коллекции все элементы, ключ которых больше заданного.
    void removeGreaterKey(Long id);

    // Удаляет из коллекции все элементы, значение которых больше заданного.
    void removeGreater(Product product);

    // Возвращает количество продуктов, владельцы которых имеют идентификатор, меньший, чем у указанного владельца.
    long countLessThanOwner(Person owner);

    // Проверяет, содержится ли элемент с указанным ключом в коллекции.
    boolean contains(Long key);
}
