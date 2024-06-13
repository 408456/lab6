package ru.itmo.server.comands;

import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.server.dao.UserDAO;

import javax.management.InstanceAlreadyExistsException;
import java.util.regex.Pattern;

/**
 * Команда 'register'. Регистрирует нового пользователя в системе.
 */
public class Register extends Command {
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 50;
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 50;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]{3,50}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,50}$");

    private UserDAO userDAO;

    public Register() {
        super("register", "{username} {password} регистрирует нового пользователя");
    }

    /**
     * Конструктор для создания команды Register.
     *
     * @param userDAO менеджер пользователей
     */
    public Register(UserDAO userDAO) {
        this();
        this.userDAO = userDAO;
    }

    /**
     * Выполняет команду.
     *
     * @param request запрос на регистрацию пользователя
     * @return ответ с результатом выполнения команды
     */
    @Override
    public Response execute(Request request) {
        try {
            String username = request.getLogin();
            String password = request.getPassword();

            if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
                throw new InvalidAmountException("Длина имени пользователя должна быть от " + MIN_USERNAME_LENGTH + " до " + MAX_USERNAME_LENGTH + " символов.");
            }

            if (!USERNAME_PATTERN.matcher(username).matches()) {
                throw new InvalidAmountException("Имя пользователя не должно содержать знаков препинания и специальных символов!");
            }

            if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
                throw new InvalidAmountException("Длина пароля должна быть от " + MIN_PASSWORD_LENGTH + " до " + MAX_PASSWORD_LENGTH + " символов.");
            }

            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                throw new InvalidAmountException("Пароль должен содержать от 8 до 50 символов и может включать любые знаки.");
            }

            if (request.getUserId() != null) {
                throw new InstanceAlreadyExistsException("Пользователь уже существует.");
            }

            var user = userDAO.insertUser(username, password);

            if (user == null) {
                throw new InstanceAlreadyExistsException("Пользователь уже существует.");
            }

            return new Response(true, "Пользователь успешно зарегистрирован.", user.getId());

        } catch (InstanceAlreadyExistsException ex) {
            return new Response(false, "Ошибка: " + ex.getMessage(), null);
        } catch (InvalidAmountException invalid) {
            return new Response(false, "Ошибка: " + invalid.getMessage());
        } catch (Exception e) {
            return new Response(false, "Непредвиденная ошибка: " + e.toString(), -1);
        }
    }
}
