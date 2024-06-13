package ru.itmo.server.managers;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;
import ru.itmo.general.utility.io.Console;
import ru.itmo.server.comands.*;
import ru.itmo.server.dao.UserDAO;


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
        if (command == null) return new Response(false, request.getCommand(), "Команда не найдена! Введите 'help'");
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

//    public static void initClientCommands(Console console) {
//        commandAdd("clear", new Clear());
//        commandAdd("count_less_than_owner", new CountLessThanOwner(console));
//        commandAdd("execute_script", new ExecuteScript());
//        commandAdd("exit", new Exit());
//        commandAdd("help", new Help(new CommandManager()));
//        commandAdd("history", new History(new CommandManager()));
//        commandAdd("info", new Info());
//        commandAdd("insert", new Insert(console));
//        commandAdd("print_field_ascending_owner", new PrintFieldAscendingOwner());
//        commandAdd("print_field_descending_price", new PrintFieldDescendingPrice());
//        commandAdd("remove_greater", new RemoveGreater(console));
//        commandAdd("remove_greater_key", new RemoveGreaterKey());
//        commandAdd("remove_lower_key", new RemoveLowerKey());
//        commandAdd("show", new Show());
//        commandAdd("update", new Update(console));
//    }

    public static void initServerCommands(CollectionManager productCollectionManager, UserDAO userDAO) {
        commandAdd("help", new Help(new CommandManager()));
        commandAdd("clear", new Clear(userDAO, productCollectionManager));
        commandAdd("count_less_than_owner", new CountLessThanOwner(productCollectionManager));
        commandAdd("exit", new Exit(productCollectionManager));
        commandAdd("info", new Info(productCollectionManager));
        commandAdd("insert", new Insert(productCollectionManager));
        commandAdd("print_field_ascending_owner", new PrintFieldAscendingOwner(productCollectionManager));
        commandAdd("print_field_descending_price", new PrintFieldDescendingPrice(productCollectionManager));
        commandAdd("remove_greater", new RemoveGreater(productCollectionManager, userDAO));
        commandAdd("remove_greater_key", new RemoveGreaterKey(productCollectionManager, userDAO));
        commandAdd("remove_lower_key", new RemoveLowerKey(productCollectionManager, userDAO));
        commandAdd("show", new Show(productCollectionManager));
        commandAdd("update", new Update(productCollectionManager));
        commandAdd("help", new Help(new CommandManager()));
        commandAdd("register", new Register(userDAO));
        commandAdd("login", new Login(userDAO));
    }
}
