package ru.itmo.server.comands;

import ru.itmo.general.data.User;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;
import ru.itmo.server.dao.UserDAO;

/**
 * Command 'login'. Logs in a user to the system.
 *
 */
public class Login extends Command {
    private UserDAO userDAO;

    public Login() {
        super("login", "{username} log in to the system");
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
                return new Response(false, "Invalid username or password", null);
            }

            User user = userDAO.getUserByUsername(username);

            if (user == null) {
                return new Response(false, "User not found", null);
            }

            if (user.getId() == null) {
                return new Response(false, "User ID is null", null);
            }

            return new Response(true, "You have successfully logged in\nYour id:", user.getId());
        } catch (Exception e) {
            System.out.println("Exception during login: " + e); // Debug message
            return new Response(false, e.toString(), null);
        }
    }
}
