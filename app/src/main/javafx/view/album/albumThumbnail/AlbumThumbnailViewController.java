package view.album.albumThumbnail;

import controller.album.AlbumThumbnailController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Album;
import view.library.LibraryViewController;

public class AlbumThumbnailViewController {

    @FXML
    private ImageView imageView;

    @FXML
    private Text dateRangeText;

    @FXML
    private Text photoNumText;

    @FXML
    private Text titleText;

    @FXML
    private AnchorPane backgroundPane;

    private Album album;
    private static AlbumThumbnailController tc = AlbumThumbnailController.getInstance();
    private LibraryViewController lvc;

    public void injectAlbum(Album album) {
        this.album = album;
        lvc = album.getUser().getLC();
        initScene();
    }

    private void initScene() {
        imageView.setImage(tc.getImage(album));
        titleText.setText(tc.getName(album));
        dateRangeText.setText(tc.getDateRange(album));
        photoNumText.setText(tc.getNumPhotos(album));
    }

    @FXML
    void onClick(MouseEvent e) { // maybe this should be on the whole anchorpane and not just the image
        lvc.select(album);
    }

    public void select() {
        backgroundPane.setStyle("-fx-border-color: TEAL");
    }

    public void deselect() {
        backgroundPane.setStyle("-fx-border-color: TRANSPARENT");
    }
}
