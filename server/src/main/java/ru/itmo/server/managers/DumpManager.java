package ru.itmo.server.managers;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import ru.itmo.general.data.Product;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;


import ru.itmo.general.utility.io.Console;

/**
 * Использует файл для сохранения и загрузки коллекции.
 */
public class DumpManager {
    /**
     * Имя файла для сохранения и загрузки коллекции
     */
    private final String fileName;
    /**
     * Консоль для вывода информации и сообщений об ошибках
     */
    private final Console console;

    /**
     * Получает имя файла.
     *
     * @return Имя файла
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Конструктор класса.
     *
     * @param fileName Имя файла для сохранения и загрузки коллекции
     * @param console  Консоль для вывода информации и сообщений об ошибках
     */
    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    /**
     * Преобразует коллекцию в CSV-строку.
     *
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
     *
     * @param collection Коллекция для записи
     */
    public void writeCollection(Collection<Product> collection) {
        File file = new File(fileName);

        // Проверяем права на запись
        if (!file.canWrite()) {
            console.printError("Нет прав на запись в файл: " + fileName);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String csv = collection2CSV(collection);
            if (csv == null) return;
            writer.write(csv);
//            console.println("Коллекция успешно сохранена в файл!");
        } catch (IOException e) {
            console.printError("Неожиданная ошибка сохранения");
        }
    }

    /**
     * Преобразует CSV-строку в коллекцию.
     *
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
                try {
                    Product p = Product.fromArray(record);
                    if (p != null && p.validate()) {
                        ds.add(p);
                    } else {
                        console.printError("Файл с коллекцией содержит недействительные данные: " + String.join(", ", record));
                    }
                } catch (IllegalArgumentException e) {
                    console.printError("Ошибка при обработке строки: " + String.join(", ", record));
                    console.printError(e.getMessage());
                }
            }
            csvReader.close();
            return ds;
        } catch (Exception e) {
            console.printError("Ошибка десериализации");
            console.printError(e.getMessage());
            return null;
        }
    }


    /**
     * Считывает коллекцию из файла.
     *
     * @return Считанная коллекция или пустая коллекция, если возникла ошибка или файл пуст
     */
    public LinkedList<Product> readCollection() {
        LinkedList<Product> collection = new LinkedList<>();
        File file = new File(fileName);

        // Проверяем права на чтение
        if (!file.canRead()) {
            console.printError("Нет прав на чтение файла: " + fileName);
            return collection;  // Возвращаем пустую коллекцию
        }

        if (fileName != null && !fileName.isEmpty()) {
            try (Scanner fileReader = new Scanner(file)) {
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
                    console.printError("Коллекция пуста!");
                    return collection;  // Возвращаем пустую коллекцию
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
        return collection;  // Возвращаем пустую коллекцию
    }

}
