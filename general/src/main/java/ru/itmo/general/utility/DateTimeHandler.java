package ru.itmo.lab5.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateTimeHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    /**
     * Преобразует строку в момент времени Instant.
     *
     * @param dateString строковое представление даты и времени
     * @return момент времени Instant
     * @throws IllegalArgumentException если строка не соответствует ожидаемому формату
     */

    public static Instant parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка парсинга строки даты", e);
        }
        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    /**
     * Преобразует момент времени Instant в объект Date.
     *
     * @param instant момент времени Instant
     * @return объект Date
     */
    public static Date instantToDate(Instant instant) {
        return Date.from(instant);
    }

    /**
     * Преобразует объект Date в момент времени Instant.
     *
     * @param date объект Date
     * @return момент времени Instant
     */
    public static Instant dateToInstant(Date date) {
        return date.toInstant();
    }

    /**
     * Форматирует момент времени Instant в строку.
     *
     * @param instant момент времени Instant
     * @return строковое представление даты и времени
     */
    public static String formatInstantToString(Instant instant) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        return dateTime.format(FORMATTER);
    }
}
