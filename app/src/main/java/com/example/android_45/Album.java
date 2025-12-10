package com.example.android_45;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a photo album belonging to a specific User.
 * <p>
 * An {@code Album} stores a list of {@link Photo} objects as well as metadata
 * such as the album name and the owning user. Although this is primarily a
 * model class, it also maintains several transient JavaFX components used
 * for rendering album views and thumbnails.
 * </p>
 *
 * <p><b>Note:</b> Transient fields (FXML nodes, controllers, and scenes)
 * are recreated when needed, since they cannot be serialized. The underlying
 * album data ({@code name}, {@code photos}, {@code user}) is fully
 * serializable.</p>
 */
public class Album implements Serializable {

    /** The name of the album. */
    private String name;

    /** The list of photos contained in this album. */
    private ArrayList<Photo> photos;

    /**
     * Creates an empty album with the specified name and owner.
     *
     * @param name the album name
     */
    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    /**
     * Creates an album with a predefined list of photos.
     *
     * @param photos the photos to include
     * @param name   the album name
     */
    public Album(ArrayList<Photo> photos, String name) {
        this.photos = photos;
        this.name = name;
    }

    /**
     * Returns the list of photos in this album.
     *
     * @return the list of {@link Photo} objects
     */
    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    /**
     * Returns the album name.
     *
     * @return the name of the album
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the album.
     *
     * @param newName the updated name
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Creates a shallow clone of this album.
     * <p>
     * The clone references the same {@code photos} list and user,
     * but is a distinct Album instance.
     * </p>
     *
     * @return a cloned {@code Album} instance
     */
    public Album clone() {
        return new Album(photos, name);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Album))
            return false;
        return name.equals(((Album) o).name);
    }
}
