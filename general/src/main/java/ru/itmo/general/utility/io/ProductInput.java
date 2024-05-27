package ru.itmo.general.utility.io;

import ru.itmo.general.data.Product;
import ru.itmo.general.data.Color;
import ru.itmo.general.data.Coordinates;
import ru.itmo.general.data.Country;
import ru.itmo.general.data.Location;
import ru.itmo.general.data.Person;
import ru.itmo.general.data.UnitOfMeasure;
import ru.itmo.general.utility.exceptions.IncorrectScriptException;
import ru.itmo.general.utility.exceptions.InvalidFormException;
import ru.itmo.general.utility.exceptions.InvalidRangeException;
import ru.itmo.general.utility.exceptions.InvalidValueException;
import ru.itmo.general.utility.exceptions.MustBeNotEmptyException;

import java.util.regex.*;


import java.util.NoSuchElementException;

public class ProductInput {
    private final Console console;

    public ProductInput(Console console) {
        this.console = console;
    }

    public Product make() throws IncorrectScriptException, InvalidFormException, InvalidValueException {
        Product product = new Product(inputProductName(), inputCoordinates(), inputPrice(), inputUnitOfMeasure(), inputPerson());
        if (!product.validate()) throw new InvalidFormException();
        return product;
    }

    /**
     * Ввод названия продукта
     */
    private String inputProductName() throws IncorrectScriptException {
        String name;
        boolean fileMode = InputSteamer.getFileMode();
        Pattern pattern = Pattern.compile("^[^\\p{P}\\d\\s].*");
        while (true) {
            try {
                console.println("Введите название продукта:");
                console.ps1();
                name = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(name);
                if (name.isEmpty()) throw new MustBeNotEmptyException();
                Matcher matcher = pattern.matcher(name);
                if (!matcher.matches()) throw new InvalidValueException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Название не распознано!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (MustBeNotEmptyException exception) {
                console.printError("Название не может быть пустым!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidValueException exception) {
                console.printError("Название не может начинаться с знаков препинания, чисел или специальных символов!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }

    /**
     * Ввод координат
     */
    private Coordinates inputCoordinates() throws IncorrectScriptException, InvalidFormException {
        Coordinates coordinates = new Coordinates(inputX(), inputY());
        if (!coordinates.validate()) throw new InvalidFormException();
        return coordinates;
    }

    /**
     * Ввод х координаты
     */
    private Integer inputX() throws IncorrectScriptException {
        int x;
        boolean fileMode = InputSteamer.getFileMode();
        while (true) {
            try {
                console.println("Введите координату X (должно быть целым числом):");
                console.ps1();
                String strX = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strX);
                x = Integer.parseInt(strX);
                if (x < Integer.MIN_VALUE || x > Integer.MAX_VALUE) throw new InvalidRangeException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата X не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Координата X должна быть представлена целым числом! Значение должно быть в диапазоне от -2147483648 до 2147483647!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidRangeException exception) {
                console.printError("Значение должно быть в диапазоне от -2147483648 до 2147483647!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Ввод y координаты
     */
    private Double inputY() throws IncorrectScriptException {
        double y;
        boolean fileMode = InputSteamer.getFileMode();
        while (true) {
            try {
                console.println("Введите координату Y (должно быть числом с плавающей точкой):");
                console.ps1();
                String strY = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strY);
                y = Double.parseDouble(strY);
                if (y < -Double.MAX_VALUE || y > Double.MAX_VALUE) throw new InvalidRangeException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата Y не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Координата Y должна быть представлена числом с плавающей точкой! Значение должно быть в диапазоне от -1.7976931348623157E308 до 1.7976931348623157E308!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidRangeException exception) {
                console.printError("Значение должно быть в диапазоне от -1.7976931348623157E308 до 1.7976931348623157E308!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return y;
    }


    /**
     * Ввод цены продукта
     */
    private Integer inputPrice() throws IncorrectScriptException {
        boolean fileMode = InputSteamer.getFileMode();
        int price;
        while (true) {
            try {
                console.println("Введите цену продукта:");
                console.ps1();
                String strPrice = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strPrice);
                price = Integer.parseInt(strPrice);
                if (price <= 0 || price < Integer.MIN_VALUE || price > Integer.MAX_VALUE)
                    throw new InvalidRangeException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Цена продукта не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidRangeException exception) {
                console.printError("Цена должна быть больше нуля и в диапазоне от -2147483648 до 2147483647!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Цена продукта должна быть представлена целым числом от 0 до 2147483647!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return price;
    }

    /**
     * Ввод единиц измерения
     */
    private UnitOfMeasure inputUnitOfMeasure() throws IncorrectScriptException {
        boolean fileMode = InputSteamer.getFileMode();

        String strUnitOfMeasure;
        UnitOfMeasure unitOfMeasure;
        while (true) {
            try {
                console.println("Список доступных единиц измерения - " + UnitOfMeasure.names());
                console.println("Введите единицы измерения:");
                console.ps1();
                strUnitOfMeasure = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strUnitOfMeasure);
                unitOfMeasure = UnitOfMeasure.valueOf(strUnitOfMeasure.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Единицы измерения не распознаны!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalArgumentException exception) {
                console.printError("Единиц измерения нет в списке!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return unitOfMeasure;
    }

    public Person inputPerson() throws IncorrectScriptException {
        String name = inputPersonName();
        String passportID = inputPassportID();
        Color hairColor = inputHairColor();
        Country nationality = inputNationality();
        Location location = inputLocation();
        return new Person(name, passportID, hairColor, nationality, location);
    }
    public Person inputPersonForCountLess() throws IncorrectScriptException {
        String name = inputPersonName();
        String passportID = inputPassportID();
        Color hairColor = inputHairColor();
        Country nationality = inputNationality();
        Location location = inputLocation();
        return new Person(name, passportID, hairColor, nationality, location);
    }

    /**
     * Ввод имени покупателя
     */
    private String inputPersonName() throws IncorrectScriptException {
        String name;
        boolean fileMode = InputSteamer.getFileMode();
        Pattern pattern = Pattern.compile("^[^\\p{P}\\d\\s].*");
        while (true) {
            try {
                console.println("Введите имя покупателя:");
                console.ps1();
                name = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(name);
                if (name.isEmpty()) throw new MustBeNotEmptyException();
                Matcher matcher = pattern.matcher(name);
                if (!matcher.matches()) throw new InvalidValueException();

                break;
            } catch (NoSuchElementException exception) {
                console.printError("Имя не распознано!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (MustBeNotEmptyException exception) {
                console.printError("Имя не может быть пустым!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidValueException exception) {
                console.printError("Имя не может начинаться с знаков препинания, чисел или специальных символов!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }

    /**
     * Ввод паспортных данных покупателя
     */
    private String inputPassportID() throws IncorrectScriptException {
        String passportID;
        boolean fileMode = InputSteamer.getFileMode();
        Pattern pattern = Pattern.compile("^[\\p{L}\\d]+$");
        while (true) {
            try {
                console.println("Введите номер паспорта покупателя (от 8 до 42 символов):");
                console.ps1();
                passportID = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(passportID);
                if (passportID.length() < 8 || passportID.length() > 42)
                    throw new InvalidFormException();
                Matcher matcher = pattern.matcher(passportID);
                if (!matcher.matches()) throw new InvalidValueException();

                break;
            } catch (NoSuchElementException exception) {
                console.printError("Номер паспорта не распознан!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidFormException exception) {
                console.printError("Номер паспорта должен содержать от 8 до 42 символов!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidValueException exception) {
                console.printError("Номер паспорта не должен содержать специальных символов и знаков препинания!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return passportID;
    }

    /**
     * Ввод цвета волос покупателя
     */
    private Color inputHairColor() throws IncorrectScriptException {
        boolean fileMode = InputSteamer.getFileMode();
        String strColor;
        Color color;
        while (true) {
            try {
                console.println("Список цветов волос покупателя - " + Color.names());
                console.println("Введите цвет волос:");
                console.ps1();
                strColor = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strColor);
                color = Color.valueOf(strColor.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Цвет волос не распознан!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalArgumentException exception) {
                console.printError("Цвета волос нет в списке!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return color;
    }

    /**
     * Ввод национальности покупателя
     */
    private Country inputNationality() throws IncorrectScriptException {
        boolean fileMode = InputSteamer.getFileMode();

        String strCountry;
        Country country;
        while (true) {
            try {
                console.println("Список стран - " + Country.names());
                console.println("Введите национальность покупателя:");
                console.ps1();
                strCountry = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strCountry);
                country = Country.valueOf(strCountry.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Национальность покупателя не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalArgumentException exception) {
                console.printError("Национальности нет в списке!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return country;
    }

    /**
     * Ввод местоположения покупателя
     */
    private Location inputLocation() throws IncorrectScriptException {
        Location location;
        while (true) {
            console.println("Введите данные о местоположении покупателя:");
            long x = inputLocationX();
            int y = inputLocationY();
            String name = inputLocationName();
            location = new Location(x, y, name);
            if (location.validate()) break;
            else console.printError("Данные о местоположении некорректны!");
        }
        return location;
    }

    /**
     * Ввод X координаты местоположения покупателя
     */
    private long inputLocationX() throws IncorrectScriptException {
        long x;
        boolean fileMode = InputSteamer.getFileMode();
        while (true) {
            try {
                console.println("Введите координату X покупателя (должно быть числом с плавающей точкой):");
                console.ps1();
                String strX = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strX);
                double tempX = Double.parseDouble(strX);
                if (tempX < Long.MIN_VALUE || tempX > Long.MAX_VALUE) throw new InvalidRangeException();
                x = (long) tempX;
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата X не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Координата X должна быть представлена числом с плавающей точкой! Значение должно быть в диапазоне от -9223372036854775808 до 9223372036854775807!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidRangeException exception) {
                console.printError("Значение должно быть в диапазоне от -9223372036854775808 до 9223372036854775807!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Ввод Y координаты местоположения покупателя
     */
    private int inputLocationY() throws IncorrectScriptException {
        int y;
        boolean fileMode = InputSteamer.getFileMode();
        while (true) {
            try {
                console.println("Введите координату Y покупателя (должно быть целым числом):");
                console.ps1();
                String strY = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strY);
                y = Integer.parseInt(strY);
                if (y < Integer.MIN_VALUE || y > Integer.MAX_VALUE) throw new InvalidRangeException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата Y не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Координата Y должна быть представлена целым числом! Значение должно быть в диапазоне от -2147483648 до 2147483647!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidRangeException exception) {
                console.printError("Значение должно быть в диапазоне от -2147483648 до 2147483647!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return y;
    }


    /**
     * Ввод названия местоположения
     */
    private String inputLocationName() throws IncorrectScriptException {
        String name;
        boolean fileMode = InputSteamer.getFileMode();
        Pattern pattern = Pattern.compile("^[^\\p{P}\\d]+$");
        while (true) {
            try {
                console.println("Введите название местоположения покупателя:");
                console.ps1();
                name = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(name);
                if (name.isEmpty()) throw new MustBeNotEmptyException();
                Matcher matcher = pattern.matcher(name);
                if (!matcher.matches()) throw new InvalidValueException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Название местоположения не распознано!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (MustBeNotEmptyException exception) {
                console.printError("Название местоположения не может быть пустым!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidValueException exception) {
                console.printError("Название не может содержать знаки препинания, числа или специальные символы!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }
}


