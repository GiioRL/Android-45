package view.album.customDialogs;

import java.util.Collection;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import model.Tag;

/**
 * Custom dialog for adding a tag to a photo or album.
 * <p>
 * Displays a ComboBox for selecting the tag type (pre-populated from existing tags)
 * and a TextField for entering the tag value. Returns a {@link TagData} object
 * containing the user's input when OK is pressed.
 */
public class AddTagDialog extends Dialog<AddTagDialog.TagData> {
    
    /** GridPane layout for arranging labels and input controls. */
    private final GridPane grid;

    /** Label for the tag type field. */
    private final Label typeLabel;

    /** Label for the tag value field. */
    private final Label valueLabel;

    /** ComboBox to select the tag type. */
    private final ComboBox<String> typeBox;

    /** TextField to enter the tag value. */
    private final TextField valueField;

    /**
     * Constructs a new AddTagDialog.
     *
     * @param tags A collection of existing {@link Tag} objects. Their types are
     *             added to the ComboBox to allow selection.
     */
    public AddTagDialog(Collection<Tag> tags) {
        super();
        DialogPane pane = this.getDialogPane();

        // Initialize ComboBox for tag types
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

        // Initialize TextField for tag value
        this.valueField = new TextField("");
        this.valueField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(this.valueField, Priority.ALWAYS);
        GridPane.setFillWidth(this.valueField, true);

        // Initialize labels
        this.typeLabel = createContentLabel("Tag type:");
        this.typeLabel.setPrefWidth(-1.0);

        this.valueLabel = createContentLabel("Tag value:");
        this.valueLabel.setPrefWidth(-1.0);

        // Initialize grid layout
        this.grid = new GridPane();
        this.grid.setHgap(10.0);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        pane.contentTextProperty().addListener((var1x) -> {
            this.updateGrid();
        });

        this.setHeaderText("Add Tag");
        pane.getStyleClass().add("text-input-dialog");
        pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.updateGrid();

        // Convert dialog result to TagData
        this.setResultConverter((buttonType) -> {
            ButtonBar.ButtonData buttonData = buttonType == null ? null : buttonType.getButtonData();
            return buttonData == ButtonBar.ButtonData.OK_DONE 
                    ? new TagData(typeBox.getSelectionModel().getSelectedItem(), valueField.getText()) 
                    : null;
        });
    }

    /**
     * Updates the grid layout with the labels and input fields.
     */
    private void updateGrid() {
        this.grid.getChildren().clear();
        this.grid.add(this.typeLabel, 0, 0);
        this.grid.add(this.typeBox, 1, 0);
        this.grid.add(this.valueLabel, 0, 1);
        this.grid.add(this.valueField, 1, 1);
        this.getDialogPane().setContent(this.grid);
    }

    /**
     * Creates a label for use in the dialog content.
     *
     * @param label The text of the label.
     * @return A new {@link Label} instance.
     */
    private Label createContentLabel(String label) {
        return new Label(label);
    }

    /**
     * Simple data holder for a tag type and value.
     * Returned by the dialog when the user confirms input.
     */
    public class TagData {

        /** The type of the tag. */
        private String type;

        /** The value of the tag. */
        private String value;

        /**
         * Constructs a new TagData object.
         *
         * @param type  The tag type.
         * @param value The tag value.
         */
        public TagData(String type, String value) {
            this.type = type;
            this.value = value;
        }

        /**
         * Returns the tag type.
         *
         * @return the tag type.
         */
        public String getType() { return type; }

        /**
         * Returns the tag value.
         *
         * @return the tag value.
         */
        public String getValue() { return value; }
    }
}
