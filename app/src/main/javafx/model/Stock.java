package model;

import java.util.ArrayList;

import controller.album.AlbumController;

import java.io.File;

/**
 * Represents the built-in stock user included with the application.
 * <p>
 * The {@code Stock} user is automatically populated with preset tags
 * and a default album containing several preloaded example photos.
 * These stock photos are located in the application's
 * {@code /resources/StockPhotos/} directory.
 * </p>
 *
 * <p>
 * This class extends {@link User} but behaves as a read-only, non-deletable,
 * non-modifiable system user intended to provide sample images for testing
 * and demonstration purposes.
 * </p>
 */
public class Stock extends User {

    /** Reference to the album controller used to create stock photos. */
    private AlbumController albumModel = AlbumController.getInstance();

    /**
     * Constructs the stock user, initializing its tags and preloaded photos.
     * The constructor:
     * <ul>
     *   <li>Sets username and password to {@code "stock"}.</li>
     *   <li>Initializes a preset list of commonly used tags.</li>
     *   <li>Loads several sample photos from the resources folder.</li>
     *   <li>Puts them into a single "Stock" album.</li>
     * </ul>
     */
    public Stock() {
        username = "stock";
        password = "stock";

        // Predefined tags
        tags.add(new Tag("Location", null, false)); 
        tags.add(new Tag("Location", "Home", false));
        tags.add(new Tag("Location", "Rutgers University", false));
        tags.add(new Tag("Location", "Background", false));

        tags.add(new Tag("Person", null));
        tags.add(new Tag("Person", "Rohit"));

        tags.add(new Tag("Pet", null));
        tags.add(new Tag("Pet", "Monkey"));
        tags.add(new Tag("Pet", "Pumpkin"));

        // Path to stock resource directory
        String resFolder = System.getProperty("user.dir")
                + File.separator + "src"
                + File.separator + "resources"
                + File.separator + "StockPhotos"
                + File.separator;

        ArrayList<Photo> photos = new ArrayList<>();

        // Create sample photos with default tags
        photos.add(albumModel.createPhoto(resFolder + "Monk_with_Munk.jpg", "Monk with Munk", new ArrayList<Tag>() {{
            add(new Tag("Location", "Home", false));
            add(new Tag("Pet", "Monkey"));
        }}));

        photos.add(albumModel.createPhoto(resFolder + "Monkey_Fall.jpg", "Monkey Fall", new ArrayList<Tag>() {{
            add(new Tag("Location", "Home", false));
            add(new Tag("Pet", "Monkey"));
        }}));

        photos.add(albumModel.createPhoto(resFolder + "Pumpkin.jpg", "Pumpkin", new ArrayList<Tag>() {{
            add(new Tag("Location", "Home", false));
            add(new Tag("Pet", "Pumpkin"));
        }}));

        photos.add(albumModel.createPhoto(resFolder + "Skill_Issue.jpg", "Pool", new ArrayList<Tag>() {{
            add(new Tag("Location", "Rutgers University", false));
            add(new Tag("Person", "Rohit"));
        }}));

        photos.add(albumModel.createPhoto(resFolder + "Costa_Rica_Background.jpg", "Costa Rica", new ArrayList<Tag>() {{
            add(new Tag("Location", "Background", false));
        }}));

        photos.add(albumModel.createPhoto(resFolder + "Forest_Background.jpg", "Forest", new ArrayList<Tag>() {{
            add(new Tag("Location", "Background", false));
        }}));

        photos.add(albumModel.createPhoto(resFolder + "Mountain_Background.jpg", "Mountain", new ArrayList<Tag>() {{
            add(new Tag("Location", "Background", false));
        }}));

        photos.add(albumModel.createPhoto(resFolder + "Ocean_Background.jpg", "Ocean", new ArrayList<Tag>() {{
            add(new Tag("Location", "Background", false));
        }}));

        // Add the stock album
        albums.add(new Album(photos, "Stock", this));
    }
}
