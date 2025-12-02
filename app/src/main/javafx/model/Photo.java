package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import view.album.AlbumViewController;
import view.album.photoThumbnail.PhotoThumbnailViewController;

/**
 * Represents a photo stored in an album.
 * <p>
 * A {@code Photo} object maintains information about its file location,
 * caption, tags, and date. Because this is a serializable model class that
 * also interacts with JavaFX, certain UI fields (e.g., thumbnail, controllers)
 * are declared {@code transient} and recreated during deserialization.
 * </p>
 */
public class Photo implements Serializable {

    /** The file path of the image. */
    private String location;

    /** The caption associated with the photo. */
    private String caption;

    /** The list of tags assigned to this photo. */
    private ArrayList<Tag> tags;

    /** The date the photo was taken or added. */
    private Calendar date;

    /** The Image object representing this photo (not serialized). */
    private transient Image image;

    /** Thumbnail node used in the album view (not serialized). */
    private transient Node thumbnail;

    /** Controller for the photo's thumbnail (not serialized). */
    private transient PhotoThumbnailViewController tc;

    /**
     * Constructs a new photo.
     *
     * @param location the file path to the photo
     * @param caption  the caption for the photo
     * @param tags     the list of tags assigned to the photo
     * @param millis   the date represented in milliseconds
     */
    public Photo(String location, String caption, ArrayList<Tag> tags, long millis) {
        this.location = location;
        this.caption = caption;
        this.tags = tags;

        date = Calendar.getInstance();
        date.setTimeInMillis(millis);
        date.set(Calendar.MILLISECOND, 0);

        image = createImage();
        createThumbnail();
    }

    /**
     * Loads the image from disk and returns a JavaFX {@link Image} object.
     * Displays an error dialog if the file cannot be loaded.
     *
     * @return the loaded image, or a placeholder image if loading fails
     */
    private Image createImage() {
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(location));
        } catch (IOException e) {
            Alert error = new Alert(
                Alert.AlertType.ERROR,
                "Photo not found. Please enter a valid photo file path."
            );
            error.setHeaderText("Photo Not Found");
            error.showAndWait();
        }
        return new Image(stream);
    }

    /**
     * Loads and constructs the photo thumbnail UI component.
     * The associated controller is also initialized.
     */
    public void createThumbnail() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/album/photoThumbnail/PhotoThumbnailView.fxml")
            );
            thumbnail = loader.load();
            tc = loader.getController();
            tc.injectPhoto(this);
        } catch (Exception e) {
            System.out.println("o no bad thumbnail in photo");
            e.printStackTrace();
        }
    }

    /**
     * Returns the date associated with this photo.
     *
     * @return the photo date as a {@link Calendar}
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Returns the date as a formatted string (MM/DD/YYYY).
     *
     * @return the formatted date string
     */
    public String getDateString() {
        return ("" + (date.get(Calendar.MONTH) + 1)
                + "/" + date.get(Calendar.DAY_OF_MONTH)
                + "/" + date.get(Calendar.YEAR));
    }

    /**
     * Returns the list of tags assigned to the photo.
     *
     * @return the list of {@link Tag} objects
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Replaces the existing tag list with a new set of tags.
     *
     * @param newTags the updated tag list
     */
    public void setTags(ArrayList<Tag> newTags) {
        tags = newTags;
    }

    /**
     * Returns the file path of the photo.
     *
     * @return the photo's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the caption associated with the photo.
     *
     * @return the caption string
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Updates the caption for this photo.
     * <p>
     * After updating, the thumbnail is recreated so the UI reflects the change.
     * </p>
     *
     * @param newCaption the updated caption
     */
    public void setCaption(String newCaption) {
        this.caption = newCaption;

        AlbumViewController temp = tc.getAC();
        createThumbnail();
        tc.injectAlbumController(temp);
    }

    /**
     * Returns the JavaFX image associated with this photo.
     *
     * @return the image object
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns the UI thumbnail node for this photo.
     *
     * @return the thumbnail node
     */
    public Node getThumbnail() {
        return thumbnail;
    }

    /**
     * Returns the controller responsible for the photo's thumbnail UI.
     *
     * @return the thumbnail controller
     */
    public PhotoThumbnailViewController getPhotoThumbnailController() {
        return tc;
    }

    /**
     * Highlights the thumbnail to indicate selection.
     */
    public void select() {
        tc.select();
    }

    /**
     * Removes highlight from the thumbnail.
     */
    public void deselect() {
        tc.deselect();
    }

    /**
     * Determines whether two photos are equal based on their file location.
     * <p>
     * This prevents duplicate photos from being added to the same album.
     * </p>
     *
     * @param o the object to compare
     * @return {@code true} if both photos share the same location
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Photo))
            return false;
        return location.equals(((Photo) o).location);
    }

    /**
     * Custom deserialization logic.
     * <p>
     * After the photo's base data is deserialized, transient UI fields such
     * as {@code image} and the thumbnail must be recreated manually.
     * </p>
     *
     * @param in the input stream
     * @throws IOException if reading fails
     * @throws ClassNotFoundException if class lookup fails
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        in.defaultReadObject();
        image = createImage();
        createThumbnail();
    }
}
