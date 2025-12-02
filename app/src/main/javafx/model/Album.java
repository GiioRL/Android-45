package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.App;
import view.album.AlbumViewController;
import view.album.albumThumbnail.AlbumThumbnailViewController;

/**
 * Represents a photo album belonging to a specific {@link User}.
 * <p>
 * An {@code Album} stores a list of {@link Photo} objects as well as metadata
 * such as the album name and the owning user. Although this is primarily a
 * model class, it also maintains several transient JavaFX components used
 * for rendering album views and thumbnails.
 * </p>
 *
 * <p><b>Note:</b> Transient fields (FXML nodes, controllers, and scenes)
 * are recreated when needed, since they cannot be serialized. The underlying
 * album data ({@code name}, {@code photos}, {@code user}) is fully
 * serializable.</p>
 */
public class Album implements Serializable {

    /** The name of the album. */
    private String name;

    /** The user who owns this album. */
    private User user;

    /** The list of photos contained in this album. */
    private ArrayList<Photo> photos;

    /** Thumbnail node displayed in the album list (not serialized). */
    private transient Node thumbnail;

    /** Controller for the album's thumbnail view (not serialized). */
    private transient AlbumThumbnailViewController tc;

    /** Controller for the album's main view (not serialized). */
    private transient AlbumViewController ac;

    /** The scene displaying this album (not serialized). */
    private transient Scene scene;

    /** Reference to the primary stage (not serialized). */
    private transient Stage primaryStage;

    /**
     * Creates an empty album with the specified name and owner.
     *
     * @param user the owning user
     * @param name the album name
     */
    public Album(User user, String name) {
        this.user = user;
        this.name = name;
        this.photos = new ArrayList<>();
    }

    /**
     * Creates an album with a predefined list of photos.
     *
     * @param photos the photos to include
     * @param name   the album name
     * @param user   the album owner
     */
    public Album(ArrayList<Photo> photos, String name, User user) {
        this.photos = photos;
        this.name = name;
        this.user = user;
    }

    /**
     * Loads the album's thumbnail FXML layout and initializes
     * the associated controller.
     */
    public void createThumbnail() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/album/albumThumbnail/albumThumbnailView.fxml")
            );
            thumbnail = loader.load();
            tc = loader.getController();
            tc.injectAlbum(this);
        } catch (Exception e) {
            System.out.println("o no bad thumbnail");
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of photos in this album.
     *
     * @return the list of {@link Photo} objects
     */
    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    /**
     * Returns the graphical thumbnail for this album.
     * If not yet created, it is generated on demand.
     *
     * @return the thumbnail node
     */
    public Node getThumbnail() {
        if (thumbnail == null) {
            createThumbnail();
        }
        return thumbnail;
    }

    /**
     * Returns the album name.
     *
     * @return the name of the album
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the album.
     *
     * @param newName the updated name
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Returns the user who owns this album.
     *
     * @return the owning {@link User}
     */
    public User getUser() {
        return user;
    }

    /**
     * Displays the album's full view scene.
     * <p>
     * If this is the first time the album is opened,
     * its scene and controller are initialized.
     * </p>
     */
    public void start() {
        if (scene == null) {
            initScene();
        }
        ac.injectAlbum(this);

        if (photos != null) {
            for (Photo photo : photos) {
                photo.getPhotoThumbnailController().injectAlbumController(ac);
            }
        }

        primaryStage.setScene(scene);
    }

    /**
     * Displays the album view with a variant initialization
     * based on the provided boolean flag.
     *
     * @param bool parameter passed to the album controller’s setup
     */
    public void start(boolean bool) {
        if (scene == null) {
            initScene(bool);
        }
        ac.injectAlbum(this);

        if (photos != null) {
            for (Photo photo : photos) {
                photo.getPhotoThumbnailController().injectAlbumController(ac);
            }
        }

        primaryStage.setScene(scene);
    }

    /**
     * Selects this album’s thumbnail (visual highlight).
     */
    public void select() {
        tc.select();
    }

    /**
     * Deselects this album’s thumbnail (removes highlight).
     */
    public void deselect() {
        tc.deselect();
    }

    /**
     * Returns to the user’s album list view.
     * <p>
     * The thumbnail is recreated to refresh its graphics.
     * </p>
     */
    public void back() {
        createThumbnail(); // Rebuild thumbnail (may be optimized)
        user.start();
    }

    /**
     * Removes this album from its owner's collection.
     */
    public void delete() {
        user.getAlbums().remove(this);
    }

    /**
     * Initializes the main album viewing scene.
     */
    private void initScene() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/album/AlbumView.fxml")
            );
            Parent root = loader.load();
            ac = loader.getController();
            ac.injectAlbum(this);
            scene = new Scene(root);
        } catch (Exception e) {
            System.out.println("album oops");
            e.printStackTrace();
        }
        primaryStage = App.getStage();
    }

    /**
     * Initializes the album viewing scene with an extra boolean parameter.
     *
     * @param bool parameter passed to the controller setup
     */
    private void initScene(boolean bool) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/album/AlbumView.fxml")
            );
            Parent root = loader.load();
            ac = loader.getController();
            ac.injectAlbum(this, bool);
            scene = new Scene(root);
        } catch (Exception e) {
            System.out.println("album oops");
            e.printStackTrace();
        }
        primaryStage = App.getStage();
    }

    /**
     * Creates a shallow clone of this album.
     * <p>
     * The clone references the same {@code photos} list and user,
     * but is a distinct Album instance.
     * </p>
     *
     * @return a cloned {@code Album} instance
     */
    public Album clone() {
        return new Album(photos, name, user);
    }
}
