package ru.itmo.general.utility.exceptions;

/**
 * Выбрасывается, если количество не соответствует требованиям
 */

public class InvalidAmountException extends Exception {
    public InvalidAmountException() {
        super();
    }
    public InvalidAmountException(String s) {
        super(s);
    }
}
