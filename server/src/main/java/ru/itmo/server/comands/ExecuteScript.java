package ru.itmo.server.comands;

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

}
