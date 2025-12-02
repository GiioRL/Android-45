package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.App;
import view.library.LibraryViewController;

/**
 * Represents an application user who owns albums and maintains a tag library.
 * <p>
 * A {@code User} stores a username, password, a list of {@link Album}s,
 * and a personal list of {@link Tag} types and values. Users can log in,
 * view their album library, and serialize their data to disk.
 * </p>
 *
 * <p>
 * This class is {@link Serializable}; user data is saved in individual
 * {@code .dat} files inside the application's data folder. A static list of
 * all users is maintained in-memory and restored on application startup.
 * </p>
 */
public class User implements Serializable {

    /** The user's login username. */
    protected String username = "";

    /** The user's login password. */
    protected String password = "";

    /** All photo albums belonging to the user. */
    protected ArrayList<Album> albums = new ArrayList<>();

    /** Static registry of all users in the system. */
    private static ArrayList<User> users = new ArrayList<>();

    /**
     * User-maintained list of known tag types and values.
     * <p>
     * For each tag type, a Tag of form {@code (type, null)} should be stored
     * so that users can later add new tag values.
     * </p>
     */
    protected ArrayList<Tag> tags = new ArrayList<>();

    /** Controller for the user's Library view (transient). */
    protected transient LibraryViewController lc;

    /** The primary JavaFX stage (transient). */
    private transient Stage primaryStage;

    /** The scene associated with this user's library view (transient). */
    private transient Scene scene;

    /** Default constructor (used primarily during deserialization). */
    public User() {}

    /**
     * Constructs a new user and registers them in the global user list.
     *
     * @param username the desired username
     * @param password the desired password
     * @throws Exception if a user with the same username already exists
     */
    public User(String username, String password) throws Exception {
        if (User.exists(username)) {
            throw new Exception("User already exists with that username.");
        }
        this.username = username;
        this.password = password;

        users.add(this);

        // Default tag categories for all users
        tags.add(new Tag("Location", null, false));
        tags.add(new Tag("Person", null));
    }

    /**
     * Adds a new tag definition to the user's tag list.
     * <p>
     * Behavior:
     * <ul>
     *   <li>If the tag's type exists, the method checks whether its value already exists.</li>
     *   <li>A {@code (type, null)} entry is always ensured to exist.</li>
     *   <li>If the new value is unique, the full tag is added.</li>
     * </ul>
     *
     * @param newTag the tag to add
     */
    public void addTag(Tag newTag) {
        if (tags.contains(newTag)) { // type already exists
            boolean foundValue = false;
            for (Tag tag : tags) {
                foundValue |= tag.tagEquals(newTag);
            }
            if (foundValue) {
                tags.add(newTag);
            }
        }
        tags.add(new Tag(newTag.getType(), null));
        if (newTag.getValue() != null) {
            tags.add(newTag);
        }
    }

    /**
     * Returns the list of all tag definitions for this user.
     *
     * @return the user's tag list
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /** @return the username */
    public String getUsername() {
        return username;
    }

    /** @return the password */
    public String getPassword() {
        return password;
    }

    /** Sets the username. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Sets the password. */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Users are equal if both their usernames and passwords match.
     *
     * @param other the object to compare
     * @return {@code true} if both users match by credentials
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof User)) {
            return false;
        }
        User u = (User) other;
        return username.equals(u.getUsername()) && password.equals(u.getPassword());
    }

    /**
     * @return the static list of all users
     */
    public static ArrayList<User> getUsers() {
        return users;
    }

    /** @return {@code true} if a user with the given username exists */
    public static boolean exists(String username) {
        return getUser(username) != null;
    }

    /** @return {@code true} if credentials match an existing user */
    public static boolean exists(String username, String password) {
        return getUser(username, password) != null;
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username to search for
     * @return the user or {@code null} if none found
     */
    public static User getUser(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves a user by credentials.
     *
     * @param username the username
     * @param password the password
     * @return the matching user or {@code null} if none found
     */
    public static User getUser(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Adds an album to the user's album collection.
     *
     * @param album the album to add
     */
    public void addAlbum(Album album) {
        albums.add(album);
    }

    /**
     * Returns all albums belonging to the user.
     *
     * @return the album list
     */
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    /**
     * @return this user's LibraryViewController
     */
    public LibraryViewController getLC() {
        return lc;
    }

    /**
     * Loads this user's library scene and displays it.
     */
    public void start() {
        if (scene == null) {
            initScene();
        }
        lc.start();
        primaryStage.setScene(scene);
    }

    /**
     * Initializes the user's library scene by loading the LibraryView FXML.
     */
    private void initScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/library/LibraryView.fxml"));
            Parent root = loader.load();
            lc = loader.getController();
            lc.injectUser(this);
            scene = new Scene(root);
        } catch (Exception e) {
            System.out.println("user oops");
            e.printStackTrace();
        }
        primaryStage = App.getStage();
    }

    /**
     * Initializes the static user list with default Admin and Stock accounts.
     */
    public static void initClass() {
        users.add(new Admin());
        users.add(new Stock());
    }

    /**
     * Clears all users, deletes all saved user files, and restores defaults.
     */
    public static void resetClass() {
        users.clear();
        File saveFolder = new File(System.getProperty("user.dir")
                + File.separator + "src" + File.separator + "data" + File.separator);
        File[] files = saveFolder.listFiles();

        for (File file : files) {
            if (!file.delete()) {
                System.out.println("Failed to delete file: " + file.getAbsolutePath());
            }
        }

        initClass();
    }

    /**
     * Deletes stored Admin and Stock data files and recreates them.
     */
    public static void restoreClass() {
        File saveFolder = new File(System.getProperty("user.dir")
                + File.separator + "src" + File.separator + "data" + File.separator);
        File adminFile = new File(saveFolder + File.separator + "admin.dat");
        File stockFile = new File(saveFolder + File.separator + "stock.dat");

        if (adminFile.delete()) {
            users.remove(new Admin());
        } else {
            System.out.println("Failed to delete admin file");
        }

        if (stockFile.delete()) {
            users.remove(new Stock());
        } else {
            System.out.println("Failed to delete stock file");
        }

        initClass();
    }

    /**
     * Saves this user to a {@code .dat} file in the given folder.
     *
     * @param folder the folder where the user file will be written
     * @throws Exception if an I/O error occurs
     */
    public void save(String folder) throws Exception {
        File file = new File(folder + File.separator + username + ".dat");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }

    /**
     * Loads a serialized user from the given file and registers them.
     *
     * @param file the serialized user file
     * @throws Exception if deserialization fails
     */
    public static void load(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        User user = (User) ois.readObject();
        users.add(user);
        ois.close();
    }

    /** @return a readable representation of the user */
    @Override
    public String toString() {
        return "User " + username;
    }
}
