package ru.itmo.lab5.managers;

import ru.itmo.lab5.data.Person;
import ru.itmo.lab5.data.Product;
import ru.itmo.lab5.input.Console;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для управления коллекцией продуктов
 */
public class CollectionManager {
    /**
     * Коллекция продуктов
     */
    private final Hashtable<Long, Product> collection = new Hashtable<>();
    /**
     * Время последней инициализации коллекции
     */
    private LocalDateTime lastInitTime;
    /**
     * Время последнего сохранения коллекции
     */
    private LocalDateTime lastSaveTime;
    /**
     * Менеджер для сохранения/загрузки коллекции
     */
    private final DumpManager dumpManager;

    /**
     * Конструктор класса.
     *
     * @param dumpManager Менеджер для сохранения/загрузки коллекции
     */
    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;

        loadCollection();
    }

    /**
     * Получает коллекцию продуктов.
     *
     * @return Коллекция продуктов
     */
    public Hashtable<Long, Product> getCollection() {
        return collection;
    }

    /**
     * Получает время последней инициализации коллекции.
     *
     * @return Время последней инициализации коллекции
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * Получает время последнего сохранения коллекции.
     *
     * @return Время последнего сохранения коллекции
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * Возвращает тип коллекции.
     *
     * @return Тип коллекции
     */
    public String getType() {
        return collection.getClass().getName();
    }

    /**
     * Возвращает размер коллекции.
     *
     * @return Размер коллекции
     */
    public int getSize() {
        return collection.size();
    }

    /**
     * Возвращает последний добавленный продукт.
     *
     * @return Последний добавленный продукт
     */
    public Product getLast() {
        if (collection.isEmpty()) return null;
        return collection.get(collection.keySet().stream().max(Long::compareTo).orElse(null));
    }

    /**
     * Возвращает продукт по указанному ключу.
     *
     * @param id Ключ продукта
     * @return Продукт с указанным ключом
     */
    public Product getById(long id) {
        return collection.get(id);
    }

    // Методы для управления коллекцией

    /**
     * Добавляет продукт в коллекцию.
     *
     * @param product Добавляемый продукт
     */
    public void addToCollection(Product product) {
        collection.put(product.getId(), product);
    }

    /**
     * Очищает коллекцию продуктов.
     */
    public void clearCollection() {
        collection.clear();
    }

    /**
     * Сохраняет коллекцию продуктов.
     */
    public void saveCollection() {
        dumpManager.writeCollection(collection.values());
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * Загружает коллекцию продуктов.
     */
    private void loadCollection() {
        Map<Long, Product> loadedCollection = dumpManager.readCollection().stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
        collection.putAll(loadedCollection);
        lastInitTime = LocalDateTime.now();
    }

    /**
     * Проверяет валидность всех продуктов в коллекции.
     *
     * @param console Консоль для вывода сообщений об ошибках
     */
    public void validateAll(Console console) {
        collection.values().stream()
                .filter(product -> !product.validate())
                .forEach(product -> console.println("Продукт с id = " + product.getId() + " имеет невалидные поля."));
        console.println("Загруженные продукты валидны!");
    }

    //    @Override
//    public String toString() {
//        if (collection.isEmpty()) return "Коллекция пуста!";
//        Product last = getLast();
//
//        StringBuilder info = new StringBuilder();
//        for (Product product : collection.values()) {
//            info.append(product);
//            if (product != last) info.append("\n\n");
//        }
//        return info.toString();
//    }
    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();

        info.append("Продукты, отсортированные по убыванию цены:\n");
        for (Product product : getProductsSortedByPriceDesc()) {
            info.append(product).append("\n");
        }
//        info.append("Продукты, отсортированные по возрастанию цены:\n");
//        for (Product product : getProductsSortedByPriceAsc()) {
//            info.append(product).append("\n");
//        }
//        info.append("\n");
//        info.append("Продукты, отсортированные по ключу: \n");
//        for (Long key : getSortedKeys()) {
//            info.append(collection.get(key)).append("\n");
//        }

        return info.toString();
    }


    /**
     * Удаляет из коллекции все элементы, ключ которых меньше заданного id.
     *
     * @param id Заданный id
     */
    public void removeLowerKey(Long id) {
        collection.entrySet().removeIf(entry -> entry.getKey() < id);
    }

    /**
     * Удаляет из коллекции все элементы, ключ которых больше заданного id.
     *
     * @param id Заданный id
     */
    public void removeGreaterKey(Long id) {
        collection.entrySet().removeIf(entry -> entry.getKey() > id);
    }
    /**
     * Удаляет из коллекции все элементы, ключ которых больше, чем у заданного продукта.
     *
     * @param product заданный продукт
     */
    public void removeGreater(Product product) {
        collection.values().removeIf(p -> p.compareTo(product) > 0);
    }

    /**
     * Возвращает количество продуктов, у которых владелец меньше заданного.
     *
     * @param owner Владелец, с которым сравнивается владелец каждого продукта
     * @return Количество продуктов, у которых владелец меньше заданного
     */
    public Integer countLessThanOwner(Person owner) {
        return (int) collection.values().stream()
                .filter(product -> product.getOwner().compareTo(owner) < 0)
                .count();
    }

    /**
     * Проверяет, содержит ли коллекция элемент с указанным ключом.
     *
     * @param key Ключ для проверки
     * @return true, если коллекция содержит элемент с указанным ключом, иначе false
     */
    public boolean contains(Long key) {
        return collection.containsKey(key);
    }

    /**
     * Возвращает отсортированный список ключей из коллекции.
     *
     * @return Отсортированный список ключей
     */
    public List<Long> getSortedKeys() {
        List<Long> keys = new ArrayList<>(collection.keySet());
        Collections.sort(keys);
        return keys;
    }

    /**
     * Возвращает отсортированные продукты по возрастанию цены.
     *
     * @return Отсортированные продукты по возрастанию цены
     */
    public List<Product> getProductsSortedByPriceAsc() {
        return collection.values().stream()
                .sorted(Comparator.comparingInt(Product::getPrice))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает отсортированные продукты по убыванию цены.
     *
     * @return Отсортированные продукты по убыванию цены
     */
    public List<Product> getProductsSortedByPriceDesc() {
        return collection.values().stream()
                .sorted(Comparator.comparingInt(Product::getPrice).reversed())
                .collect(Collectors.toList());
    }
}
