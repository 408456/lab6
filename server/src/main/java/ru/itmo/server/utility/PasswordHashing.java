package ru.itmo.server.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Утилитарный класс для хеширования паролей с использованием алгоритма MD2 и случайной строки.
 *
 */
public class PasswordHashing {

    /**
     * Хеширует указанный пароль с использованием алгоритма MD2 и случайно сгенерированной строки.
     *
     * @param password Пароль для хеширования.
     * @return Массив, содержащий хешированный пароль и использованную для хеширования строку.
     */
    public static String[] hashPassword(String password) {
        String salt = generateSalt(16);
        String hashedPassword = hashPassword(password, salt);
        return new String[]{hashedPassword, salt};
    }

    /**
     * Хеширует указанный пароль с использованием алгоритма MD2 и предоставленной соли.
     *
     * @param password Пароль для хеширования.
     * @param salt     Случайная строка, используемая для хеширования.
     * @return Хешированный пароль.
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD2");
            md.update((salt + password).getBytes());
            byte[] hashedBytes = md.digest();
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Алгоритм MD2 не найден");
            return null;
        }
    }

    /**
     * Проверяет введенный пароль по отношению к хешированному паролю с использованием предоставленной случайной строки.
     *
     * @param inputPassword  Пароль для проверки.
     * @param salt           Случайная строка, использованная для хеширования.
     * @param hashedPassword Хешированный пароль для сравнения.
     * @return true, если введенный пароль совпадает с хешированным паролем, иначе false.
     */
    public static boolean verifyPassword(String inputPassword, String salt, String hashedPassword) {
        String hashedInputPassword = hashPassword(inputPassword, salt);
        return hashedInputPassword != null && hashedInputPassword.equals(hashedPassword);
    }

    /**
     * Генерирует случайную строку случайной длины.
     *
     * @param length Длина строки для генерации.
     * @return Случайно сгенерированная строка.
     */
    public static String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
