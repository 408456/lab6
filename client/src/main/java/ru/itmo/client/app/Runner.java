package ru.itmo.client.app;

import ru.itmo.client.network.TCPClient;
import ru.itmo.general.data.Person;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;
import ru.itmo.general.utility.exceptions.IncorrectScriptException;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.general.utility.exceptions.InvalidFormException;
import ru.itmo.general.utility.exceptions.ScriptRecursionException;
import ru.itmo.general.utility.io.Console;
import ru.itmo.general.utility.io.InputSteamer;
import ru.itmo.general.utility.io.ProductInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Класс для выполнения программы клиента.
 */
public class Runner {
    /**
     * < Консоль для вывода информации и сообщений об ошибках
     */
    private final Console console;
    /**
     * < Стек скриптов для контроля рекурсии
     */
    private final Set<String> scriptStack = new HashSet<>();
    private final TCPClient tcpClient;
    Response response;
    private boolean loggedIn = false;
    private List<String> commandHistory = new ArrayList<>();

    /**
     * Конструктор класса.
     *
     * @param console       Консоль для вывода информации и сообщений об ошибках
     * @param port          Порт для подключения к серверу
     * @param serverAddress Адрес сервера
     */
    public Runner(Console console, int port, String serverAddress) {
        this.console = console;
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
//            inputCommand = new String[]{"help", ""};
            console.printItalic("* первое подключение");
//            send(inputCommand);
            while (exitCode != ExitCode.EXIT) {
                console.ps1();
                try {
                    inputCommand = (userScanner.nextLine().trim() + " ").split(" ", 2);
                    inputCommand[1] = inputCommand[1].trim();
                } catch (NoSuchElementException e) {
//                    console.println("Ввод принудительно завершен. Сохранение данных в файл...");
//                    String[] errorCommand = {"save", ""};
//                    commandManager.getCommands().get("save").execute(errorCommand);
                    break;
                }

                addToHistory(inputCommand[0]);
                exitCode = send(inputCommand);
            }
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToHistory(String c) {
        commandHistory.add(c);
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
                exitCode = send(inputCommand);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            scriptStack.remove(argument);
        }
        return ExitCode.ERROR;
    }

    /**
     * Выполняет команду.
     *
     * @param userCommand Команда для выполнения
     * @return Код завершения
     */
    private ExitCode send(String[] userCommand) throws IOException {
        if (userCommand[0].isEmpty()) return ExitCode.OK;
        if ((!loggedIn &&
                !userCommand[0].equals("register") &&
                !userCommand[0].equals("login") &&
                !userCommand[0].equals("help")) &&
                !userCommand[0].equals("exit")) {
            console.println("Вы не вошли в систему.\nВведите help для получения списка команд\nregister <login> <password> для регистрации или login <password> для входа.");
            return ExitCode.OK;
        }
        response = null;
        try {
            switch (userCommand[0]) {
                case "exit" -> {
                    try {
                        tcpClient.sendRequest(new Request("exit", null));
                    } catch (Exception ignored) {
                    }
                    return ExitCode.EXIT;
                }
                case "execute_script" -> {
                    if (userCommand.length < 2 || userCommand[1].isEmpty()) {
                        console.printError("Необходимо указать файл скрипта.");
                        return ExitCode.ERROR;
                    }
                    return fromScript(userCommand[1]);
                }
                case "insert" -> handleInsert(userCommand);
                case "remove_greater" -> handleRemove(userCommand);
                case "count_less_than_owner" -> {
                    try {
                        ProductInput productInput = new ProductInput(console);
                        Person owner = productInput.inputPersonForCountLess();
                        response = tcpClient.sendCommand(new Request(userCommand[0], owner));
                    } catch (IllegalArgumentException | IncorrectScriptException e) {
                        console.printError("Ошибка ввода!");
                    }
                }
                case "update" -> handleUpdate(userCommand);
                case "remove_greater_key", "remove_lower_key" -> handleRemoveById(userCommand);
//                case "remove_by_id", "remove_greater_key", "remove_lower_key" -> handleRemoveById(userCommand);
                case "history" -> {
                    console.println("История команд:");
                    for (String com : commandHistory) {
                        console.println(com);
                    }
                }
                case "register", "login" -> {
                    try {
                        if (userCommand.length < 2)
                            throw new InvalidAmountException();
                        if (userCommand[1].split(" ").length < 2)
                            throw new InvalidAmountException();

                        Request request = new Request(userCommand[0], null);
                        tcpClient.setLogin(userCommand[1].split(" ")[0]);
                        tcpClient.setPassword(userCommand[1].split(" ")[1]);
                        response = tcpClient.sendCommand(request);
                        if (response != null && response.isSuccess()) {
                            loggedIn = true;
                        } else {
                            return ExitCode.ERROR;
                        }
                    } catch (InvalidAmountException exception) {
                        console.printError("Неправильное количество аргументов!");
                        console.println("Правильное использование: <команда> <логин> <пароль>");
                    }
                }
                default -> {
                    try {
                        if (userCommand.length != 2) {
                            throw new InvalidAmountException();
                        }
                        response = tcpClient.sendCommand(new Request(userCommand[0], userCommand[1]));
                        if (response == null || !response.isSuccess()) return ExitCode.ERROR;
                    } catch (InvalidAmountException e) {
                        console.printError("Неправильное количество аргументов!");
                        console.println("Правильное использование: <команда>");
                    }
                }
            }
        } finally {
            if (response != null) {
                if (response.isSuccess())
                    console.println(((response.getMessage() != null) ? response.getMessage() + '\n' : "") +
                            ((response.getData() != null) ? response.getData() : ""));
                else
                    console.printError(((response.getMessage() != null) ? response.getMessage() + '\n' : "") + ((response.getData() != null) ? response.getData() : ""));
            }
        }
        return ExitCode.OK;
    }

