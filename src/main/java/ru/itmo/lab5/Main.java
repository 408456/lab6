package ru.itmo.lab5;

import ru.itmo.lab5.comands.Clear;
import ru.itmo.lab5.comands.CountLessThanOwner;
import ru.itmo.lab5.comands.ExecuteScript;
import ru.itmo.lab5.comands.Exit;
import ru.itmo.lab5.comands.Help;
import ru.itmo.lab5.comands.History;
import ru.itmo.lab5.comands.Info;
import ru.itmo.lab5.comands.Insert;
import ru.itmo.lab5.comands.PrintFieldAscendingOwner;
import ru.itmo.lab5.comands.PrintFieldDescendingPrice;
import ru.itmo.lab5.comands.RemoveGreaterKey;
import ru.itmo.lab5.comands.RemoveLowerKey;
import ru.itmo.lab5.comands.Save;
import ru.itmo.lab5.comands.Show;
import ru.itmo.lab5.comands.Update;
import ru.itmo.lab5.managers.CollectionManager;
import ru.itmo.lab5.managers.CommandManager;
import ru.itmo.lab5.input.Console;
import ru.itmo.lab5.input.InputSteamer;
import ru.itmo.lab5.managers.DumpManager;
import ru.itmo.lab5.utility.Executor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        InputSteamer.setScanner(new Scanner(System.in));
        var console = new Console();

        if (args.length == 0) {
            console.printError("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        var dumpManager = new DumpManager(args[0], console);
        var collectionManager = new CollectionManager(dumpManager);

        collectionManager.validateAll(console);

        var commandController = new CommandManager(){
            {
                commandAdd("clear", new Clear(console, collectionManager));
                commandAdd("count_less_than_owner", new CountLessThanOwner(console, collectionManager));
                commandAdd("execute_script", new ExecuteScript(console));
                commandAdd("exit", new Exit(console));
                commandAdd("help", new Help(console, this));
                commandAdd("history", new History(console, this));
                commandAdd("info", new Info(console, collectionManager));
                commandAdd("insert", new Insert(console, collectionManager));
                commandAdd("print_field_ascending_owner", new PrintFieldAscendingOwner(console, collectionManager));
                commandAdd("print_field_descending_price", new PrintFieldDescendingPrice(console, collectionManager));
                commandAdd("remove_greater", new ru.itmo.lab5.comands.RemoveGreater(console, collectionManager));
                commandAdd("remove_greater_key", new RemoveGreaterKey(console, collectionManager));
                commandAdd("remove_lower_key", new RemoveLowerKey(console, collectionManager));
                commandAdd("save", new Save(console, collectionManager));
                commandAdd("show", new Show(console, collectionManager));
                commandAdd("update", new Update(console, collectionManager));
            }
        };

        new Executor(console, commandController).fromConsole();
    }
}