package ru.itmo.server.comands;

import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Интерфейс для всех исполняемых команд.
 */
public interface Executable {

    /**
     * Выполняет команду с указанными аргументами.
     *
     * @param arguments аргументы команды
     * @return ответ, указывающий результат выполнения команды
     */
    default Response execute(Request arguments) {
        return null;
    }
}
