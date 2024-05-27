package ru.itmo.general.data;

import java.io.Serializable;

/**
 * Перечисление, представляющее возможные цвета.
 */
public enum Color implements Serializable {
    YELLOW, // Жёлтый
    ORANGE, // Оранжевый
    BROWN;  // Коричневый

    /**
     * Возвращает строку, содержащую названия всех цветов, разделенных запятыми.
     *
     * @return строка с названиями цветов
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (Color colorType : values()) {
            nameList.append(colorType.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
