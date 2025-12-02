package view.album.customDialogs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Photo;
import model.Tag;

/**
 * A custom dialog for displaying a {@link Photo} and its tags.
 * <p>
 * Shows the image in an {@link ImageView} and lists all associated tags in a
 * formatted {@link Text} area. The dialog header displays the photo's caption
 * (or "(Uncaptioned)" if empty) and its date.
 * </p>
 */
public class ImageDialog extends Dialog<Object> {

    /** The ImageView that displays the photo. */
    @FXML
    private ImageView imageView;

    /** The Text control displaying tags. */
    @FXML
    private Text tagText;

    /**
     * Constructs a new ImageDialog for a given {@link Photo}.
     *
     * @param photo The photo to display.
     */
    public ImageDialog(Photo photo) {
        super();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/album/customDialogs/ImageDialog.fxml"));
            loader.setController(this);
            DialogPane pane = loader.load();

            // Set the photo image
            this.imageView.setImage(photo.getImage());

            // Build tag text
            String text = "Tags:\n";
            if (photo.getTags() != null) {
                HashMap<String, String> tagLines = new HashMap<>();
                for (Tag t: photo.getTags()) {
                    if (!tagLines.containsKey(t.getType()))
                        tagLines.put(t.getType(), t.getValue());
                    else
                        tagLines.put(t.getType(), tagLines.get(t.getType()) + ", " + t.getValue()); 
                }
                for (Map.Entry<String, String> line: tagLines.entrySet())
                    text += line.getKey() + ": " + line.getValue() + "\n";
            }
            this.tagText.setText(text);

            // Add OK button
            pane.getButtonTypes().add(ButtonType.OK);
            this.setDialogPane(pane);

            // Set header text with caption and date
            String header = photo.getCaption().length() > 0 ? photo.getCaption() : "(Uncaptioned)";
            header += " - " + photo.getDateString();
            this.setHeaderText(header);

            // No specific result needed
            this.setResultConverter((buttonType) -> null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
