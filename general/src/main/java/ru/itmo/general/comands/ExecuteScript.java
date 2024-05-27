package ru.itmo.general.comands;

import ru.itmo.general.network.Request;

/**
 * Команда для исполнения скрипта.
 */
public class ExecuteScript extends Command {

    /**
     * Конструктор класса.
     */
    public ExecuteScript() {
        super("execute_script", "<file_name> исполнить скрипт из указанного файла");
    }

    /**
     * Выполняет команду на клиенте.
     *
     * @param arguments аргументы команды
     * @return запрос для выполнения на сервере
     */
    @Override
    public Request execute(String[] arguments) {
        if (arguments.length != 2 || arguments[1].isEmpty()) {
            return new Request(false, getName(), getUsingError());
        }
        return new Request(getName(), arguments[1]);
    }
}
