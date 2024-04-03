package ru.itmo.lab5.data;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum UnitOfMeasure {
    METERS,
    CENTIMETERS,
    LITERS;
    /**
     * @return Строка со всеми элементами enum'а через запятую.
     */
    public static String names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}