package view.login;

import controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.App;
import javafx.scene.text.Text;
import model.User;

/**
 * Controller class for the login view.
 * Handles user interactions with the login screen, including login attempts, 
 * restoring, resetting user data, and quitting the application.
 */
public class LoginViewController {

    /** Login button in the view. */
    @FXML
    private Button butonLogin;

    /** TextField for entering the user's password. */
    @FXML
    private TextField tfPassword;

    /** TextField for entering the user's username. */
    @FXML
    private TextField tfUsername;

    /** Text node for displaying invalid login messages. */
    @FXML
    private Text textInvalid;

    /** Button to quit the application. */
    @FXML
    private Button quitButton;

    /** Scene representing this view. */
    private Scene scene;

    /** Stage used to display this scene. */
    private Stage stage;

    /** LoginController singleton used to authenticate users. */
    private static LoginController lc = LoginController.getInstance();

    /**
     * Attempts to log in the user with the provided username and password.
     * If login fails, an invalid login message is displayed.
     * If login succeeds, the user is redirected to their main view.
     */
    @FXML
    void login() {
        User user = lc.login(tfUsername.getText(), tfPassword.getText());
        if (user == null) { // invalid login
            textInvalid.setVisible(true);
        } else {
            textInvalid.setVisible(false);
            tfUsername.setText("");
            tfPassword.setText("");
            user.start();
        }
    }

    /**
     * Quits the application.
     */
    @FXML
    void quit() {
        App.quit();
    }

    /**
     * Restores saved user data by deserializing stored information.
     */
    @FXML
    void restore() {
        User.restoreClass();
    }

    /**
     * Resets all user data to its initial state.
     */
    @FXML
    void reset() {
        User.resetClass();
    }

    /**
     * Starts the login view by setting the scene on the primary stage.
     * Initializes the scene if it has not been created yet.
     */
    public void start() {
        if (scene == null) {
            initScene();
        }
        stage.setScene(scene);
    }

    /**
     * Initializes the scene and stage for the login view.
     */
    private void initScene() {
        stage = App.getStage();
        scene = App.getLoginScene(); // may be replaced with a new Scene in future
    }
}
