package view.album;

import java.io.File;
import java.util.List;

import controller.album.AlbumController;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.App;
import model.*;
import view.album.customDialogs.*;
import view.album.photoBox.PhotoBoxController;

/**
 * Controller class for the Album view in the application.
 * Handles the user interface and interactions within an Album,
 * including adding/removing photos, editing captions, managing tags,
 * copying/moving photos between albums, and displaying slideshows.
 */
public class AlbumViewController {

    @FXML
    private Button addPhotoButon;

    @FXML
    private Button addTagButon;

    @FXML
    private Button backButon;

    @FXML
    private Button captionPhotoButon;

    @FXML
    private Button copyButon;

    @FXML
    private Button displayPhotoButon;

    @FXML
    private Button moveButon;

    @FXML
    private Button removePhotoButon;

    @FXML
    private Button removeTagButon;

    @FXML
    private Button slideshowButon;

    @FXML
    private Button quitButton;

    @FXML
    private VBox butonBox;

    @FXML
    private VBox photoVBox;
    
    private static Album album;
    private static AlbumController ac = AlbumController.getInstance();
    private ArrayList<Node> photoBoxes = new ArrayList<Node>();
    private ArrayList<PhotoBoxController> pbControllers = new ArrayList<PhotoBoxController>();
    private Photo curSelected = null;

    /**
     * Injects an Album into this controller and initializes the scene.
     * @param album The album to display
     */
    public void injectAlbum(Album album) {
        this.album = album;
        initScene(false);
    }

    /**
     * Injects an Album and optionally adds a library button.
     * @param album The album to display
     * @param bool Whether to show the "Add to Library" button
     */
    public void injectAlbum(Album album, boolean bool) {
        this.album = album;
        initScene(bool);
    }

     /**
     * Initializes the album scene with photo thumbnails.
     */
    private void initScene() {
        initScene(false);
    }

    /**
     * Initializes the album scene with photo thumbnails.
     * @param addLibraryButton Whether to display "Add to Library" button
     */
    private void initScene(boolean bool) {
        ArrayList<Node> thumbnails = ac.getThumbnails(album);
        int num = thumbnails.size();
        ac.injectAlbumController(this);
        photoVBox.getChildren().clear(); // there could be better ways..
        photoBoxes.clear();
        pbControllers.clear();

        for (int i = 0; i < (num+2)/3; i++) {
            photoBoxes.add(ac.getPhotoBox());
            Node[] photos = new Node[3];
            for (int j = 0; j < 3; j++) {
                if (3*i+j >= num) {
                    photos[j] = null;
                } else {
                    photos[j] = thumbnails.get(3*i + j);
                }
            }
            pbControllers.get(i).init(photos);
        }
        photoVBox.getChildren().addAll(photoBoxes);
        if (bool) {
            Button buton = new Button("Add to Library");
            buton.setOnAction(e -> addToLibrary());
            butonBox.getChildren().addFirst(buton);
            butonBox.setSpacing(10);
        }
    }

    /**
     * Registers a PhotoBoxController with this controller.
     * @param pb The PhotoBoxController to register
     */
    public void injectPB(PhotoBoxController pb) {
        pbControllers.add(pb);
    }

    /**
     * Selects a photo, highlighting it and enabling relevant buttons.
     * @param photo The photo to select
     */
    public void select(Photo photo) {
        if (curSelected == null || !curSelected.equals(photo)) {
            deselect();
            curSelected = photo;
            curSelected.select();
            removePhotoButon.setDisable(false);
            captionPhotoButon.setDisable(false);
            displayPhotoButon.setDisable(false);
            addTagButon.setDisable(false);
            removeTagButon.setDisable(false);
            copyButon.setDisable(false);
            moveButon.setDisable(false);
        }
        else
            deselect();
    }

