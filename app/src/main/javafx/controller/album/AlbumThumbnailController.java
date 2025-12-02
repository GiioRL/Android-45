package controller.album;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.image.Image;
import model.Album;
import model.Photo;

/**
 * Singleton controller for handling album thumbnail information, such as
 * the album's representative image, name, date range, and number of photos.
 */
public class AlbumThumbnailController {

    /** Singleton instance of AlbumThumbnailController */
    private static AlbumThumbnailController instance;

    /** Reference to the AlbumController for utility functions */
    private static AlbumController ac = AlbumController.getInstance();
    
    /** Private constructor for singleton pattern */
    private AlbumThumbnailController() {}

    /**
     * Returns the singleton instance of AlbumThumbnailController.
     * Creates it if it does not already exist.
     *
     * @return the singleton AlbumThumbnailController instance
     */
    public static AlbumThumbnailController getInstance() {
        if (instance == null) {
            instance = new AlbumThumbnailController();
        }
        return instance;
    }

    /**
     * Returns the representative image of an album.  
     * If the album has no photos, returns a default "image not found" placeholder.
     *
     * @param album the Album object
     * @return an Image representing the album thumbnail
     */
    public Image getImage(Album album) {
        if (album.getPhotos().size() == 0) {
            return new Image("/resources/imageNotFound.png");
        }
        return album.getPhotos().get(0).getImage();
    }

    /**
     * Returns the name of the album.
     *
     * @param album the Album object
     * @return the album name as a String
     */
    public String getName(Album album) {
        return album.getName();
    }

    /**
     * Returns the date range of the photos in the album, formatted as "MM/DD/YYYY - MM/DD/YYYY".
     * If the album has no photos, returns an empty string.
     *
     * @param album the Album object
     * @return a String representing the date range of the album's photos
     */
    public String getDateRange(Album album) {
        if (album.getPhotos().size() == 0) {
            return "";
        }
        ArrayList<Calendar> dates = new ArrayList<Calendar>();
        for (Photo photo: album.getPhotos()) {
            dates.add(photo.getDate());
        }
        dates.sort(null);
        
        return ac.convertDatetoString(dates.get(0)) + " - " + ac.convertDatetoString(dates.get(dates.size()-1));
    }

    /**
     * Returns a human-readable string representing the number of photos in the album.
     * For example: "1 photo" or "5 photos".
     *
     * @param album the Album object
     * @return a String representing the number of photos in the album
     */
    public String getNumPhotos(Album album) {
        int num = album.getPhotos().size();
        if (num == 1) {
            return "1 photo";
        }
        return "" + num + " photos";
    }
}
