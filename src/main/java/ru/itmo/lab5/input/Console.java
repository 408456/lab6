package ru.itmo.lab5.input;

public class Console {
    private static final String PS1 = "> "; // PS1

    /**
     * Выводит obj.toString() в консоль
     * @param obj Объект для печати
     */
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит obj.toString() + \n в консоль
     * @param obj Объект для печати
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Выводит error: obj.toString() в консоль
     *
     * @param obj Ошибка для печати
     */
    public void printError(Object obj) {
        System.out.println("error: " + obj);
    }

    /**
     * Выводит таблицу из 2 колонок
     *
     * @param elementLeft  Левый элемент колонки.
     * @param elementRight Правый элемент колонки.
     */
    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-40s%-1s%n", elementLeft, elementRight);
    }

    public void ps1() {
        print(PS1);
    }

    /**
     * @return PS1 текущей консоли
     */
    public String getPS1() {
        return PS1;
    }

}