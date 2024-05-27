package ru.itmo.general.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Перечисление, представляющее возможные страны.
 */
public enum Country implements Serializable {
    UNITED_KINGDOM, // Соединенное Королевство
    GERMANY,        // Германия
    FRANCE,         // Франция
    ITALY;          // Италия

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
