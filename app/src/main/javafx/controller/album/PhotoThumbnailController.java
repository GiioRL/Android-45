package controller.album;

import javafx.scene.image.Image;
import model.Photo;

/**
 * Singleton controller for handling photo thumbnail information, such as
 * the image and caption of a photo.
 */
public class PhotoThumbnailController {

    /** Singleton instance of PhotoThumbnailController */
    private static PhotoThumbnailController instance;

    /** Private constructor for singleton pattern */
    private PhotoThumbnailController() {}

    /**
     * Returns the singleton instance of PhotoThumbnailController.
     * Creates it if it does not already exist.
     *
     * @return the singleton PhotoThumbnailController instance
     */
    public static PhotoThumbnailController getInstance() {
        if (instance == null) {
            instance = new PhotoThumbnailController();
        }
        return instance;
    }

    /**
     * Returns the Image object of a given photo.
     *
     * @param photo the Photo object
     * @return the Image of the photo
     */
    public Image getImage(Photo photo) {
        return photo.getImage();
    }

    /**
     * Returns the caption of a given photo.
     *
     * @param photo the Photo object
     * @return the caption of the photo as a String
     */
    public String getCaption(Photo photo) {
        return photo.getCaption();
    }
}