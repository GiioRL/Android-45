package model;

import java.io.Serializable;

/**
 * Represents a tag attached to a {@link Photo}.
 * <p>
 * A tag consists of a {@code type} (e.g., "Location", "Person", "Pet") and
 * an optional {@code value} (e.g., "Home", "Rohit", "Monkey").
 * Tags are used for filtering and searching photos across albums.
 * </p>
 *
 * <p>
 * This class is {@link Serializable} so that tags associated with photos
 * can be saved and restored along with user data.
 * </p>
 */
public class Tag implements Serializable {

    /** The tag category, such as "Location" or "Person". */
    private String type;

    /** The specific tag value (may be null). */
    private String value;

    /** Controls whether a tag can have multiple values or a single value */
    private boolean multiValue;

    /**
     * Creates a new tag with the specified type and value.
     *
     * @param type  the tag category
     * @param value the tag value (may be {@code null})
     */
    public Tag(String type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Creates a new tag with the specified type and value.
     *
     * @param type  the tag category
     * @param value the tag value (may be {@code null})
     * @param multiValue the multivalue
     */
    public Tag(String type, String value, boolean multiValue) {
        this.type = type;
        this.value = value;
        this.multiValue = multiValue;
    }

    /**
     * Returns the tag type.
     *
     * @return the tag's category
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the tag value.
     *
     * @return the tag value, or {@code null} if none
     */
    public String getValue() {
        return value;
    }


    /**
     * Returns the multivalue
     * 
     * @return the multivalue boolean
     */
    public boolean getMultiValue() {
        return multiValue;
    }

    /**
     * Determines equality based on tag type only.
     * <p>
     * This is used to check whether two tags belong to the same category,
     * ignoring their specific values.
     * </p>
     *
     * @param o the object to compare
     * @return {@code true} if the other object is a Tag with the same type
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) o;
        return type.equals(other.getType());
    }

    /**
     * Compares this tag to another tag by value only.
     * <p>
     * Unlike {@link #equals(Object)}, this method checks whether two tags
     * have identical values, ignoring their types.
     * </p>
     *
     * @param other the other tag
     * @return {@code true} if both tags have the same non-null value
     */
    public boolean tagEquals(Tag other) {
        if (value == null || other == null)
            return false;
        return value.equals(other.getValue());
    }

    /**
     * Returns a string representation of the tag in the form
     * {@code "type: value"}.
     *
     * @return the string representation of the tag
     */
    @Override
    public String toString() {
        return type + ": " + value;
    }
}