    private void handleInsert(String[] userCommand) {
        try {
            if (userCommand.length != 2 || userCommand[1].isEmpty())
                throw new InvalidAmountException();
            long id = Long.parseLong(userCommand[1]);
            console.println("* Создание нового продукта:");
            var product = (new ProductInput(console)).make();
            product.setId(id);
            response = tcpClient.sendCommand(new Request("insert", product));
        } catch (InvalidAmountException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Правильное использование: <команда> <аргументы>");
        } catch (InvalidFormException exception) {
            console.printError("Поля продукта не валидны! Продукт не создан.");
        } catch (IncorrectScriptException ignored) {
        }
    }

    private void handleRemove(String[] userCommand) {
        try {
            if (userCommand.length != 2 || userCommand[1].isEmpty())
                throw new InvalidAmountException();
            console.println("* Удаление продукта:");
            var product = (new ProductInput(console)).make();
            response = tcpClient.sendCommand(new Request(userCommand[0], product));
        } catch (InvalidAmountException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Правильное использование: <команда> <аргументы>");
        } catch (InvalidFormException exception) {
            console.printError("Поля продукта не валидны! Продукт не создан.");
        } catch (IncorrectScriptException ignored) {
        }
    }

    private void handleUpdate(String[] userCommand) {
        try {
            if (userCommand.length < 2 || userCommand[1].isEmpty())
                throw new InvalidAmountException();
            var id = Long.parseLong(userCommand[1]);
            console.println("* Обновление продукта:");
            var product = (new ProductInput(console)).make();
            product.setId(id);
            response = tcpClient.sendCommand(new Request(userCommand[0], product));
        } catch (InvalidAmountException | NumberFormatException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Правильное использование: <команда> <id>");
        } catch (InvalidFormException exception) {
            console.printError("Поля продукта не валидны! Продукт не создан.");
        } catch (IncorrectScriptException ignored) {
        }
    }

    private void handleRemoveById(String[] userCommand) {
        try {
            if (userCommand.length < 2 || userCommand[1].isEmpty())
                throw new InvalidAmountException();
            var id = Long.parseLong(userCommand[1]);
            response = tcpClient.sendCommand(new Request(userCommand[0], id));
        } catch (InvalidAmountException | NumberFormatException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Правильное использование: <команда> <id>");
        }
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
