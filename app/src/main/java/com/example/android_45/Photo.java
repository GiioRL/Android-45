package com.example.android_45;

import android.net.Uri;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.IOException;

import java.util.ArrayList;

/**
 * Represents a photo stored in an album.
 * <p>
 * A {@code Photo} object maintains information about its file location,
 * caption, tags, and date. Because this is a serializable model class that
 * also interacts with JavaFX, certain UI fields (e.g., thumbnail, controllers)
 * are declared {@code transient} and recreated during deserialization.
 * </p>
 */
public class Photo implements Serializable {

    /** The file path of the image. */
    private Uri uri;

    /** The list of tags assigned to this photo. */
    private ArrayList<Tag> tags;

    /**
     * Constructs a new photo.
     * @param uri the file path to the photo
     * @param tags     the list of tags assigned to the photo
     */
    public Photo(Uri uri, ArrayList<Tag> tags) {
        this.uri = uri;
        this.tags = tags;
    }

    /**
     * Returns the list of tags assigned to the photo.
     *
     * @return the list of {@link Tag} objects
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Replaces the existing tag list with a new set of tags.
     *
     * @param newTags the updated tag list
     */
    public void setTags(ArrayList<Tag> newTags) {
        tags = newTags;
    }

    /**
     * Returns the file path of the photo.
     *
     * @return the photo's location
     */
    public Uri getUri() {
        return uri;
    }

    public String getName() {   return uri.toString();  }

    // Photos are equal if they have the same location (URI)
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Photo))
            return false;
        return uri.equals(((Photo) o).getUri());
    }

    /**
     * Custom deserialization logic.
     * <p>
     * After the photo's base data is deserialized, transient UI fields such
     * as {@code image} and the thumbnail must be recreated manually.
     * </p>
     *
     * @param in the input stream
     * @throws IOException if reading fails
     * @throws ClassNotFoundException if class lookup fails
     */
//    private void readObject(ObjectInputStream in)
//            throws IOException, ClassNotFoundException {
//
//        in.defaultReadObject();
//        image = createImage();
//        createThumbnail();
//    }
}
