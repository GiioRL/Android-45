package view.library;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import main.App;
import controller.LibraryController;
import model.*;
import view.album.customDialogs.CreateTagDialog;

/**
 * Controller class for the library view.
 * Handles interactions in the user's library, including creating, deleting, 
 * renaming albums, performing tag/date searches, and managing tags.
 */
public class LibraryViewController {

    /** HBox container for album thumbnails. */
    @FXML
    private HBox albumHBox;

    /** Button to create a new tag. */
    @FXML
    private Button createTagButton;

    /** Button to create a new album. */
    @FXML
    private Button createAlbumButon;

    /** Button to delete the currently selected album. */
    @FXML
    private Button deleteAlbumButon;

    /** Button to log out from the library view. */
    @FXML
    private Button logoutButton;

    /** Button to open the currently selected album. */
    @FXML
    private Button openAlbumButon;

    /** Button to quit the application. */
    @FXML
    private Button quitButton;

    /** Button to rename the currently selected album. */
    @FXML
    private Button renameAlbumButon;

    /** Button to perform a tag-based search. */
    @FXML
    private Button tagSearchButon;

    /** ComboBox for selecting the first tag type for search. */
    @FXML
    private ComboBox<String> tagDropdown1;

    /** ComboBox for selecting the first tag value for search. */
    @FXML
    private ComboBox<String> valueDropdown1;

    /** ComboBox for selecting the conjunction ("And"/"Or") for tag search. */
    @FXML
    private ComboBox<String> conjunctionBox;

    /** ComboBox for selecting the second tag type for search. */
    @FXML
    private ComboBox<String> tagDropdown2;

    /** ComboBox for selecting the second tag value for search. */
    @FXML
    private ComboBox<String> valueDropdown2;

    /** DatePicker for selecting the start of a date range search. */
    @FXML
    private DatePicker fromDate;

    /** DatePicker for selecting the end of a date range search. */
    @FXML
    private DatePicker toDate;

    /** Current logged-in user. */
    private static User user;

    /** Singleton library controller for library operations. */
    private static LibraryController lc = LibraryController.getInstance();

    /** Currently selected album in the library view. */
    private static Album currentlySelected = null;

    /** Tracks whether an album is currently selected. */
    private boolean selection = false;

    /**
     * Injects the current user into the controller and starts the view.
     *
     * @param user The user currently logged in.
     */
    public void injectUser(User user) {
        this.user = user;
        start();
    }

    /**
     * Deselects the currently selected album, disabling related buttons.
     */
    @FXML
    void deselect() {
        if (selection) {
            selection = false;
            return;
        }
        if (currentlySelected != null) {
            currentlySelected.deselect();
        }
        openAlbumButon.setDisable(true);
        renameAlbumButon.setDisable(true);
        deleteAlbumButon.setDisable(true);
    }

    /**
     * Selects an album, enabling buttons for album operations.
     *
     * @param album The album to select.
     */
    public void select(Album album) {
        selection = true;
        if (currentlySelected != null) {
            currentlySelected.deselect();
        }
        currentlySelected = album;
        currentlySelected.select();
        openAlbumButon.setDisable(false);
        renameAlbumButon.setDisable(false);
        deleteAlbumButon.setDisable(false);
    }

    /**
     * Initializes the library view with albums, thumbnails, and tag filters.
     */
    public void start() {
        albumHBox.getChildren().clear();
        for (Album album: user.getAlbums())
            album.createThumbnail();
        ArrayList<Node> thumbnails = lc.getThumbnails(user);
        albumHBox.getChildren().addAll(thumbnails);

        tagDropdown1.setItems(FXCollections.observableArrayList(lc.getTypes(user)));
        tagDropdown1.getSelectionModel().clearSelection();
        tagDropdown1.valueProperty().addListener((obs, oldType, newType) -> {
            valueDropdown1.setItems(FXCollections.observableArrayList(lc.getValues(user, newType)));
            valueDropdown1.getSelectionModel().clearSelection();
        });

        tagDropdown2.setItems(FXCollections.observableArrayList(lc.getTypes(user)));
        tagDropdown2.getSelectionModel().clearSelection();
        tagDropdown2.valueProperty().addListener((obs, oldType, newType) -> {
            valueDropdown2.setItems(FXCollections.observableArrayList(lc.getValues(user, newType)));
            valueDropdown2.getSelectionModel().clearSelection();
        });

        ArrayList<String> conjunctions = new ArrayList<>() {{
            add("And");
            add("Or");
        }};
        conjunctionBox.setItems(FXCollections.observableArrayList(conjunctions));
        conjunctionBox.getSelectionModel().clearSelection();

        deselect();
    }

