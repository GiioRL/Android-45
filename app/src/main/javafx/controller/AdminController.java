package controller;

import javafx.scene.control.Alert;
import model.User;
import view.album.customDialogs.UserCredentialsDialog;

/**
 * Controller class for managing administrative actions on users.
 * <p>
 * This class is implemented as a singleton to provide a single centralized
 * point of control for administrative functions such as adding or deleting users.
 * </p>
 * <p>
 * Provides visual feedback using JavaFX {@link Alert} dialogs when operations
 * succeed or fail.
 * </p>
 */
public class AdminController {

    /** Singleton instance of AdminController. */
    private static AdminController instance;

    /** Private constructor to enforce singleton pattern. */
    private AdminController() {}

    /**
     * Returns the singleton instance of this controller.
     * Creates a new instance if one does not already exist.
     *
     * @return the singleton AdminController instance
     */
    public static AdminController getInstance() {
        if (instance == null) {
            instance = new AdminController();
        }
        return instance;
    }

    /**
     * Adds a new user to the system with the given credentials.
     * <p>
     * Displays an alert in case of invalid input (empty username or password)
     * or if the user cannot be created (e.g., username already exists).
     * </p>
     *
     * @param username the desired username
     * @param password the desired password
     */
    public void addUser(String username, String password) {
        if (username.length() == 0) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Enter a username to proceed.");
            warning.setHeaderText("Invalid Username");
            warning.showAndWait();
            return;
        } else if (password.length() == 0) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Enter a password to proceed.");
            warning.setHeaderText("Invalid Password");
            warning.showAndWait();
            return;
        } else {
            try {
                new User(username, password);
                Alert info = new Alert(Alert.AlertType.INFORMATION, "User added successfully!");
                info.setHeaderText("User Added");
                info.showAndWait();
            } catch (Exception e) {
                Alert warning = new Alert(Alert.AlertType.WARNING, e.getMessage());
                warning.setHeaderText("Unable To Create User");
                warning.showAndWait();
            }
        }
    }

    /**
     * Deletes a user from the system by username.
     * <p>
     * Cannot delete the default users "admin" or "stock".
     * Displays alerts for invalid input, non-existent users, or successful deletion.
     * </p>
     *
     * @param username the username of the user to delete
     */
    public void deleteUser(String username) {
        if (username.length() == 0) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Enter a username to proceed.");
            warning.setHeaderText("Invalid Username");
            warning.showAndWait();
            return;
        } else if (username.equals("admin") || username.equals("stock")) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Cannot remove " + username + " user.");
            warning.setHeaderText("Cannot Remove User");
            warning.showAndWait();
            return;
        } else {
            User oldUser = User.getUser(username);
            if (oldUser == null) {
                Alert warning = new Alert(Alert.AlertType.WARNING, "User with that username does not exist.");
                warning.setHeaderText("User Does Not Exist");
                warning.showAndWait();
            } else {
                User.getUsers().remove(oldUser);
                Alert info = new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!");
                info.setHeaderText("User Deleted");
                info.showAndWait();
            }
        }
    }
}
