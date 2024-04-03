package ru.itmo.lab5.data;

import ru.itmo.lab5.utility.Validateable;

import java.util.Objects;

public class Location implements Validateable {
    private long x;
    private int y;
    private String name; // Поле не может быть null

    public Location(long x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public boolean validate() {
        // Проверяем, что поле name не равно null
        return name != null;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }

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

    // Конструктор, принимающий строку вида "x ; y ; name"
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