     /**
     * Deselects the current photo, disabling action buttons.
     */
    private void deselect() {
        if (curSelected != null) {
            curSelected.deselect();
            removePhotoButon.setDisable(true);
            captionPhotoButon.setDisable(true);
            displayPhotoButon.setDisable(true);
            addTagButon.setDisable(true);
            removeTagButon.setDisable(true);
            copyButon.setDisable(true);
            moveButon.setDisable(true);
        }
        curSelected = null;
    }

    /**
     * Adds the current album to the user's library.
     */
    
    @FXML
    void addToLibrary() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setContentText("Album name:");
        nameDialog.showAndWait().ifPresent(albumName -> {
            if (albumName.length() == 0) {
                Alert warning = new Alert(AlertType.WARNING, "Enter a non-empty name.");
                warning.setHeaderText("Invalid Name");
                warning.showAndWait();
                return;
            }
            for (Album a: album.getUser().getAlbums()) {
                if (a.getName().equals(albumName)) {
                    Alert warning = new Alert(AlertType.WARNING, "Album with that name already exists.");
                    warning.setHeaderText("Album Already Exists");
                    warning.showAndWait();
                    return;
                }
            }
            album.setName(albumName);
            album.getUser().getAlbums().add(album.clone());
            album.getUser().start();
        });
    }

    /**
     * Opens a file chooser to add one or more photos to the album.
     * @param event The action event
     */
    @FXML
    void addPhoto(ActionEvent event) {
        Stage primaryStage = App.getStage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg, *.jpeg, *.gif, *.bmp)", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        List<File> photoFiles = fileChooser.showOpenMultipleDialog(primaryStage);
        if (photoFiles == null)
            return;
        
        for (File photoFile: photoFiles) {
            String location = photoFile.getAbsolutePath();
            Photo newPhoto = ac.createPhoto(location);
            if (!album.getPhotos().contains(newPhoto)) {
                newPhoto.getPhotoThumbnailController().injectAlbumController(this);
                album.getPhotos().add(newPhoto);
                initScene(false);
            }
            else {
                Alert warning = new Alert(Alert.AlertType.WARNING, "Photo already exists in album.");
                warning.setHeaderText("Photo Already Exists");
                warning.showAndWait();
            }
        }
    }

     /**
     * Adds a tag to the currently selected photo.
     * @param event The action event
     */
    @FXML
    void addTag(ActionEvent event) {
        AddTagDialog locationDialog = new AddTagDialog(album.getUser().getTags());
        locationDialog.showAndWait().ifPresent(tagData -> {
            if (tagData.getType() == null || tagData.getValue().length() == 0) {
                Alert warning = new Alert(Alert.AlertType.WARNING, "Enter a tag-value combination.");
                warning.setHeaderText("Invalid Tag Selection");
                warning.showAndWait();
                return;
            }
            boolean multiValue = true;
            for (Tag t: album.getUser().getTags()) {
                if (t.getType().equals(tagData.getType())) {
                    multiValue = t.getMultiValue();
                    break;
                }
            }
            Tag newTag = new Tag(tagData.getType(), tagData.getValue(), multiValue);
            ArrayList<Tag> curTags = curSelected.getTags();
            if (curTags == null)
                curTags = new ArrayList<Tag>();
            for (Tag tag: curTags) {
                if (tag.equals(newTag)) {
                    // System.out.println(tag.getMultiValue());
                    if (tag.tagEquals(newTag)) {
                        Alert warning = new Alert(Alert.AlertType.WARNING, "Photo already contains tag.");
                        warning.setHeaderText("Tag Already Exists");
                        warning.showAndWait();
                        return;
                    }
                    else if (!tag.getMultiValue()) {
                        Alert warning = new Alert(Alert.AlertType.WARNING, "Photo cannot have more than one value for " + tag.getType() + " tag.");
                        warning.setHeaderText("Cannot Have Multiple Tag Values");
                        warning.showAndWait();
                        return;
                    }
                }
            }
            curTags.add(newTag);
            curSelected.setTags(curTags);
            boolean userHasTag = false;
            for (Tag t: album.getUser().getTags())
                if (t.equals(newTag) && t.tagEquals(newTag))
                    userHasTag = true;
            if (!userHasTag)
                album.getUser().getTags().add(newTag);
            Alert info = new Alert(Alert.AlertType.WARNING, "Tag added successfully!");
            info.setHeaderText("Tag Added");
            info.showAndWait();
        });
    }

    /**
 * Returns to the previous view in the album navigation.
 * Deselects the currently selected photo before navigating back.
 *
 * @param event The action event triggered by the back button.
 */
    @FXML
    void back(ActionEvent event) {
        deselect();
        ac.back(album);
    }

    /**
 * Prompts the user to enter a caption for the currently selected photo.
 * Updates the photo's caption and refreshes the view.
 *
 * @param event The action event triggered by the caption photo button.
 */
    @FXML
    void captionPhoto(ActionEvent event) {
        TextInputDialog captionDialog = new TextInputDialog();
        captionDialog.setContentText("Photo caption:");
        captionDialog.setHeaderText("Caption Photo");
        captionDialog.showAndWait().ifPresent(caption -> {
            curSelected.setCaption(caption);
            initScene();
            curSelected.select();
        });
    }

    /**
 * Copies the currently selected photo to another album chosen by the user.
 * Displays an information or warning alert based on whether the copy succeeds.
 *
 * @param event The action event triggered by the copy button.
 */
    @FXML
    void copy(ActionEvent event) {
        ChoiceDialog<String> albumDialog = new ChoiceDialog<String>();
        for (Album userAlbum: album.getUser().getAlbums())
            if (!userAlbum.getName().equals(album.getName()))
                albumDialog.getItems().add(userAlbum.getName());
        albumDialog.setContentText("Album to copy to:");
        albumDialog.setHeaderText("Choose Album");
        albumDialog.showAndWait().ifPresent(destAlbum -> {
            if (destAlbum == null)
                return;
            for (Album userAlbum: album.getUser().getAlbums()) {
                if (userAlbum.getName().equals(destAlbum)) {
                    if (!userAlbum.getPhotos().contains(curSelected)) {
                        userAlbum.getPhotos().add(curSelected);
                        Alert info = new Alert(Alert.AlertType.INFORMATION, "Photo copied to " + destAlbum + " successfully!");
                        info.setHeaderText("Copied Successfully");
                        info.showAndWait();
                    }
                    else {
                        Alert warning = new Alert(Alert.AlertType.WARNING, "Photo already exists in " + destAlbum + ".");
                        warning.setHeaderText("Photo Already Exists");
                        warning.showAndWait();
                    }
                    return;
                }
            }
        });
    }

    /**
 * Displays the currently selected photo in a dialog window.
 *
 * @param event The action event triggered by the display photo button.
 */
    @FXML
    void displayPhoto(ActionEvent event) {
        new ImageDialog(curSelected).showAndWait();
    }

    /**
 * Moves the currently selected photo to another album chosen by the user.
 * Updates both the source and destination albums and provides appropriate alerts.
 *
 * @param event The action event triggered by the move button.
 */
    @FXML
    void move(ActionEvent event) {
        ChoiceDialog<String> albumDialog = new ChoiceDialog<String>();
        for (Album userAlbum: album.getUser().getAlbums())
            if (!userAlbum.getName().equals(album.getName()))
                albumDialog.getItems().add(userAlbum.getName());
        albumDialog.setContentText("Album to move to:");
        albumDialog.setHeaderText("Choose Album");
        albumDialog.showAndWait().ifPresent(destAlbum -> {
            if (destAlbum == null)
                return;
            for (Album userAlbum: album.getUser().getAlbums()) {
                if (userAlbum.getName().equals(destAlbum)) {
                    if (!userAlbum.getPhotos().contains(curSelected)) {
                        userAlbum.getPhotos().add(curSelected);
                        album.getPhotos().remove(curSelected);
                        deselect();
                        initScene();
                        Alert info = new Alert(Alert.AlertType.INFORMATION, "Photo moved to " + destAlbum + " successfully!");
                        info.setHeaderText("Moved Successfully");
                        info.showAndWait();
                    }
                    else {
                        Alert warning = new Alert(Alert.AlertType.WARNING, "Photo already exists in " + destAlbum + ".");
                        warning.setHeaderText("Photo Already Exists");
                        warning.showAndWait();
                    }
                    return;
                }
            }
        });
    }

    /**
 * Removes the currently selected photo from the album.
 * Also removes any tags from the user’s global tag list that no longer exist in any photo.
 *
 * @param event The action event triggered by the remove photo button.
 */
    @FXML
    void removePhoto(ActionEvent event) {
        album.getPhotos().remove(curSelected);
        if (curSelected.getTags() != null) {
            for (Tag t: curSelected.getTags())
                if (!searchTagInAlbums(t))
                    removeUserTag(t);
        }
        deselect();
        initScene();
    }

    /**
 * Removes a tag from the currently selected photo.
 * Prompts the user to select a tag to remove and updates the user's global tag list if necessary.
 *
 * @param event The action event triggered by the remove tag button.
 */
    @FXML
    void removeTag(ActionEvent event) {
        ArrayList<Tag> curTags = curSelected.getTags();
        if (curTags == null || curTags.size() == 0)
        {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Photo does not have any tags.");
            warning.setHeaderText("No Tags");
            warning.showAndWait();
            return;
        }
        RemoveTagDialog locationDialog = new RemoveTagDialog(curTags);
        locationDialog.showAndWait().ifPresent(tagData -> {
            if (tagData.getType() == null || tagData.getValue() == null) {
                Alert warning = new Alert(Alert.AlertType.WARNING, "Select a tag-value combination.");
                warning.setHeaderText("Invalid Selection");
                warning.showAndWait();
                return;
            }
            Tag oldTag = new Tag(tagData.getType(), tagData.getValue());
            for (int i = 0; i < curTags.size(); i++) {
                Tag t = curTags.get(i);
                if (t.equals(oldTag) && t.tagEquals(oldTag)) {
                    curTags.remove(i);
                    break;
                }
            }
            curSelected.setTags(curTags);
            if (!searchTagInAlbums(oldTag)) {
                removeUserTag(oldTag);
            }
            Alert info = new Alert(Alert.AlertType.INFORMATION, "Tag removed successfully!");
            info.setHeaderText("Tag Removed");
            info.showAndWait();
        });
    }

    /**
 * Starts a slideshow of the photos in the current album, beginning with the currently selected photo.
 *
 * @param event The action event triggered by the slideshow button.
 */
    @FXML
    void slideshow(ActionEvent event) {
        new SlidesDialog(album, curSelected).showAndWait();
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
 * Exits the application.
 *
 * @param event The action event triggered by the quit button.
 */
    @FXML
    void quit(ActionEvent event) {
        App.quit();
    }

    private boolean searchTagInAlbums(Tag searchTag) {
        boolean somePhotoHasTag = false;
        for (Album a: album.getUser().getAlbums()) {
            for (Photo p: a.getPhotos()) {
                if (p.getTags() != null) {
                    for (Tag t: p.getTags()) {
                        if (searchTag.equals(t) && searchTag.tagEquals(t))
                            somePhotoHasTag = true;
                    }
                }
            }
        }
        return somePhotoHasTag;
    }

    private void removeUserTag(Tag oldTag) {
        ArrayList<Tag> userTags = album.getUser().getTags();
        if (userTags == null)
            return;
        for (int i = 0; i < userTags.size(); i++) {
            Tag t = userTags.get(i);
            if (oldTag.equals(t) && oldTag.tagEquals(t)) {
                userTags.remove(i);
                break;
            }
        }
    }
}
