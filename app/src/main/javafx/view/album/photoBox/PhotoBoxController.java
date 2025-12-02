package view.album.photoBox;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

public class PhotoBoxController {

    @FXML
    private HBox box;

    public void init(Node[] photos) {
        for (int i = 0; i < photos.length; i++) {
            if (photos[i] != null && !box.getChildren().contains(photos[i])) {
                box.getChildren().add(photos[i]);
            }
        }
    }
}
