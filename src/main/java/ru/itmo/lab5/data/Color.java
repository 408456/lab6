package ru.itmo.lab5.data;
/**
 *
 *
 */
public enum Color {
    YELLOW,
    ORANGE,
    BROWN;
    /**
     * Возвращает строку, содержащую названия всех цветов, разделенных запятыми.
     *
     * @return строка с названиями цветов
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var colorType : values()) {
            nameList.append(colorType.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}