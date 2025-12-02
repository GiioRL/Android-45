package controller.album;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import model.*;
import view.album.AlbumViewController;

/**
 * Singleton controller for managing album-related operations such as creating photos,
 * generating thumbnails, and interacting with AlbumViewController.
 * Implements Serializable to allow saving and restoring state if necessary.
 */
public class AlbumController implements Serializable {

    /** Singleton instance of AlbumController */
    static AlbumController instance;

    /** Reference to the associated AlbumViewController */
    private transient AlbumViewController avc;

    /** Private constructor for singleton pattern */
    private AlbumController() {}

    /**
     * Returns the singleton instance of AlbumController.
     * If the instance does not exist, it is created.
     *
     * @return the singleton AlbumController instance
     */
    public static AlbumController getInstance() {
        if (instance == null) {
            instance = new AlbumController();
        }
        return instance;
    }

    /**
     * Creates a Photo object with the specified location, caption, tags, and timestamp.
     *
     * @param location the file path of the photo
     * @param caption  the caption of the photo
     * @param tags     the list of tags associated with the photo
     * @param millis   the timestamp of the photo in milliseconds
     * @return the created Photo object
     */
    public Photo createPhoto(String location, String caption, ArrayList<Tag> tags, long millis) {
        return new Photo(location, caption, tags, millis);
    }

    /**
     * Creates a Photo object with the specified location, caption, and tags.
     * Uses the file's last modified timestamp.
     *
     * @param location the file path of the photo
     * @param caption  the caption of the photo
     * @param tags     the list of tags associated with the photo
     * @return the created Photo object
     */
    public Photo createPhoto(String location, String caption, ArrayList<Tag> tags) {
        File file = new File(location);
        return createPhoto(location, caption, tags, file.lastModified());
    }

    /**
     * Creates a Photo object with the specified location and caption.
     * No tags are assigned.
     *
     * @param location the file path of the photo
     * @param caption  the caption of the photo
     * @return the created Photo object
     */
    public Photo createPhoto(String location, String caption) {
        return createPhoto(location, caption, null);
    }

    /**
     * Creates a Photo object with only the specified location.
     * Caption is set to an empty string, and no tags are assigned.
     *
     * @param location the file path of the photo
     * @return the created Photo object
     */
    public Photo createPhoto(String location) {
        return createPhoto(location, "");
    }

    /**
     * Converts a Calendar date object into a formatted string (MM/DD/YYYY).
     *
     * @param date the Calendar object representing the date
     * @return the formatted date string
     */
    public String convertDatetoString(Calendar date) {
        return ("" + (date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR));
    }

    /**
     * Generates a list of JavaFX Nodes representing thumbnails for all photos in the album.
     *
     * @param album the Album object containing photos
     * @return a list of Node objects representing thumbnails
     */
    public ArrayList<Node> getThumbnails(Album album) {
        ArrayList<Node> thumbnails = new ArrayList<Node>();
        ArrayList<Photo> photos = album.getPhotos();
        if (photos != null) {
            for (Photo photo: photos) {
                Node thumbnail = photo.getThumbnail();
                thumbnails.add(thumbnail);
            }
        }
        return thumbnails;
    }

    /**
     * Handles the "back" action for an album, usually returning to the parent view.
     *
     * @param album the Album object to navigate back from
     */
    public void back(Album album) {
        album.back();
    }

    /**
     * Injects the AlbumViewController into this controller for UI interaction.
     *
     * @param avc the AlbumViewController to be injected
     */
    public void injectAlbumController(AlbumViewController avc) {
        this.avc = avc;
    }

    /**
     * Loads a PhotoBox UI component from FXML and returns it as a Node.
     * Also injects the PhotoBox controller into the AlbumViewController.
     *
     * @return the Node representing the loaded PhotoBox
     */
    public Node getPhotoBox() {
        Node photoBox = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album/photoBox/PhotoBox.fxml"));
            photoBox = loader.load();
            if (photoBox == null) {
                System.out.println("why null???");
            }
            avc.injectPB(loader.getController());
        } catch (Exception e) {
            System.out.println("o no bad thumbnail in AM");
            e.printStackTrace();
        }
        return photoBox;
    }
}
