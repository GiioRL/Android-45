package view.album.customDialogs;

import java.util.Collection;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Pair;
import model.Tag;

/**
 * A dialog for creating a new {@link Tag}.  
 * <p>
 * This dialog prompts the user for two pieces of information:
 * <ul>
 *   <li>The tag name (a {@code String})</li>
 *   <li>Whether the tag supports multiple values (a {@code boolean})</li>
 * </ul>
 * The result is returned as a {@link Pair} where the key is the tag name and the value
 * indicates whether multiple values are allowed.
 */
public class CreateTagDialog extends Dialog<Pair<String, Boolean>> {
    
    /** Layout grid used to arrange dialog controls. */
    private final GridPane grid;

    /** Label for the tag name input field. */
    private final Label typeLabel;

    /** Label for the multi-value checkbox. */
    private final Label multiValueLabel;

    /** Text field where the user enters the tag name. */
    private final TextField typeField;

    /** Checkbox indicating whether the tag supports multiple values. */
    private final CheckBox muitiValueCheck;

    /**
     * Constructs a new {@code CreateTagDialog}.
     * <p>
     * Sets up all controls, labels, layout, and result conversion.
     * The dialog contains OK and Cancel buttons.  
     * When the user confirms with OK, a {@link Pair} containing
     * the tag name and the multi-value flag is returned.
     */
    public CreateTagDialog() {
        super();
        DialogPane pane = this.getDialogPane();

        this.typeField = new TextField("");
        this.typeField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(this.typeField, Priority.ALWAYS);
        GridPane.setFillWidth(this.typeField, true);

        this.muitiValueCheck = new CheckBox();

        this.typeLabel = createContentLabel("Tag name:");
        this.typeLabel.setPrefWidth(-1.0);

        this.multiValueLabel = createContentLabel("Multiple values:");
        this.multiValueLabel.setPrefWidth(-1.0);

        this.grid = new GridPane();
        this.grid.setHgap(10.0);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        // Ensures UI is updated when content text changes.
        pane.contentTextProperty().addListener((var1x) -> {
            this.updateGrid();
        });

        this.setHeaderText("Create Tag");
        pane.getStyleClass().add("text-input-dialog");
        pane.getButtonTypes().addAll(new ButtonType[]{ButtonType.OK, ButtonType.CANCEL});
        this.updateGrid();

        this.setResultConverter((buttonType) -> {
            ButtonBar.ButtonData buttonData = buttonType == null ? null : buttonType.getButtonData();
            return buttonData == ButtonData.OK_DONE
                    ? new Pair<>(typeField.getText(), muitiValueCheck.isSelected())
                    : null;
        });
    }

    /**
     * Refreshes and rebuilds the dialog's grid layout.
     * <p>
     * This method clears existing components and re-adds them in the correct layout.
     * It is invoked when the dialog is initialized or when its content text changes.
     */
    private void updateGrid() {
        this.grid.getChildren().clear();
        this.grid.add(this.typeLabel, 0, 0);
        this.grid.add(this.typeField, 1, 0);
        this.grid.add(this.multiValueLabel, 0, 1);
        this.grid.add(this.muitiValueCheck, 1, 1);
        this.getDialogPane().setContent(this.grid);
    }

    /**
     * Creates a label for use within the dialog content.
     *
     * @param label the text to display in the label
     * @return a new {@link Label} initialized with the given text
     */
    private Label createContentLabel(String label) {
        return new Label(label);
    }
}
