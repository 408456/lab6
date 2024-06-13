package ru.itmo.server.comands;


import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;
import ru.itmo.general.utility.exceptions.InvalidAmountException;
import ru.itmo.server.dao.UserDAO;

import javax.management.InstanceAlreadyExistsException;

/**
 * Command 'register'. Registers a new user in the system.
 *
 */
public class Register extends Command {
    public static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_USERNAME_LENGTH = 50;
    private UserDAO userDAO;

    public Register() {
        super("register", "{username} register a new user");
    }

    /**
     * Constructor for creating an instance of the Register command.
     *
     * @param userDAO the user manager
     */
    public Register(UserDAO userDAO) {
        this();
        this.userDAO = userDAO;
    }

    /**
     * Executes the command.
     *
     * @param request the request to register a user
     * @return the response indicating the success or failure of the command execution
     */
    @Override
    public Response execute(Request request) {
        try {
            if (request.getLogin().length() >= MAX_USERNAME_LENGTH)
                throw new InvalidAmountException("Username length must be less than " + MAX_USERNAME_LENGTH);

            if (request.getPassword().length() < MIN_PASSWORD_LENGTH)
                throw new InvalidAmountException("Password length must be at least " + MIN_PASSWORD_LENGTH);

            if (request.getUserId() != null) throw new InstanceAlreadyExistsException("User already exists");

            var user = userDAO.insertUser(request.getLogin(), request.getPassword());

            if (user == null) throw new InstanceAlreadyExistsException("User already exists");

            return new Response(true, "User successfully registered", user.getId());
        } catch (InstanceAlreadyExistsException ex) {
            return new Response(false, ex.getMessage(), null);
        } catch (InvalidAmountException invalid) {
            return new Response(false, invalid.getMessage());
        } catch (Exception e) {
            return new Response(false, e.toString(), -1);
        }
    }
}
