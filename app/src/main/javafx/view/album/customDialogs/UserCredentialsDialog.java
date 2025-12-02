package view.album.customDialogs;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * A dialog for entering user credentials (username and password).
 * <p>
 * Provides two fields for the user to input a username and a password.
 * Returns a {@link CredData} object when OK is pressed, or null if cancelled.
 * </p>
 */
public class UserCredentialsDialog extends Dialog<UserCredentialsDialog.CredData> {

    /** Grid layout holding the labels and input fields. */
    private final GridPane grid;

    /** Label for the username field. */
    private final Label usernameLabel;

    /** Label for the password field. */
    private final Label passwordLabel;

    /** Text field for entering the username. */
    private final TextField usernameField;

    /** Password field for entering the password. */
    private final PasswordField passwordField;

    /**
     * Creates a new dialog for entering user credentials.
     * <p>
     * The dialog contains a username text field and a password field,
     * with OK and Cancel buttons.
     * </p>
     */
    public UserCredentialsDialog() {
        super();
        DialogPane pane = this.getDialogPane();

        this.usernameField = new TextField("");
        this.usernameField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(this.usernameField, Priority.ALWAYS);
        GridPane.setFillWidth(this.usernameField, true);

        this.passwordField = new PasswordField();
        this.passwordField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(this.passwordField, Priority.ALWAYS);
        GridPane.setFillWidth(this.passwordField, true);

        this.usernameLabel = createContentLabel("Username:");
        this.usernameLabel.setPrefWidth(-1.0);

        this.passwordLabel = createContentLabel("Password:");
        this.passwordLabel.setPrefWidth(-1.0);

        this.grid = new GridPane();
        this.grid.setHgap(10.0);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        pane.contentTextProperty().addListener((var1x) -> {
            this.updateGrid();
        });

        this.setHeaderText("Add User");
        pane.getStyleClass().add("text-input-dialog");
        pane.getButtonTypes().addAll(new ButtonType[]{ButtonType.OK, ButtonType.CANCEL});
        this.updateGrid();

        this.setResultConverter((buttonType) -> {
            ButtonBar.ButtonData buttonData = buttonType == null ? null : buttonType.getButtonData();
            return buttonData == ButtonData.OK_DONE ? new CredData(usernameField.getText(), passwordField.getText()) : null;
        });
    }

    /**
     * Updates the dialog grid layout with the current labels and fields.
     */
    private void updateGrid() {
        this.grid.getChildren().clear();
        this.grid.add(this.usernameLabel, 0, 0);
        this.grid.add(this.usernameField, 1, 0);
        this.grid.add(this.passwordLabel, 0, 1);
        this.grid.add(this.passwordField, 1, 1);
        this.getDialogPane().setContent(this.grid);
    }

    /**
     * Creates a label for use in the dialog.
     *
     * @param label The text for the label.
     * @return A new Label with the specified text.
     */
    private Label createContentLabel(String label) {
        return new Label(label);
    }

    /**
     * Data class representing user credentials entered in the dialog.
     */
    public class CredData {
        private String username, password;

        /**
         * Constructs a new credentials object.
         *
         * @param username The entered username.
         * @param password The entered password.
         */
        public CredData(String username, String password) {
            this.username = username;
            this.password = password;
        }

        /**
         * Returns the entered username.
         *
         * @return Username as a string.
         */
        public String getUsername() { return username; }

        /**
         * Returns the entered password.
         *
         * @return Password as a string.
         */
        public String getPassword() { return password; }
    }
}
