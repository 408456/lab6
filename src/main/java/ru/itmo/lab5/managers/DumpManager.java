package ru.itmo.lab5.managers;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import ru.itmo.lab5.data.Product;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;
import ru.itmo.lab5.input.Console;

/**
 * Использует файл для сохранения и загрузки коллекции.
 */
public class DumpManager {
    /** Имя файла для сохранения и загрузки коллекции */
    private final String fileName;
    /** Консоль для вывода информации и сообщений об ошибках */
    private final Console console;

    /**
     * Конструктор класса.
     * @param fileName Имя файла для сохранения и загрузки коллекции
     * @param console Консоль для вывода информации и сообщений об ошибках
     */
    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    /**
     * Преобразует коллекцию в CSV-строку.
     * @param collection Коллекция для преобразования
     * @return CSV-строка
     */
    private String collection2CSV(Collection<Product> collection) {
        try {
            StringWriter sw = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(sw);
            for (Product e : collection) {
                csvWriter.writeNext(Product.toArray(e));
            }
            csvWriter.close();
            return sw.toString();
        } catch (Exception e) {
            console.printError("Ошибка сериализации");
            return null;
        }
    }

    /**
     * Записывает коллекцию в файл.
     * @param collection Коллекция для записи
     */
    public void writeCollection(Collection<Product> collection) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String csv = collection2CSV(collection);
            if (csv == null) return;
            writer.write(csv);
            console.println("Коллекция успешно сохранена в файл!");
        } catch (IOException e) {
            console.printError("Неожиданная ошибка сохранения");
        }
    }
    /**
     * Преобразует CSV-строку в коллекцию.
     * @param s CSV-строка
     * @return Коллекция продуктов
     */
    private LinkedList<Product> CSV2collection(String s) {
        try {
            StringReader sr = new StringReader(s);
            CSVReader csvReader = new CSVReader(sr);
            LinkedList<Product> ds = new LinkedList<>();
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                Product p = Product.fromArray(record);
                if (p != null && p.validate()) {
                    ds.add(p);
                } else {
                    console.printError("Файл с коллекцией содержит недействительные данные");
                }
            }
            csvReader.close();
            return ds;
        } catch (Exception e) {
            console.printError("Ошибка десериализации");
            return null;
        }
    }

    /**
     * Считывает коллекцию из файла.
     * @return Считанная коллекция
     */
    public LinkedList<Product> readCollection() {
        LinkedList<Product> collection = new LinkedList<>();
        if (fileName != null && !fileName.isEmpty()) {
            try (Scanner fileReader = new Scanner(new File(fileName))) {
                StringBuilder s = new StringBuilder();
                while (fileReader.hasNextLine()) {
                    s.append(fileReader.nextLine());
                    s.append("\n");
                }
                LinkedList<Product> loadedCollection = CSV2collection(s.toString());
                if (loadedCollection != null && !loadedCollection.isEmpty()) {
                    console.println("Коллекция успешно загружена!");
                    return loadedCollection;
                } else {
                    console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
                }
            } catch (FileNotFoundException exception) {
                console.printError("Загрузочный файл не найден!");
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        } else {
            console.printError("Аргумент командной строки с загрузочным файлом не найден!");
        }
        return collection;
    }
}
