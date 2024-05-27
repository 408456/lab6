package ru.itmo.general.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Перечисление, представляющее единицы измерения.
 */
public enum UnitOfMeasure implements Serializable {
    METERS,         // Метры
    CENTIMETERS,    // Сантиметры
    LITERS;         // Литры

    /**
     * Возвращает строку, содержащую названия всех элементов перечисления, разделенных запятыми.
     *
     * @return строка с названиями единиц измерения
     */
    public static String names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
