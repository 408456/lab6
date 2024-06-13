package ru.itmo.server.comands;

import ru.itmo.general.network.Response;
import ru.itmo.server.managers.CommandManager;
import ru.itmo.general.network.Request;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Команда для вывода справки о доступных командах.
 */
public class Help extends Command {
    private final CommandManager commandManager;

    /**
     * Конструктор класса.
     *
     * @param commandManager менеджер команд
     */
    public Help(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.commandManager = commandManager;
    }
    @Override
    public Response execute(Request request) {
        Map<String, String> commandsInfo = commandManager.getCommands().values().stream()
                .collect(Collectors.toMap(Command::getName, Command::getDescription));
        StringBuilder table = new StringBuilder();
        table.append(String.format("%-30s  %-50s%n", "", ""));
//        table.append(String.format("%-30s | %-50s%n", "", ""));
        commandsInfo.forEach((name, description) -> {
            table.append(String.format("%-30s  %-50s%n", name, description));
        });
        return new Response(true, table.toString(), null);
    }
}
