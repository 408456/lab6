package ru.itmo.lab5.data;

import ru.itmo.lab5.utility.Validateable;

import java.util.Objects;


/**
 * Класс, представляющий человека.
 */
public class Person implements Validateable, Comparable<Person> {
    private String name;           // Имя; Поле не может быть null, Строка не может быть пустой
    private String passportID;     // Идентификационный номер паспорта; Строка не может быть пустой, Значение этого поля должно быть уникальным, Длина строки не должна быть больше 42, Поле может быть null
    private Color hairColor;      // Цвет волос; Поле может быть null
    private Country nationality;  // Национальность; Поле не может быть null
    private Location location;    // Местоположение; Поле может быть null

    /**
     * Конструктор класса.
     *
     * @param name        имя человека
     * @param passportID  идентификационный номер паспорта
     * @param hairColor   цвет волос
     * @param nationality национальность
     * @param location    местоположение
     */
    public Person(String name, String passportID, Color hairColor, Country nationality, Location location) {
        this.name = name;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    /**
     * Конструктор, принимающий строку вида "name ; passportID ; hairColor ; nationality ; location".
     *
     * @param s строка с данными о человеке
     */
    public Person(String s) {
        try {
            String[] parts = s.split(" ; ");
            this.name = parts[0];
            this.passportID = parts[1].equals("null") ? null : parts[1];
            this.hairColor = Color.valueOf(parts[2]);
            this.nationality = Country.valueOf(parts[3]);
            this.location = new Location(parts[4]);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Проверяет, что данные человека валидны.
     *
     * @return true, если данные валидны, иначе false
     */
    @Override
    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (passportID == null || passportID.isEmpty() || passportID.length() > 42) return false;
        if (hairColor == null) return false;
        if (nationality == null) return false;
        return location != null;
    }

        /**
     * Переопределение метода toString.
     *
     * @return строковое представление объекта Person
     */
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", passportID='" + passportID + '\'' +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                ", location=" + location +
                '}';
    }

    /**
     * Переопределение методов equals и hashCode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(passportID, person.passportID) && hairColor == person.hairColor && nationality == person.nationality && Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, passportID, hairColor, nationality, location);
    }

    /**
     * Переопределение метода compareTo.
     *
     * @param o объект Person для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(Person o) {
        int nameComparison = this.name.compareTo(o.name);
        if (nameComparison != 0) {
            return nameComparison;
        }
        return this.passportID.compareTo(o.passportID);
    }


    /**
     * Возвращает имя человека.
     *
     * @return имя
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает идентификационный номер паспорта человека.
     *
     * @return идентификационный номер паспорта
     */
    public String getPassportID() {
        return passportID;
    }

    public static Person fromString(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Строка для парсинга Person не может быть null или пустой");
        }

        try {
            String data = s.substring(s.indexOf("{") + 1, s.lastIndexOf("}"));

            String[] parts = data.split(", ");

            String a1 = parts[0].substring(6);
            String nameProm = a1.substring(0, a1.length() - 1);

            String a2 = parts[1].substring(12);
            String passportProm = a2.substring(0, a2.length() - 1);

            String hairColorProm = parts[2].substring(10);

            String nationalityProm = parts[3].substring(12);

            String locationProm1 = parts[4] + ", " + parts[5] + ", " + parts[6];
            String locationProm = locationProm1.substring(9);

            if (parts.length != 7) {
                throw new IllegalArgumentException("Некорректный формат строки для создания объекта Person");
            }

            String passportID = passportProm.equals("null") ? null : passportProm;
            Color hairColor = Color.valueOf(hairColorProm);
            Country nationality = Country.valueOf(nationalityProm);
            Location location = Location.fromString(locationProm);

            return new Person(nameProm, passportID, hairColor, nationality, location);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга строки в объект Person", e);
        }
    }

}
