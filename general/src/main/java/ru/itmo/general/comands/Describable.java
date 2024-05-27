package ru.itmo.general.comands;

/**
 * Интерфейс для объектов, которые могут иметь имя и описание.
 */
public interface Describable {

    /**
     * Получает имя объекта.
     *
     * @return имя объекта
     */
    String getName();

    /**
     * Получает описание объекта.
     *
     * @return описание объекта
     */
    String getDescription();
}
