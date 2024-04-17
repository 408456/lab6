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

    public static Coordinates fromString(String s) {
        try {
            // Разбиваем строку по запятой и пробелу
            String[] parts = s.split(", ");

            Integer x = null;
            Double y = null;

            try {
                // Извлекаем значения x
                x = Integer.parseInt(parts[0].split("=")[1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) { }

            try {
                String original = parts[1].split("=")[1];
                StringBuilder sb = new StringBuilder(original);
                sb.deleteCharAt(original.length() - 1);
                String modified = sb.toString();
                // Извлекаем значения y
                y = Double.parseDouble(modified);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) { }

            return new Coordinates(x, y);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка преобразования строки в объект Coordinates", e);
        }
    }



}
