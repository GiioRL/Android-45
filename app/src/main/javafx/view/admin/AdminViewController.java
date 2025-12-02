package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.App;
import model.User;
import view.album.customDialogs.UserCredentialsDialog;

import controller.AdminController;

/**
 * Controller class for the Admin view in the application.
 * Handles UI actions related to adding, deleting, and listing users,
 * as well as logging out or quitting the application.
 */
public class AdminViewController {

    @FXML
    private Button buttonAddUser;

    @FXML
    private Button buttonDeleteUser;

    @FXML
    private Button buttonListUsers;

    @FXML
    private Text display;

    private static AdminController ac = AdminController.getInstance();

    /**
     * Displays a list of all registered users in the admin display text area.
     */
    @FXML
    void listUsers() {
        String list = "Users:\n";
        for (User user: User.getUsers())
            list += user.getUsername() + '\n';
        display.setText(list);
    }

    /**
     * Opens a dialog to add a new user. 
     * If valid credentials are provided, the user is added via AdminController.
     */
    @FXML
    void addUser() {
        display.setText("");
        new UserCredentialsDialog().showAndWait().ifPresent(credData -> {
            String username = credData.getUsername(), password = credData.getPassword();
            ac.addUser(username, password);
        });
    }

    /**
     * Opens a dialog to input a username to delete. 
     * If the username is valid, the user is deleted via AdminController.
     */
    @FXML
    void deleteUser() {
        display.setText("");
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setContentText("Username:");
        usernameDialog.setHeaderText("Delete User");
        usernameDialog.showAndWait().ifPresent(username -> {
            ac.deleteUser(username);
        });
    }

    /**
     * Logs out the current admin user by switching to the login scene.
     */
    @FXML
    void logout() {
        Stage primaryStage = App.getStage();
        primaryStage.setScene(App.getLoginScene());
    }

    /**
     * Exits the application gracefully.
     */
    @FXML
    void quit() {
        App.quit();
    }
}
