package ru.itmo.lab5.input;

import ru.itmo.lab5.data.Product;
import ru.itmo.lab5.data.Color;
import ru.itmo.lab5.data.Coordinates;
import ru.itmo.lab5.data.Country;
import ru.itmo.lab5.data.Location;
import ru.itmo.lab5.data.Person;
import ru.itmo.lab5.data.UnitOfMeasure;
import ru.itmo.lab5.exceptions.IncorrectScriptException;
import ru.itmo.lab5.exceptions.InvalidFormException;
import ru.itmo.lab5.exceptions.InvalidRangeException;
import ru.itmo.lab5.exceptions.InvalidValueException;
import ru.itmo.lab5.exceptions.MustBeNotEmptyException;

import java.util.regex.*;


import java.util.NoSuchElementException;

public class ProductInput {
    private final Console console;

    public ProductInput(Console console) {
        this.console = console;
    }

    public Product make() throws IncorrectScriptException, InvalidFormException, InvalidValueException {
        var product = new Product(inputProductName(), inputCoordinates(), inputPrice(), inputUnitOfMeasure(), inputPerson());
        if (!product.validate()) throw new InvalidFormException();
        return product;
    }

    /**
     * Ввод названия продукта
     */
    private String inputProductName() throws IncorrectScriptException {
        String name;
        var fileMode = InputSteamer.getFileMode();
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
        Integer x;
        var fileMode = InputSteamer.getFileMode();
        while (true) {
            try {
                console.println("Введите координату X (должно быть целым числом):");
                console.ps1();
                var strX = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strX);
                x = Integer.parseInt(strX);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата X не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Координата X должна быть представлена целым числом!");
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
        Double y;
        var fileMode = InputSteamer.getFileMode();
        while (true) {
            try {
                console.println("Введите координату Y (должно быть числом с плавающей точкой):");
                console.ps1();
                var strY = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strY);
                y = Double.parseDouble(strY);
                if (y < -Double.MAX_VALUE || y > Double.MAX_VALUE) throw new InvalidValueException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата Y не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Координата Y должна быть представлена числом с плавающей точкой!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidValueException exception) {
                console.printError("Координата Y должна быть представлена числом с плавающей точкой от от 4.9e-324 до 1.8e+308!");
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
        var fileMode = InputSteamer.getFileMode();
        Integer price;
        while (true) {
            try {
                console.println("Введите цену продукта:");
                console.ps1();
                var strPrice = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strPrice);
                price = Integer.parseInt(strPrice);
                if (price <= 0) throw new InvalidRangeException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Цена продукта не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidRangeException exception) {
                console.printError("Цена должна быть больше нуля!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Цена продукта должна быть представлена целым числом от -2147483648 до 2147483647!");
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
        var fileMode = InputSteamer.getFileMode();

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


    /**
     * Создание покупателя
     */
    private Person inputPerson() throws IncorrectScriptException {
        var person = new Person(inputPersonName(), inputPassportID(), inputHairColor(), inputNationality(), inputLocation());
        return person;
    }

    /**
     * Ввод имени покупателя
     */
    private String inputPersonName() throws IncorrectScriptException {
        String name;
        var fileMode = InputSteamer.getFileMode();
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
        var fileMode = InputSteamer.getFileMode();
        Pattern pattern = Pattern.compile("^[\\p{L}\\d]+$");
        while (true) {
            try {
                console.println("Введите номер паспорта покупателя (до 42 символов):");
                console.ps1();
                passportID = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(passportID);
                if (passportID.isEmpty() || passportID.length() > 42) throw new InvalidFormException();
                Matcher matcher = pattern.matcher(passportID);
                if (!matcher.matches()) throw new InvalidValueException();

                break;
            } catch (NoSuchElementException exception) {
                console.printError("Номер паспорта не распознан!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (InvalidFormException exception) {
                console.printError("Номер паспорта не соответствует формату (строка до 42 символов)!");
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
        var fileMode = InputSteamer.getFileMode();
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
        var fileMode = InputSteamer.getFileMode();

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
        var fileMode = InputSteamer.getFileMode();
        while (true) {
            try {
                console.println("Введите координату X покупателя (должно быть числом с плавающей точкой):");
                console.ps1();
                var strX = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strX);
                x = Long.parseLong(strX);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата X не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Координата X должна быть представлена числом с плавающей точкой!");
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
        var fileMode = InputSteamer.getFileMode();
        while (true) {
            try {
                console.println("Введите координату Y покупателя (должно быть целым числом):");
                console.ps1();
                var strY = InputSteamer.getScanner().nextLine().trim();
                if (fileMode) console.println(strY);
                y = Integer.parseInt(strY);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата Y не распознана!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (NumberFormatException exception) {
                console.printError("Координата Y должна быть представлена целым числом от от -2147483648 до 2147483647!");
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
        var fileMode = InputSteamer.getFileMode();
        Pattern pattern = Pattern.compile("^[^\\p{P}\\d\\s].*");
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
                console.printError("Название не может начинаться с знаков препинания, чисел или специальных символов!");
                if (fileMode) throw new IncorrectScriptException();
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }

}


