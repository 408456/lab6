package ru.itmo.lab5.data;

import ru.itmo.lab5.utility.Validateable;

import java.util.Objects;

/**
 * Класс, представляющий координаты.
 */
public class Coordinates implements Validateable {
    private Integer x; // Значение поля должно быть больше -454, Поле не может быть null
    private Double y; // Поле не может быть null

    /**
     * Конструктор класса.
     *
     * @param x координата x
     * @param y координата y
     */
    public Coordinates(Integer x, Double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Проверяет, что значения координат удовлетворяют условиям.
     *
     * @return true, если координаты валидны, иначе false
     */
    @Override
    public boolean validate() {
        if (x == null || y == null) return false;
        return x > -454;
    }

    /**
     * Переопределение метода toString.
     *
     * @return строковое представление объекта Coordinates
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Переопределение методов equals и hashCode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Создает объект Coordinates из строки.
     *
     * @param coordinatesString строка с координатами
     * @return объект Coordinates
     * @throws IllegalArgumentException если строка не соответствует ожидаемому формату
     */
    public static Coordinates fromString(String coordinatesString) {
        if (coordinatesString == null) {
            return null;
        }

        String[] parts = coordinatesString.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid coordinates string format: " + coordinatesString);
        }

        try {
            Integer x = Integer.parseInt(parts[0]);
            Double y = Double.parseDouble(parts[1]);
            return new Coordinates(x, y);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates values: " + coordinatesString);
        }
    }
}
