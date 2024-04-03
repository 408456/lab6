package ru.itmo.lab5.data;

import ru.itmo.lab5.utility.Validateable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Product implements Validateable, Comparable<Product> {
    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private java.util.Date creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer price; // Поле может быть null, Значение поля должно быть больше 0
    private UnitOfMeasure unitOfMeasure; // Поле может быть null
    private Person owner; // Поле может быть null
    public static long nextId = 1; // Статическое поле для генерации уникальных идентификаторов продуктов

    /**
     * Конструктор для создания объекта класса Product.
     *
     * @param name          наименование продукта
     * @param coordinates   координаты продукта
     * @param price         цена продукта
     * @param unitOfMeasure единица измерения продукта
     * @param owner         владелец продукта
     */
    public Product(String name, Coordinates coordinates, Integer price, UnitOfMeasure unitOfMeasure, Person owner) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.price = price;
        this.unitOfMeasure = unitOfMeasure;
        this.owner = owner;
        this.id = nextId++; // Генерация уникального идентификатора для продукта
    }

    /**
     * Конструктор для создания объекта класса Product с заданным идентификатором.
     *
     * @param id            идентификатор продукта
     * @param name          наименование продукта
     * @param coordinates   координаты продукта
     * @param creationDate  дата создания продукта
     * @param price         цена продукта
     * @param unitOfMeasure единица измерения продукта
     * @param owner         владелец продукта
     */
    public Product(Long id, String name, Coordinates coordinates, Date creationDate, Integer price, UnitOfMeasure unitOfMeasure, Person owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.unitOfMeasure = unitOfMeasure;
        this.owner = owner;
    }

    /**
     * Проверяет валидность объекта Product.
     *
     * @return true, если объект валиден, иначе false
     */
    @Override
    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (price == null || price < 0) return false;
        if (unitOfMeasure == null) return false;
        if (owner == null) return false;
        return true;
    }

    /**
     * Сравнивает данный объект Product с указанным объектом Product по их идентификаторам.
     *
     * @param o объект Product для сравнения
     * @return отрицательное целое число, ноль или положительное целое число, если этот объект меньше, равен или больше указанного объекта Product
     */
    @Override
    public int compareTo(Product o) {
        return (int) (this.id - o.getId());
    }

    /**
     * Возвращает строковое представление объекта Product.
     *
     * @return строковое представление объекта Product
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", unitOfMeasure=" + unitOfMeasure +
                ", owner=" + owner +
                '}';
    }

    /**
     * Проверяет равенство данного объекта Product с указанным объектом на равенство.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(coordinates, product.coordinates) && Objects.equals(creationDate, product.creationDate) && Objects.equals(price, product.price) && unitOfMeasure == product.unitOfMeasure && Objects.equals(owner, product.owner);
    }

    /**
     * Возвращает хеш-код для данного объекта Product.
     *
     * @return хеш-код объекта Product
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, unitOfMeasure, owner);
    }

    /**
     * Создает объект Product из массива строк.
     *
     * @param a массив строк, содержащий данные о продукте
     * @return объект Product, созданный из массива строк
     */

    public static Product fromArray(String[] a) {
        Long id;
        String name;
        Coordinates coordinates;
        Date creationDate;
        Integer price;
        UnitOfMeasure unitOfMeasure;
        Person owner;

        try {
            try {
                id = Long.parseLong(a[0]);
            } catch (NumberFormatException e) {
                id = null;
            }
            name = a[1];
            coordinates = Coordinates.fromString(a[2]);
            try {
                creationDate = Date.from(LocalDate.parse(a[3], DateTimeFormatter.ISO_DATE).atStartOfDay(ZoneId.systemDefault()).toInstant());
            } catch (DateTimeParseException e) {
                creationDate = null;
            }
            try {
                price = (a[4].equals("null") ? null : Integer.parseInt(a[4]));
            } catch (NumberFormatException e) {
                price = null;
            }
            try {
                unitOfMeasure = UnitOfMeasure.valueOf(a[5]);
            } catch (NullPointerException | IllegalArgumentException e) {
                unitOfMeasure = null;
            }
            owner = (a[6].equals("null") ? null : new Person(a[6]));
            return new Product(id, name, coordinates, creationDate, price, unitOfMeasure, owner);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * Преобразует объект Product в массив строк.
     *
     * @param e объект Product для преобразования
     * @return массив строк, представляющий объект Product
     */
    public static String[] toArray(Product e) {
        var list = new ArrayList<String>();
        list.add(e.getId().toString());
        list.add(e.getName());
        list.add(e.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE_TIME));
        list.add(e.getCoordinates().toString());
        list.add(e.getUnitOfMeasure() == null ? "null" : e.getUnitOfMeasure().toString());
        return list.toArray(new String[0]);
    }

    /**
     * Возвращает идентификатор данного объекта Product.
     *
     * @return идентификатор объекта Product
     */
    public Long getId() {
        return id;
    }

    /**
     * Обновляет данные текущего объекта Product данными из другого объекта Product.
     *
     * @param product объект Product, данные которого будут использованы для обновления текущего объекта
     */
    public void update(Product product) {
        this.name = product.name;
        this.coordinates = product.coordinates;
        this.creationDate = product.creationDate;
        this.price = product.price;
        this.unitOfMeasure = product.unitOfMeasure;
        this.owner = product.owner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Integer getPrice() {
        return price;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Person getOwner() {
        return owner;
    }

}