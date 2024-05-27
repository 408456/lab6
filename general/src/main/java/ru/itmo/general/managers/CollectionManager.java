package ru.itmo.general.managers;

import ru.itmo.general.data.Person;
import ru.itmo.general.data.Product;
import ru.itmo.general.input.Console;

import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;

public interface CollectionManager {

    boolean canWriteToFile();

    boolean canReadFromFile();

    Hashtable<Long, Product> getCollection();

    LocalDateTime getLastInitTime();

    LocalDateTime getLastSaveTime();

    String getType();

    int getSize();

    Product getLast();

    Product getById(long id);

    void addToCollection(Product product);

    void clearCollection();

    void saveCollection();

    void validateAll(Console console);

    void removeLowerKey(Long id);

    void removeGreaterKey(Long id);

    void removeGreater(Product product);

    long countLessThanOwner(Person owner);

    boolean contains(Long key);

    List<Long> getSortedKeys();

    List<Product> getProductsSortedByPriceAsc();

    List<Product> getProductsSortedByPriceDesc();
}
