package main;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import view.login.LoginViewController;

/**
 * Entry point for the Photos Application.
 * <p>
 * This class initializes the JavaFX environment, loads all saved user data,
 * sets up the login screen, and manages global access to shared application
 * components such as the primary stage and login controller.
 * </p>
 */
public class App extends Application {

    /** The primary stage of the application. */
    private static Stage primaryStage;

    /** Loader for the login view FXML file. */
    private static FXMLLoader loginLoader;

    /** Root node for the login view. */
    private static Parent loginRoot;

    /** Scene for the login screen. */
    private static Scene loginScene;

    /** Controller associated with the login view. */
    private static LoginViewController loginController;

    /**
     * Launches the Photos Application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the JavaFX application.
     * <p>
     * Loads persistent data, initializes the login screen, and shows
     * the main application window.
     * </p>
     *
     * @param primaryStage the application primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;

        try {
            loadData();

            loginLoader = new FXMLLoader(getClass().getResource("/view/login/LoginView.fxml"));
            loginRoot = loginLoader.load();
            loginController = loginLoader.getController();
            loginScene = new Scene(loginRoot);
        } catch (Exception e) {
            System.out.println("oopsie daisy");
            e.printStackTrace();
        }

        primaryStage.setTitle("Photos Application");
        primaryStage.setResizable(false);
        loginController.start();
        primaryStage.show();
    }

    /**
     * Returns the primary stage for global access.
     *
     * @return the application's primary stage
     */
    public static Stage getStage() {
        return primaryStage;
    }

    /**
     * Returns the login view controller.
     *
     * @return the login controller instance
     */
    public static LoginViewController getLoginController() {
        return loginController;
    }

    /**
     * Returns the login scene.
     *
     * @return the login scene
     */
    public static Scene getLoginScene() {
        return loginScene;
    }

    /**
     * Safely quits the application.
     * <p>
     * Saves user data before terminating the application.
     * </p>
     */
    public static void quit() {
        saveData();
        System.exit(0);
    }

    /**
     * Initializes default application data when no saved data exists.
     * <p>
     * Currently initializes the {@link User} class.
     * </p>
     */
    private static void initApp() {
        User.initClass();
    }

    /**
     * Saves all user data to the /data directory.
     */
    public static void saveData() {
        ArrayList<User> users = User.getUsers();
        String dataFolder = System.getProperty("user.dir") 
                + File.separator + "src" 
                + File.separator + "data" 
                + File.separator;

        for (User user : users) {
            try {
                user.save(dataFolder);
            } catch (Exception e) {
                System.out.println("saving error :/");
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads all user data from the /data directory.
     * <p>
     * If no data exists, initializes the application
     * with default state.
     * </p>
     */
    public static void loadData() {
        File dataFolder = new File(System.getProperty("user.dir")
                + File.separator + "src"
                + File.separator + "data"
                + File.separator);

        File[] files = dataFolder.listFiles();

        if (files != null) {
            if (files.length == 0) {
                initApp();
            } else {
                for (File file : files) {
                    try {
                        User.load(file);
                    } catch (Exception e) {
                        System.out.println("loading error :/");
                        e.printStackTrace();
                    }
                }
            }
        } else {
            dataFolder.mkdir();
            initApp();
        }
    }
}