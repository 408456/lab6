package ru.itmo.client.app;

import ru.itmo.client.network.TCPClient;
import ru.itmo.general.comands.Command;
import ru.itmo.general.utility.exceptions.ScriptRecursionException;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.InputSteamer;
import ru.itmo.general.managers.CommandManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Класс для выполнения программы клиента.
 */
public class Executor {

    private final Console console; /**< Консоль для вывода информации и сообщений об ошибках */
    private final CommandManager commandManager; /**< Менеджер команд */
    private final Set<String> scriptStack = new HashSet<>(); /**< Стек скриптов для контроля рекурсии */
    private final TCPClient tcpClient;
    private Request request;
    private Response response;

    /**
     * Конструктор класса.
     *
     * @param console        Консоль для вывода информации и сообщений об ошибках
     * @param commandManager Менеджер команд
     * @param port           Порт для подключения к серверу
     * @param serverAddress  Адрес сервера
     */
    public Executor(Console console, CommandManager commandManager, int port, String serverAddress) {
        this.console = console;
        this.commandManager = commandManager;
        this.tcpClient = new TCPClient(serverAddress, port, console);
    }

    /**
     * Интерактивный режим работы.
     * Подключается к серверу и обрабатывает ввод команд с консоли.
     */
    public void fromConsole() {
        try {
            tcpClient.connect();
        } catch (TimeoutException e) {
            console.printError("Тайм-аут ожидания TCP соединения");
        }
        Scanner userScanner = InputSteamer.getScanner();
        try {
            ExitCode exitCode = ExitCode.OK;
            String[] inputCommand;

            while (exitCode != ExitCode.EXIT) {
                console.ps1();
                try {
                    inputCommand = (userScanner.nextLine().trim() + " ").split(" ", 2);
                    inputCommand[1] = inputCommand[1].trim();
                } catch (NoSuchElementException e) {
                    console.println("Ввод принудительно завершен. Сохранение данных в файл...");
                    String[] errorCommand = {"save", ""};
                    commandManager.getCommands().get("save").execute(errorCommand);
                    break;
                }

                commandManager.addToHistory(inputCommand[0]);
                exitCode = apply(inputCommand);
            }
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        }
    }

    /**
     * Режим для запуска скрипта.
     *
     * @param argument Путь к файлу скрипта
     * @return Код завершения
     */
    public ExitCode fromScript(String argument) {
        String[] inputCommand;
        ExitCode exitCode;
        scriptStack.add(argument);

        if (!new File(argument).exists()) {
            argument = "../" + argument;
        }

        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            Scanner tmpScanner = InputSteamer.getScanner();
            InputSteamer.setScanner(scriptScanner);
            InputSteamer.setFileMode(true);

            do {
                inputCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                inputCommand[1] = inputCommand[1].trim();

                while (scriptScanner.hasNextLine() && inputCommand[0].isEmpty()) {
                    inputCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    inputCommand[1] = inputCommand[1].trim();
                }

                console.println(console.getPS1() + String.join(" ", inputCommand));
                if (inputCommand[0].equals("execute_script")) {
                    if (!scriptStack.add(inputCommand[1])) throw new ScriptRecursionException();
                }
                exitCode = apply(inputCommand);
            } while (exitCode == ExitCode.OK && scriptScanner.hasNextLine());

            InputSteamer.setScanner(tmpScanner);
            InputSteamer.setFileMode(false);

            if (exitCode == ExitCode.ERROR && !(inputCommand[0].equals("execute_script") && !inputCommand[1].isEmpty())) {
                console.println("Проверьте скрипт на корректность введенных данных!");
            }

            return exitCode;
        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.printError("Файл со скриптом пуст!");
        } catch (ScriptRecursionException exception) {
            console.printError("Максимальная глубина рекурсии превышена!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(argument);
        }
        return ExitCode.ERROR;
    }

    /**
     * Выполняет команду.
     *
     * @param inputCommand Команда для выполнения
     * @return Код завершения
     */
    private ExitCode apply(String[] inputCommand) {
        request = null;
        response = null;
        if (inputCommand[0].equals("")) return ExitCode.OK;
        Command command = commandManager.getCommands().get(inputCommand[0]);

        if (command == null) {
            console.printError("Команда '" + inputCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }
        request = command.execute(inputCommand);
        if (request.getCommand().equals("exit")) {
            response = tcpClient.sendCommand(request);
            return ExitCode.EXIT;
        } else if (request.getCommand().equals("execute_script")) {
            if (request.isSuccess()) return fromScript(inputCommand[1]);
            else return ExitCode.ERROR;
        } else if (request.getCommand().equals("help")) {
            try {

                console.printItalic("Справка по командам:");
                Map<String, String> commandsInfo = (Map<String, String>) request.getData();
                commandsInfo.forEach((name, description) -> console.printTable(name, description));
                return ExitCode.OK;

            } catch (Exception e) {
                console.printError("Ошибка при выполнении команды help");
            }
        } else if (request.getCommand().equals("history")) {
            try {
                List<String> history = (List<String>) request.getData();
                console.println("История команд:");
                for (String x : history) {
                    console.println(x);
                }
                return ExitCode.OK;
            } catch (Exception e) {
                console.printError("Не удалось получить историю команд");
                return ExitCode.ERROR;
            }
        } else if (!request.isSuccess()) {
            console.printError(request);
            return ExitCode.ERROR;
        }
        response = tcpClient.sendCommand(request);
        if (response != null) {
            if (response.isSuccess()) {
                console.println(response);
            } else {
                console.printError(response);
            }
        } else {
            console.printError("Ответ от сервера не получен");
        }
        return ExitCode.OK;
    }

    /**
     * Перечисление для кодов завершения.
     */
    public enum ExitCode {
        OK,
        ERROR,
        EXIT
    }
}
