package ru.itmo.lab5.utility;

import ru.itmo.lab5.exceptions.ScriptRecursionException;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.input.InputSteamer;
import ru.itmo.lab5.managers.CommandManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Класс для выполнения программы
 */
public class Executor {
    /** Консоль для вывода информации и сообщений об ошибках */
    private final Console console;
    /** Менеджер команд */
    private final CommandManager commandManager;
    /** Стек скриптов для контроля рекурсии */
    private final Set<String> scriptStack = new HashSet<>();
    boolean flag = false;
    Integer depth = 0;

    /**
     * Конструктор класса.
     * @param console Консоль для вывода информации и сообщений об ошибках
     * @param commandManager Менеджер команд
     */
    public Executor(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Интерактивный режим
     */
    public void fromConsole() {
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
     * @param argument Аргумент скрипта
     * @return Код завершения.
     */
    public ExitCode fromScript(String argument) {
        Integer maxDepth = 3;
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
                    if (!scriptStack.add(inputCommand[1])) {
                        if (!flag) {
                            InputSteamer.setScanner(tmpScanner);
                            InputSteamer.setFileMode(false);
                            InputSteamer.setFileMode(false);
                            InputSteamer.setScanner(scriptScanner);
                            InputSteamer.setFileMode(true);
                            flag = true;
                        }
                        depth++;
                    }
                    if (depth > maxDepth) throw new ScriptRecursionException();
                }
                exitCode = apply(inputCommand);
            } while (exitCode == ExitCode.OK && scriptScanner.hasNextLine());

            InputSteamer.setScanner(tmpScanner);
            InputSteamer.setFileMode(false);
            flag = false;
            depth = 0;

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
     * Выполняет команду
     * @param inputCommand Команда для запуска
     * @return Код завершения.
     */
    private ExitCode apply(String[] inputCommand) {
        if (inputCommand[0].equals("")) return ExitCode.OK;
        ru.itmo.lab5.comands.Command command = commandManager.getCommands().get(inputCommand[0]);

        if (command == null) {
            console.printError("Команда '" + inputCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }

        if (inputCommand[0].equals("exit")) {
            if (command.execute(inputCommand)) return ExitCode.EXIT;
            else return ExitCode.ERROR;
        } else if (inputCommand[0].equals("execute_script")) {
            if (command.execute(inputCommand)) return fromScript(inputCommand[1]);
            else return ExitCode.ERROR;
        } else if (!command.execute(inputCommand))
            return ExitCode.ERROR;

        return ExitCode.OK;
    }


    /** Перечисление для кодов завершения */
    public enum ExitCode {
        OK,
        ERROR,
        EXIT;
    }
}
