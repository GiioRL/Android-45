package model;

import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.App;

/**
 * Represents the administrative user of the application.
 * <p>
 * The {@code Admin} class extends {@link User} and provides additional
 * functionality for managing users, including listing all users, adding
 * new users, and deleting existing users. Admin also handles switching
 * to the administrative view when logged in.
 * </p>
 */
public class Admin extends User {

    /** The primary application stage (not serialized). */
    private transient Stage primaryStage;

    /**
     * Creates the default admin user with the username and password
     * both set to {@code "admin"}.
     */
    public Admin() {
        username = "admin";
        password = "admin";
    }

    /**
     * Returns a list of all registered users.
     *
     * @return an {@link ArrayList} containing all users
     */
    public ArrayList<User> listUsers() {
        return User.getUsers();
    }

    /**
     * Attempts to add a new user to the system.
     *
     * @param username the username for the new user
     * @param password the password for the new user
     * @return {@code 0} if the user is successfully added,
     *         {@code -1} if the username already exists or another error occurs
     */
    public int addUser(String username, String password) {
        try {
            if (User.getUsers().add(new User(username, password))) {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * Deletes a user from the system based on username.
     *
     * @param username the username of the user to delete
     * @return {@code 0} if the user is successfully removed,
     *         {@code -1} if no matching user exists
     */
    public int deleteUser(String username) {
        ArrayList<User> users = User.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                users.remove(i);
                return 0;
            }
        }
        return -1;
    }

    /**
     * Loads and displays the admin interface.
     * <p>
     * This method switches the application's primary stage to the
     * Admin view defined in {@code AdminView.fxml}.
     * </p>
     */
    public void start() {
        primaryStage = App.getStage();
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("/view/admin/AdminView.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.out.println("err there was an exception");
            e.printStackTrace();
        }
    }
}
