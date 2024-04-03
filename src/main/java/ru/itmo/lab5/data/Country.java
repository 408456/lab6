package ru.itmo.lab5.data;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Country {
    UNITED_KINGDOM,
    GERMANY,
    FRANCE,
    ITALY;

    /**
     * Возвращает строку, содержащую названия всех стран, разделенных запятыми.
     *
     * @return строка с названиями стран
     */
    public static String names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
