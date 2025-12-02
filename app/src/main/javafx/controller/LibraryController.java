package controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;

import controller.album.AlbumController;
import javafx.scene.Node;
import model.*;

/**
 * Controller class for managing a user's photo library.
 * <p>
 * Provides methods for creating albums, retrieving thumbnails, and performing
 * searches based on tags or date ranges. Uses a singleton pattern to provide
 * a centralized point of access.
 * </p>
 * <p>
 * This class interacts closely with {@link AlbumController} for photo creation
 * and album handling.
 * </p>
 */
public class LibraryController {

    /** Singleton instance of LibraryController. */
    private static LibraryController instance;

    /** Reference to the AlbumController singleton. */
    private static AlbumController ac = AlbumController.getInstance();

    /** Private constructor to enforce singleton pattern. */
    private LibraryController() {}

    /**
     * Returns the singleton instance of this controller.
     * Creates a new instance if one does not already exist.
     *
     * @return the singleton LibraryController instance
     */
    public static LibraryController getInstance() {
        if (instance == null) {
            instance = new LibraryController();
        }
        return instance;
    }

    /**
     * Creates a new album with the default name "Unnamed album" for a given user.
     *
     * @param user the user for whom the album is created
     */
    public void createAlbum(User user) {
        createAlbum(user, "Unnamed album");
    }

    /**
     * Creates a new album with a specified name for a given user.
     *
     * @param user the user for whom the album is created
     * @param name the name of the album
     */
    public void createAlbum(User user, String name) {
        Album album = new Album(user, name);
        user.addAlbum(album);
        album.start();
    }

    /**
     * Retrieves all album thumbnails for a given user.
     *
     * @param user the user whose albums are queried
     * @return a list of JavaFX {@link Node} objects representing album thumbnails
     */
    public ArrayList<Node> getThumbnails(User user) {
        ArrayList<Node> thumbnails = new ArrayList<>();
        for (Album album: user.getAlbums()) {
            Node thumbnail = album.getThumbnail();
            thumbnails.add(thumbnail);
        }
        return thumbnails;
    }

    /**
     * Returns a list of all tag types associated with a user.
     * <p>
     * Only includes tags where the value is {@code null} (i.e., represents a type category).
     * </p>
     *
     * @param user the user whose tag types are requested
     * @return a list of tag type strings
     */
    public ArrayList<String> getTypes(User user) {
        ArrayList<String> types = new ArrayList<>();
        ArrayList<Tag> tags = user.getTags();
        for (Tag tag: tags) {
            if (tag.getValue() == null) {
                types.add(tag.getType());
            }
        }
        return types;
    }

    /**
     * Returns all tag values of a given type associated with a user.
     *
     * @param user the user whose tag values are requested
     * @param type the tag type
     * @return a list of tag values for the specified type
     */
    public ArrayList<String> getValues(User user, String type) {
        ArrayList<String> values = new ArrayList<>();
        ArrayList<Tag> tags = user.getTags();
        for (Tag tag: tags) {
            if (tag.equals(new Tag(type, null))) {
                if (tag.getValue() != null) {
                    values.add(tag.getValue());
                }
            }
        }
        return values;
    }

    /**
     * Searches a user's photo library for photos matching a specific tag.
     *
     * @param type the tag type
     * @param value the tag value
     * @param user the user whose library is searched
     * @return an {@link Album} containing photos matching the tag, or null if input is invalid
     */
    public Album tagSearch(String type, String value, User user) {
        if (type == null || value == null) {
            return null;
        }
        HashSet<Photo> albumPhotos = new HashSet<>();
        Tag tag = new Tag(type, value);
        ArrayList<Photo> photos = new ArrayList<>();
        for (Album album: user.getAlbums())
            photos.addAll(album.getPhotos());

        for (Photo photo: photos) {
            ArrayList<Tag> photoTags = photo.getTags();
            if (photoTags != null) {
                if (photoTags.contains(tag)) {
                    for (Tag photoTag: photoTags) {
                        if (photoTag.tagEquals(tag)) {
                            albumPhotos.add(photo);
                        }
                    }
                }
            }
        }

        return new Album(new ArrayList<>(albumPhotos), "Unnamed album", user);
    }

