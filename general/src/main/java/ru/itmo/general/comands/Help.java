package ru.itmo.general.comands;

import ru.itmo.general.managers.CommandManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Команда для вывода справки о доступных командах.
 */
public class Help extends Command {
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", " - вывести справку по доступным командам");
        this.commandManager = commandManager;
    }

    @Override
    public Request execute(String[] arguments) {
        if (arguments.length != 2) {
            return new Request(false, getName(), "Пожалуйста введите команду в правильном формате");
        }

        Map<String, String> commandsInfo = CommandManager.getCommands().values().stream()
                .collect(Collectors.toMap(Command::getName, Command::getDescription));

        return new Request(getName(), commandsInfo);
    }
}
