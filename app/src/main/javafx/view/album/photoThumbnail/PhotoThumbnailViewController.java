package view.album.photoThumbnail;

import controller.album.PhotoThumbnailController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Photo;
import view.album.AlbumViewController;

public class PhotoThumbnailViewController {

    @FXML
    private Label captionLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane container;

    private static PhotoThumbnailController thumbnailModel = PhotoThumbnailController.getInstance();
    private Photo photo;
    private AlbumViewController avc;

    public void injectPhoto(Photo photo) {
        this.photo = photo;
        initScene();
    }

    public void injectAlbumController(AlbumViewController avc) {
        this.avc = avc;
    }

    private void initScene() {
        imageView.setImage(thumbnailModel.getImage(photo));
        captionLabel.setText(thumbnailModel.getCaption(photo));
    }

    @FXML
    void onClick() {
        avc.select(photo);        
    }

    public void select() {
        container.setStyle("-fx-border-color: TEAL");
    }

    public void deselect() {
        container.setStyle("-fx-border-color: TRANSPARENT");
    }

    public AlbumViewController getAC() {
        return avc;
    }
}