    /**
     * Creates a new album after validating the name.
     */
    @FXML
    void createAlbum() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setContentText("Album name:");
        nameDialog.showAndWait().ifPresent(albumName -> {
            if (albumName.length() == 0) {
                Alert warning = new Alert(AlertType.WARNING, "Enter a non-empty name.");
                warning.setHeaderText("Invalid Name");
                warning.showAndWait();
                return;
            }
            for (Album album: user.getAlbums()) {
                if (album.getName().equals(albumName)) {
                    Alert warning = new Alert(AlertType.WARNING, "Album with that name already exists.");
                    warning.setHeaderText("Album Already Exists");
                    warning.showAndWait();
                    return;
                }
            }
            lc.createAlbum(user, albumName);
        });
    }

    /**
     * Deletes the currently selected album.
     */
    @FXML
    void deleteAlbum() {
        currentlySelected.delete();
        deselect();
        start();
    }

    /**
     * Opens the currently selected album.
     */
    @FXML
    void openAlbum() {
        currentlySelected.start();
    }

    /**
     * Performs a tag-based search to create a new album with matching photos.
     */
    @FXML
    void tagSearch() {
        Album album = lc.tagSearch(tagDropdown1.valueProperty().getValue(), valueDropdown1.valueProperty().getValue(),
                conjunctionBox.valueProperty().getValue(), tagDropdown2.valueProperty().getValue(), valueDropdown2.valueProperty().getValue(), user);
        if (album == null) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Select a proper tag-value combination.");
            warning.setHeaderText("Invalid Selection");
            warning.showAndWait();
            return;
        }
        album.start(true);
    }

    /**
     * Performs a date range search to create a new album with matching photos.
     */
    @FXML
    void dateSearch() {
        LocalDate fromLocalDate = fromDate.valueProperty().getValue();
        LocalDate toLocalDate = toDate.valueProperty().getValue();
        if (fromLocalDate == null || toLocalDate == null) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Choose a proper date range.");
            warning.setHeaderText("Invalid Date Range");
            warning.showAndWait();
            return;
        }
        if (fromLocalDate.compareTo(toLocalDate) > 0) {
            LocalDate temp = fromLocalDate;
            fromLocalDate = toLocalDate;
            toLocalDate = temp;
        }
        Album album = lc.dateSearch(fromLocalDate, toLocalDate, user);
        album.start(true);
    }

    /**
     * Logs out the current user and returns to the login scene.
     */
    @FXML
    void logout() {
        Stage primaryStage = App.getStage();
        primaryStage.setScene(App.getLoginScene());
    }

    /**
     * Quits the application.
     */
    @FXML
    void quit() {
        App.quit();
    }

    /**
     * Renames the currently selected album after validation.
     */
    @FXML
    void renameAlbum() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setContentText("Album name:");
        nameDialog.showAndWait().ifPresent(newName -> {
            if (newName.length() == 0) {
                Alert warning = new Alert(AlertType.WARNING, "Enter a non-empty name.");
                warning.setHeaderText("Invalid Name");
                warning.showAndWait();
                return;
            }
            for (Album album: user.getAlbums()) {
                if (album.getName().equals(newName)) {
                    Alert warning = new Alert(AlertType.WARNING, "Album with that name already exists.");
                    warning.setHeaderText("Album Already Exists");
                    warning.showAndWait();
                    return;
                }
            }
            currentlySelected.setName(newName);
            currentlySelected.createThumbnail();
            start();
        });
    }

    /**
     * Creates a new tag for the current user after validation.
     */
     @FXML
    void createTag() {
        CreateTagDialog tagDialog = new CreateTagDialog();
        tagDialog.showAndWait().ifPresent(tagInfo -> {
            String newTagType = tagInfo.getKey();
            boolean multiValue = tagInfo.getValue();
            if (newTagType.length() == 0) {
                Alert warning = new Alert(AlertType.WARNING, "Enter a non-empty name.");
                warning.setHeaderText("Invalid Name");
                warning.showAndWait();
                return;
            }
            for (Tag t: user.getTags()) {
                if (t.getType().equals(newTagType)) {
                    Alert warning = new Alert(AlertType.WARNING, "Tag already exists.");
                    warning.setHeaderText("Tag Already Exists");
                    warning.showAndWait();
                    return;
                }
            }
            Tag t = new Tag(newTagType, null, multiValue);
            user.getTags().add(t);
            start();
        });
    }
}