    /**
     * Searches a user's photo library for photos matching two tags combined with a logical conjunction.
     *
     * @param type1 the first tag type
     * @param value1 the first tag value
     * @param conjunction "And" or "Or" specifying how to combine the results
     * @param type2 the second tag type
     * @param value2 the second tag value
     * @param user the user whose library is searched
     * @return an {@link Album} containing photos matching the search criteria, or null for invalid conjunction
     */
    public Album tagSearch(String type1, String value1, String conjunction, String type2, String value2, User user) {
        Album album1 = tagSearch(type1, value1, user);
        if (conjunction == null) {
            return album1;
        } else {
            Album album2 = tagSearch(type2, value2, user);
            ArrayList<Photo> album1Photos = album1.getPhotos();
            ArrayList<Photo> album2Photos = album2.getPhotos();
            ArrayList<Photo> albumPhotos = new ArrayList<>();

            if (conjunction.equals("And")) {
                for (Photo photo: album1Photos) {
                    if (album2Photos.contains(photo)) {
                        albumPhotos.add(photo);
                    }
                }
                return trimDuplicates(new Album(albumPhotos, "Unnamed Album", user));
            } else if (conjunction.equals("Or")) {
                for (Photo photo: album2Photos) {
                    album1Photos.add(photo);
                }
                return trimDuplicates(new Album(album1Photos, "Unnamed Album", user));
            } else {
                System.out.println("invalid conjunction");
                return null;
            }
        }
    }

    /**
     * Removes duplicate photos in an album based on location.
     *
     * @param album the album to process
     * @return a new {@link Album} without duplicate photos
     */
    private Album trimDuplicates(Album album) {
        ArrayList<Photo> photos = album.getPhotos();
        if (photos.size() <= 1) {
            return album;
        }
        photos.sort(Comparator.comparing(Photo::getLocation));
        ArrayList<Photo> albumPhotos = new ArrayList<>();
        albumPhotos.add(photos.get(0));
        for (int i = 1; i < photos.size(); i++) {
            if (photos.get(i).getLocation().compareTo(photos.get(i-1).getLocation()) != 0) {
                albumPhotos.add(photos.get(i));
            }
        }
        return new Album(albumPhotos, album.getName(), album.getUser());
    }

    /**
     * Searches a user's photo library for photos within a given date range.
     *
     * @param from the start date (inclusive)
     * @param to the end date (inclusive)
     * @param user the user whose library is searched
     * @return an {@link Album} containing photos within the date range, or null for invalid input
     */
    public Album dateSearch(LocalDate from, LocalDate to, User user) {
        if (from == null || to == null) {
            return null;
        }
        HashSet<Photo> albumPhotos = new HashSet<>();
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(from.getYear(), from.getMonthValue()-1, from.getDayOfMonth(),
                fromCalendar.getMinimum(Calendar.HOUR_OF_DAY),
                fromCalendar.getMinimum(Calendar.MINUTE),
                fromCalendar.getMinimum(Calendar.SECOND));

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(to.getYear(), to.getMonthValue()-1, to.getDayOfMonth(),
                fromCalendar.getMaximum(Calendar.HOUR_OF_DAY),
                fromCalendar.getMaximum(Calendar.MINUTE),
                fromCalendar.getMaximum(Calendar.SECOND));

        ArrayList<Photo> photos = new ArrayList<>();
        for (Album album: user.getAlbums())
            photos.addAll(album.getPhotos());

        String resFolder = System.getProperty("user.dir") + File.separator + "src" + File.separator + "resources" + File.separator;
        Photo fromDummy = ac.createPhoto(resFolder + "dummyPhoto1.jpg", "fromDummy", null, fromCalendar.getTimeInMillis());
        Photo toDummy = ac.createPhoto(resFolder + "dummyPhoto2.jpg", "toDummy", null, toCalendar.getTimeInMillis());

        ArrayList<Photo> photoCopy = (ArrayList<Photo>) photos.clone();
        photoCopy.add(fromDummy);
        photoCopy.add(toDummy);
        photoCopy.sort(Comparator.comparing(Photo::getDate));

        for (int i = photoCopy.indexOf(fromDummy) + 1; i < photoCopy.indexOf(toDummy); i++) {
            albumPhotos.add(photoCopy.get(i));
        }
        return new Album(new ArrayList<>(albumPhotos), "Unnamed Album", user);
    }
}
