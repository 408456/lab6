package ru.itmo.server.managers;

import lombok.Getter;
import ru.itmo.general.data.Person;
import ru.itmo.general.data.Product;
import ru.itmo.general.utility.io.Console;
import ru.itmo.server.dao.ProductDAO;
import ru.itmo.server.dao.UserDAO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProductCollectionManager implements CollectionManager {
    @Getter
    private final ConcurrentHashMap<Long, Product> collection = new ConcurrentHashMap<>();
    @Getter
    private LocalDateTime lastInitTime;
    @Getter
    private LocalDateTime lastSaveTime;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public ProductCollectionManager(ProductDAO productDAO, UserDAO userDAO) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.lastInitTime = null;
        this.lastSaveTime = null;

        loadCollection();
    }

    public String getType() {
        return collection.getClass().getName();
    }

    public int getSize() {
        return collection.size();
    }

    public Product getLast() {
        if (collection.isEmpty()) return null;
        return collection.get(collection.keySet().stream().max(Long::compareTo).orElse(null));
    }

    public Product getById(long id) {
        return collection.get(id);
    }

    public void insertToCollection(Product product) {
        if (productDAO.insertProduct(product, product.getUserId())) {
            collection.put(product.getId(), product);
            lastSaveTime = LocalDateTime.now();
        }
    }

    public void clearCollection(int userId) {
        collection.entrySet().removeIf(entry -> checkOwnership(entry.getKey(), userId) && productDAO.removeProductById(entry.getKey()));
        lastSaveTime = LocalDateTime.now();
    }

    private void loadCollection() {
        List<Product> products = productDAO.getAllProducts();
        collection.clear();
        for (Product product : products) {
            collection.put(product.getId(), product);
        }
        lastInitTime = LocalDateTime.now();
        lastSaveTime = LocalDateTime.now();
    }

    public void validateAll(Console console) {
        if (collection.isEmpty()) {
            console.printError("Коллекция пуста!");
            return;
        }

        boolean allValid = collection.values().stream().allMatch(Product::validate);
        if (allValid) {
            console.println("Загруженные продукты валидны!");
        } else {
            collection.values().stream()
                    .filter(product -> !product.validate())
                    .forEach(product -> console.println("Продукт с id = " + product.getId() + " имеет невалидные поля."));
        }
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        info.append("Продукты, отсортированные по убыванию цены:\n");
        for (Product product : getProductsSortedByPriceDesc()) {
            info.append(product).append("\n");
        }
        return info.toString();
    }

    public void removeLowerKey(Long id, int userId) {
        collection.entrySet().removeIf(entry -> entry.getKey() < id && checkOwnership(entry.getKey(), userId) && productDAO.removeProductById(entry.getKey()));
        lastSaveTime = LocalDateTime.now();
    }

    public void removeGreaterKey(Long id, int userId) {
        collection.entrySet().removeIf(entry -> entry.getKey() > id && checkOwnership(entry.getKey(), userId) && productDAO.removeProductById(entry.getKey()));
        lastSaveTime = LocalDateTime.now();
    }

    public void removeGreater(Product product, int userId) {
        collection.values().removeIf(p -> p.compareTo(product) > 0 && checkOwnership(p.getId(), userId) && productDAO.removeProductById(p.getId()));
        lastSaveTime = LocalDateTime.now();
    }

    public long countLessThanOwner(Person owner) {
        return collection.values().stream()
                .filter(product -> product.getOwner() != null)
                .filter(product -> product.getOwner().compareTo(owner) < 0)
                .count();
    }

    public boolean contains(Long key) {
        return collection.containsKey(key);
    }

    public List<Long> getSortedKeys() {
        List<Long> keys = new ArrayList<>(collection.keySet());
        Collections.sort(keys);
        return keys;
    }

    public List<Product> getProductsSortedByPriceAsc() {
        return collection.values().stream()
                .sorted(Comparator.comparingInt(Product::getPrice))
                .collect(Collectors.toList());
    }

    public List<Product> getProductsSortedByPriceDesc() {
        return collection.values().stream()
                .sorted(Comparator.comparingInt(Product::getPrice).reversed())
                .collect(Collectors.toList());
    }

    public boolean checkOwnership(long productId, int userId) {
        return productDAO.checkOwnership(productId, userId);
    }

    public void updateProduct(Product product, int userId) {
        if (checkOwnership(product.getId(), userId) && productDAO.updateProduct(product)) {
            getById(product.getId()).update(product);
            lastSaveTime = LocalDateTime.now();
        }
    }

    public boolean removeById(long productId, int userId) {
        if (checkOwnership(productId, userId) && productDAO.removeProductById(productId)) {
            collection.remove(productId);
            lastSaveTime = LocalDateTime.now();
            return true;
        }
        return false;
    }
}
