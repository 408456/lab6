package ru.itmo.server.comands;

import ru.itmo.general.data.User;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;
import ru.itmo.server.comands.Command;
import ru.itmo.server.dao.UserDAO;

/**
 * Команда 'login'. Авторизует пользователя в системе.
 */
public class Login extends Command {
    private UserDAO userDAO;

    public Login() {
        super("login", "{username} вход в систему");
    }

    public Login(UserDAO userDAO) {
        this();
        this.userDAO = userDAO;
    }

    @Override
    public Response execute(Request request) {
        try {
            String username = request.getLogin();
            String password = request.getPassword();

            if (!userDAO.verifyUserPassword(username, password)) {
                return new Response(false, "Неверное имя пользователя или пароль", null);
            }

            User user = userDAO.getUserByUsername(username);

            if (user == null) {
                return new Response(false, "Пользователь не найден", null);
            }
            if (user.getId() == null) {
                return new Response(false, "ID пользователя пуст", null);
            }

            // Успешный вход
            return new Response(true, "Вы успешно вошли в систему\nВаш id:", user.getId());
        } catch (Exception e) {
            System.out.println("Ошибка во время входа: " + e);
            return new Response(false, e.toString(), null);
        }
    }
}
