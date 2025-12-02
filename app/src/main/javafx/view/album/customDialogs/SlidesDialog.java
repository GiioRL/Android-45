package view.album.customDialogs;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import model.Album;
import model.Photo;

/**
 * A dialog that displays photos in an {@link Album} as a slideshow.
 * <p>
 * Users can navigate between photos using the left and right buttons.
 * The dialog header shows the photo's caption (or "(Uncaptioned)") and its date.
 * </p>
 */
public class SlidesDialog extends Dialog<Object> {

    /** ImageView displaying the current photo. */
    @FXML
    private ImageView imageView;

    /** Button to navigate to the previous photo. */
    @FXML
    private Button leftButton;

    /** Button to navigate to the next photo. */
    @FXML
    private Button rightButton;

    /** The album containing the photos. */
    private Album album;

    /** The currently displayed photo. */
    private Photo curPhoto;

    /** Index of the currently displayed photo in the album. */
    private int curIndex;

    /**
     * Creates a SlidesDialog for an album.
     *
     * @param album The album containing photos to display.
     * @param curSelected The photo to display first. If null, the first photo in the album is used.
     */
    public SlidesDialog(Album album, Photo curSelected) {
        super();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/album/customDialogs/SlidesDialog.fxml"));
            loader.setController(this);
            DialogPane pane = loader.load();

            this.album = album;

            if (curSelected != null) {
                this.curPhoto = curSelected;
                this.curIndex = album.getPhotos().indexOf(curPhoto);
            } else if (!album.getPhotos().isEmpty()) {
                this.curPhoto = album.getPhotos().get(0);
                this.curIndex = 0;
            } else {
                curSelected = null;
                this.curIndex = -1;
            }

            pane.getButtonTypes().add(ButtonType.OK);
            this.setDialogPane(pane);
            setupImage();

            if (this.curIndex == 0)
                this.leftButton.setDisable(true);
            this.leftButton.setOnAction(arg0 -> {
                if (rightButton.isDisable())
                    rightButton.setDisable(false);
                this.curIndex--;
                this.curPhoto = album.getPhotos().get(curIndex);
                if (this.curIndex == 0)
                    this.leftButton.setDisable(true);
                setupImage();
            });

            if (this.curIndex == this.album.getPhotos().size() - 1)
                this.rightButton.setDisable(true);
            this.rightButton.setOnAction(arg0 -> {
                if (leftButton.isDisable())
                    leftButton.setDisable(false);
                this.curIndex++;
                this.curPhoto = album.getPhotos().get(curIndex);
                if (this.curIndex == album.getPhotos().size() - 1)
                    this.rightButton.setDisable(true);
                setupImage();
            });

            this.setResultConverter((buttonType) -> null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the image view and header to reflect the currently selected photo.
     */
    private void setupImage() {
        if (curPhoto == null)
            return;

        this.imageView.setImage(curPhoto.getImage());
        String header = curPhoto.getCaption().length() > 0 ? curPhoto.getCaption() : "(Uncaptioned)";
        header += " - " + curPhoto.getDateString();
        this.setHeaderText(header);

        DialogPane pane = this.getDialogPane();
        pane.getScene().getWindow().sizeToScene();
    }
}
