package ru.itmo.general.io;

import java.util.Scanner;
/**
 * Класс для управления потоком ввода в приложении
 */

public class InputSteamer {
    /** Переменная, указывающая на режим файла */
    private static boolean fileMode = false;

    /** Переменная для сканера */
    private static Scanner scanner;

    /**
     * Получает текущий режим файла
     * @return true, если включен режим файла, иначе false
     */
    public static boolean getFileMode() {
        return fileMode;
    }

    /**
     * Устанавливает режим файла
     * @param mode true, чтобы включить режим файла, иначе false
     */
    public static void setFileMode(boolean mode) {
        fileMode = mode;
    }

    /**
     * Получает сканер
     * @return объект Scanner
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * Устанавливает сканер
     * @param scanner1 объект Scanner для установки
     */
    public static void setScanner(Scanner scanner1) {
        scanner = scanner1;
    }
}
