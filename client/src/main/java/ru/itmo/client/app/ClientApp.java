package ru.itmo.client.app;

import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.InputSteamer;

import java.util.Scanner;

/**
 * Основной класс клиентского приложения.
 */
public class ClientApp {
    private static final int PORT = 8001; /**< Порт для подключения к серверу */
    private static final String SERVER_ADDRESS = "localhost"; /**< Адрес сервера */
    public static void main(String[] args) {
        InputSteamer.setScanner(new Scanner(System.in));
        Console console = new Console();

        new Runner(console, PORT, SERVER_ADDRESS).fromConsole();
    }
}