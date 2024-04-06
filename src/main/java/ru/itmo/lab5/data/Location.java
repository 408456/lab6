package ru.itmo.lab5.data;

import ru.itmo.lab5.utility.Validateable;

import java.util.Objects;

/**
 * Класс, представляющий местоположение.
 */
public class Location implements Validateable {
    private long x;          // Координата X
    private int y;           // Координата Y
    private String name;     // Название местоположения; Поле не может быть null

    /**
     * Конструктор класса.
     *
     * @param x    координата X
     * @param y    координата Y
     * @param name название местоположения
     */
    public Location(long x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     * Проверяет, что местоположение валидно.
     *
     * @return true, если местоположение валидно, иначе false
     */
    @Override
    public boolean validate() {
        // Проверяем, что поле name не равно null
        return name != null;
    }

    /**
     * Переопределение метода toString.
     *
     * @return строковое представление объекта Location
     */
    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Переопределение методов equals и hashCode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y && Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }

    /**
     * Конструктор, принимающий строку вида "x ; y ; name".
     *
     * @param s строка с данными о местоположении
     * @throws IllegalArgumentException если строка не соответствует ожидаемому формату
     */
    public Location(String s) {
        String[] parts = s.split(" ; ");
        // Проверяем размер массива parts
        if (parts.length == 3) {
            // Если размер массива равен 3, разбираем строки в переменные
            this.x = parts[0].equals("null") ? -1 : Long.parseLong(parts[0]);
            this.y = parts[1].equals("null") ? 0 : Integer.parseInt(parts[1]);
            this.name = parts[2];
        } else {
            throw new IllegalArgumentException("Некорректный формат строки для создания объекта Location");
        }
    }
}
