package ru.itmo.general.data;

import lombok.Getter;
import ru.itmo.general.utility.Validateable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс, представляющий местоположение.
 */
@Getter
public class Location implements Validateable, Serializable {
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

    public static Location fromString(String s) {
        try {
            String[] parts = s.split(", ");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Некорректный формат строки для создания объекта Location");
            }

            long x;
            int y;
            String name;

            try {
                String promX = parts[0].split("=")[1];
                x = promX.equals("null") ? -1 : Long.parseLong(promX);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Ошибка преобразования x в объект Location", e);
            }

            try {
                String promY = parts[1].split("=")[1];
                y = promY.equals("null") ? 0 : Integer.parseInt(promY);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Ошибка преобразования y в объект Location", e);
            }

            String[] nameParts = parts[2].split("=");
            name = nameParts[1].substring(0, nameParts[1].length() - 1);

            return new Location(x, y, name);

        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка преобразования строки в объект Location", e);
        }
    }
}
