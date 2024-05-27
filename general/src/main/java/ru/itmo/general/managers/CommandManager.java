package ru.itmo.general.managers;

import lombok.Getter;
import ru.itmo.general.comands.*;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс управления командами
 */
public class CommandManager {

    @Getter
    private static final Map<String, Command> commands = new HashMap<>();
    private static final List<String> commandHistory = new ArrayList<>();

    public static Response handle(Request request) {
        var command = commands.get(request.getCommand());
        if (command == null) return new Response(false, request.getCommand(), "Команда не найдена!");
        if (!"exit".equals(request.getCommand()) && !"save".equals(request.getCommand())) {
            return command.execute(request);
        }
        return new Response(false, "Неизвестная команда");
    }

    /**
     * Добавляет команду.
     *
     * @param commandName Название команды.
     * @param command     Команда.
     */
    public static void commandAdd(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public static void handleServer(Request request) {
        var command = commands.get(request.getCommand());
        if (command == null) return;
        command.execute(request);
    }

    /**
     * @return История команд.
     */
    public List<String> getCommandHistory() {
        if (commandHistory.size() >= 8) return commandHistory.subList(commandHistory.size() - 8, commandHistory.size());
        else return commandHistory;
    }

    /**
     * Добавляет команду в историю.
     *
     * @param command Команда.
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }

}