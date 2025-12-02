package view.album.customDialogs;

import java.util.Collection;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import model.Tag;

/**
 * A custom dialog for removing a {@link Tag} from a photo.
 * <p>
 * This dialog displays two ComboBoxes: one for selecting the tag type and another
 * for selecting the tag value corresponding to that type. Only tag values
 * associated with the selected type are shown. 
 * </p>
 * <p>
 * The dialog returns a {@link TagData} object containing the selected type and
 * value when the OK button is pressed. Returns {@code null} if canceled.
 * </p>
 */
public class RemoveTagDialog extends Dialog<RemoveTagDialog.TagData> {

    /** The grid layout for organizing labels and ComboBoxes. */
    private final GridPane grid;

    /** Label for the tag type ComboBox. */
    private final Label typeLabel;

    /** Label for the tag value ComboBox. */
    private final Label valueLabel;

    /** ComboBox for selecting a tag type. */
    private final ComboBox<String> typeBox;

    /** ComboBox for selecting a tag value corresponding to the selected type. */
    private final ComboBox<String> valueBox;

    /**
     * Constructs a RemoveTagDialog with a given collection of {@link Tag}s.
     *
     * @param tags Collection of tags available for removal.
     */
    public RemoveTagDialog(Collection<Tag> tags) {
        super();
        DialogPane pane = this.getDialogPane();

        // Initialize type ComboBox
        this.typeBox = new ComboBox<>();
        this.typeBox.setMinWidth(150.0);
        if (pane != null) {
            for (Tag t: tags)
                if (!this.typeBox.getItems().contains(t.getType()))
                    this.typeBox.getItems().add(t.getType());
        }
        this.typeBox.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(this.typeBox, Priority.ALWAYS);
        GridPane.setFillWidth(this.typeBox, true);

        // Initialize value ComboBox
        this.valueBox = new ComboBox<>();
        this.valueBox.setMinWidth(150.0);
        this.valueBox.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(this.valueBox, Priority.ALWAYS);
        GridPane.setFillWidth(this.valueBox, true);

        // Update valueBox items based on type selection
        this.typeBox.getSelectionModel().selectedItemProperty().addListener((obs, oldType, newType) -> {
            this.valueBox.getItems().clear();
            for (Tag t: tags)
                if (t.getType().equals(newType))
                    this.valueBox.getItems().add(t.getValue());
            this.valueBox.getSelectionModel().clearSelection();
        });

        // Initialize labels
        this.typeLabel = createContentLabel("Tag type:");
        this.typeLabel.setPrefWidth(-1.0);

        this.valueLabel = createContentLabel("Tag value:");
        this.valueLabel.setPrefWidth(-1.0);

        // Initialize grid
        this.grid = new GridPane();
        this.grid.setHgap(10.0);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        pane.contentTextProperty().addListener((var1x) -> this.updateGrid());

        // Configure dialog
        this.setHeaderText("Remove Tag");
        pane.getStyleClass().add("text-input-dialog");
        pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.updateGrid();

        // Convert result to TagData on OK
        this.setResultConverter((buttonType) -> {
            ButtonData buttonData = buttonType == null ? null : buttonType.getButtonData();
            return buttonData == ButtonData.OK_DONE 
                ? new TagData(typeBox.getSelectionModel().getSelectedItem(), valueBox.getSelectionModel().getSelectedItem())
                : null;
        });
    }

    /** Updates the grid layout with labels and ComboBoxes. */
    private void updateGrid() {
        this.grid.getChildren().clear();
        this.grid.add(this.typeLabel, 0, 0);
        this.grid.add(this.typeBox, 1, 0);
        this.grid.add(this.valueLabel, 0, 1);
        this.grid.add(this.valueBox, 1, 1);
        this.getDialogPane().setContent(this.grid);
    }

    /** Creates a simple label for dialog content. */
    private Label createContentLabel(String label) {
        return new Label(label);
    }

    /**
     * A simple data class representing the selected tag type and value.
     */
    public class TagData {
        private final String type;
        private final String value;

        /**
         * Constructs a TagData instance.
         *
         * @param type  The tag type.
         * @param value The tag value.
         */
        public TagData(String type, String value) {
            this.type = type;
            this.value = value;
        }

        /** Returns the tag type. */
        public String getType() { return type; }

        /** Returns the tag value. */
        public String getValue() { return value; }
    }
}
