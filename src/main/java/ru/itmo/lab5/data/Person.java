package ru.itmo.lab5.data;

import ru.itmo.lab5.utility.Validateable;

import java.util.Objects;

public class Person implements Validateable, Comparable<Person> {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String passportID; //Строка не может быть пустой, Значение этого поля должно быть уникальным, Длина строки не должна быть больше 42, Поле может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле не может быть null
    private Location location; //Поле может быть null

    public Person(String name, String passportID, Color hairColor, Country nationality, Location location) {
        this.name = name;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    @Override
    public boolean validate() {
        if (name == null) return false;
        if (passportID.isEmpty() || passportID.length() <= 42 || passportID == null) return false;
        if (hairColor == null) return false;
        if (nationality == null) return false;
        if (location == null) return false;
        return true;
    }

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

    public Person(String s) {
        try {
            this.name = s.split(" ; ")[0];
            try {
                this.passportID = s.split(" ; ")[1].equals("null") ? null : s.split(" ; ")[1];
            } catch (NumberFormatException e) {
                return;
            }
            try {
                this.hairColor = Color.valueOf(s.split(" ; ")[4]);
            } catch (NullPointerException | IllegalArgumentException e) {
                this.hairColor = null;
            }
            try {
                this.nationality = Country.valueOf(s.split(" ; ")[3]);
            } catch (NullPointerException | IllegalArgumentException e) {
                this.nationality = null;
            }
            try {
                this.location = new Location(s.split(" ; ")[4]);
            } catch (NullPointerException | IllegalArgumentException e) {
                this.nationality = null;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Override
    public int compareTo(Person o) {
        if (o == null) {
            throw new NullPointerException("Нельзя сравнивать с null");
        }
        return this.passportID.compareTo(o.passportID);
    }

    public String getName() {
        return name;
    }

    public String getPassportID() {
        return passportID;
    }

}
