package ru.itmo.general.comands;

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

    /**
     * Выполняет команду с указанными аргументами.
     *
     * @param arguments аргументы команды в виде массива строк
     * @return запрос, указывающий результат выполнения команды и содержащий необходимые данные для сервера
     */
    default Request execute(String[] arguments) {
        return null;
    }
}
