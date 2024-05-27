package ru.itmo.general.managers;

import ru.itmo.general.data.Person;
import ru.itmo.general.data.Product;

import java.time.LocalDateTime;
import java.util.Hashtable;

public interface CollectionManager {
    Hashtable<Long, Product> getCollection();

    LocalDateTime getLastInitTime();

    LocalDateTime getLastSaveTime();

    String getType();

    int getSize();

    Product getById(long id);

    void addToCollection(Product product);

    void clearCollection();

    void saveCollection();

    void removeLowerKey(Long id);

    void removeGreaterKey(Long id);

    void removeGreater(Product product);

    long countLessThanOwner(Person owner);

    boolean contains(Long key);
}
