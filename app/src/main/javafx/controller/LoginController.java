package controller;

import model.User;

/**
 * Controller responsible for handling user login operations.
 * <p>
 * Implements the singleton pattern to provide a single point of access
 * for login operations throughout the application.
 * </p>
 */
public class LoginController {

    /** Singleton instance of LoginController */
    private static LoginController instance;

    /** Private constructor to enforce singleton pattern */
    private LoginController() {
    }

    /**
     * Returns the singleton instance of this controller.
     * Creates a new instance if one does not already exist.
     *
     * @return the singleton LoginController instance
     */
    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }

    /**
     * Attempts to authenticate a user with the given username and password.
     * <p>
     * This method returns a {@link User} object if the credentials are correct.
     * If the credentials are invalid, it returns {@code null}.
     * </p>
     *
     * @param username the username to authenticate
     * @param password the corresponding password
     * @return a {@link User} object if successful; {@code null} otherwise
     */
    public User login(String username, String password) {
        return User.getUser(username, password);
    }
}
