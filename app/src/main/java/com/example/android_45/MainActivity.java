package com.example.android_45;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private LinearLayout albumScrollContainer;

    private Button openAlbumButon, createAlbumButon, renameAlbumButon, deleteAlbumButon, tagSearchButon;

    private Spinner tagDropdown1, tagDropdown2, conjunctionBox;

    private EditText valueField1, valueField2;

    private View curSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Match XML elements to Java objects
        albumScrollContainer = findViewById(R.id.albumScrollContainer);

        openAlbumButon = findViewById(R.id.openAlbumButon);
        createAlbumButon = findViewById(R.id.createAlbumButon);
        renameAlbumButon = findViewById(R.id.renameAlbumButon);
        deleteAlbumButon = findViewById(R.id.deleteAlbumButon);
        tagSearchButon = findViewById(R.id.tagSearchButon);

        tagDropdown1 = findViewById(R.id.tagDropdown1);
        conjunctionBox = findViewById(R.id.conjunctionBox);
        tagDropdown2 = findViewById(R.id.tagDropdown2);
        valueField1 = findViewById(R.id.valueField1);
        valueField2 = findViewById(R.id.valueField2);

        // Set onClick Listeners
        openAlbumButon.setOnClickListener(view -> openAlbum());
        createAlbumButon.setOnClickListener(view -> createAlbum());
        renameAlbumButon.setOnClickListener(view -> renameAlbum());
        deleteAlbumButon.setOnClickListener(view -> deleteAlbum());
        tagSearchButon.setOnClickListener(view -> tagSearch());

        // Set Spinner values
        ArrayAdapter<String> tag1Adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"--Tag 1--", "Person", "Location"});
        ArrayAdapter<String> tag2Adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"--Tag 2--", "Person", "Location"});
        ArrayAdapter<String> conjunctionAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"...", "And", "Or"});
        tagDropdown1.setAdapter(tag1Adapter);
        tagDropdown2.setAdapter(tag2Adapter);
        conjunctionBox.setAdapter(conjunctionAdapter);

        // Disable buttons
        openAlbumButon.setEnabled(false);
        renameAlbumButon.setEnabled(false);
        deleteAlbumButon.setEnabled(false);
    }

    private void select(View albumThumbnail) {
        // User clicked on currently selected thumbnail - simply deselect
        if (curSelected != null && ((Album) curSelected.getTag()).equals((Album) albumThumbnail.getTag())) {
            deselect();
            return;
        }

        deselect();
        curSelected = albumThumbnail;
        curSelected.setBackgroundResource(R.drawable.selection_background);

        openAlbumButon.setEnabled(true);
        renameAlbumButon.setEnabled(true);
        deleteAlbumButon.setEnabled(true);
    }

    private void deselect() {
        if (curSelected == null)
            return;
        curSelected.setBackgroundResource(android.R.color.transparent);
        curSelected = null;

        openAlbumButon.setEnabled(false);
        renameAlbumButon.setEnabled(false);
        deleteAlbumButon.setEnabled(false);
    }

    // Listener for open album button
    private void openAlbum() {
        openAlbum((Album) curSelected.getTag(), true);
    }

    private void openAlbum(Album album, boolean notTemporary) {

    }

    // Listener for create album button
    private void createAlbum() {
        createAlbumDialog().show();
    }

    private AlertDialog createAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.album_name_dialog, null);
        EditText albumField = dialogView.findViewById(R.id.albumNameTextField);

        builder.setView(dialogView)
                .setNegativeButton("Cancel", (dialog, id) -> {})
                .setPositiveButton("OK", (dialog, id) -> {
                    // Input validation
                    String albumName = albumField.getText().toString();
                    if (albumName.isEmpty()) {
                        Toast toast = Toast.makeText(this, "Enter a non-empty name.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    if (albumExists(albumName)) {
                        Toast toast = Toast.makeText(this, "Album with that name already exists.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    // Create album and View for album thumbnail
                    Album newAlbum = new Album(albumName);
                    View albumThumbnailView = createAlbumThumbnailView(newAlbum);
                    albumScrollContainer.addView(albumThumbnailView);
                });
        return builder.create();
    }

    // Listener for rename album button
    private void renameAlbum() {
        renameAlbumDialog().show();
    }

    private AlertDialog renameAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.album_name_dialog, null);
        EditText albumField = dialogView.findViewById(R.id.albumNameTextField);

        builder.setView(dialogView)
                .setNegativeButton("Cancel", (dialog, id) -> {})
                .setPositiveButton("OK", (dialog, id) -> {
                    String newName = albumField.getText().toString();
                    // Input validation
                    if (newName.isEmpty()) {
                        Toast toast = Toast.makeText(this, "Enter a non-empty name.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    if (albumExists(newName)) {
                        Toast toast = Toast.makeText(this, "Album with that name already exists.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    // Rename album
                    ((Album) curSelected.getTag()).setName(newName);
                    TextView titleText = curSelected.findViewById(R.id.titleText);
                    titleText.setText(newName);
                });
        return builder.create();
    }

    // Listener for delete album button
    private void deleteAlbum() {
        albumScrollContainer.removeView(curSelected);
        deselect();
    }

    // Listener for tag search button
    private void tagSearch() {
        String type1 = tagDropdown1.getSelectedItem().toString();
        String value1 = valueField1.getText().toString();
        String conjunction = conjunctionBox.getSelectedItem().toString();
        String type2 = tagDropdown2.getSelectedItem().toString();
        String value2 = valueField2.getText().toString();

        Album album = tagSearch(type1, value1, conjunction, type2, value2);
        if (album == null) {
            Toast toast = Toast.makeText(this, "Select a proper tag-value combination.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        openAlbum(album, false);
    }

    private Album tagSearch(String type, String value) {
        if (type.equals("--Tag 1--") || type.equals("--Tag 2--") || value.isEmpty()) {
            return null;
        }
        HashSet<Photo> albumPhotos = new HashSet<>();
        Tag tag = new Tag(type, value);
        ArrayList<Photo> photos = new ArrayList<>();
        for (int i = 0; i < albumScrollContainer.getChildCount(); i++) {
            Album album = (Album) albumScrollContainer.getChildAt(i).getTag();
            photos.addAll(album.getPhotos());
        }

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

        return new Album(new ArrayList<>(albumPhotos), "Unnamed album");
    }

    private Album tagSearch(String type1, String value1, String conjunction, String type2, String value2) {
        Album album1 = tagSearch(type1, value1);
        if (conjunction.equals("...") || album1 == null) {
            return album1;
        } else {
            Album album2 = tagSearch(type2, value2);
            ArrayList<Photo> album1Photos = album1.getPhotos();
            ArrayList<Photo> album2Photos = album2.getPhotos();
            ArrayList<Photo> albumPhotos = new ArrayList<>();

            if (conjunction.equals("And")) {
                for (Photo photo: album1Photos) {
                    if (album2Photos.contains(photo)) {
                        albumPhotos.add(photo);
                    }
                }
                return trimDuplicates(new Album(albumPhotos, "Unnamed Album"));
            } else if (conjunction.equals("Or")) {
                for (Photo photo: album2Photos) {
                    album1Photos.add(photo);
                }
                return trimDuplicates(new Album(album1Photos, "Unnamed Album"));
            } else {
                System.out.println("invalid conjunction");
                return null;
            }
        }
    }

    // Helper to create album thumbnail View for album
    private View createAlbumThumbnailView(Album album) {
        View albumThumbnailView = getLayoutInflater().inflate(R.layout.album_thumbnail, albumScrollContainer, false); // creates View object from XML design

        ImageView imageView = albumThumbnailView.findViewById(R.id.imageView);
        TextView titleText = albumThumbnailView.findViewById(R.id.titleText);
        titleText.setText(album.getName());
        if (album.getPhotos().isEmpty())
            imageView.setImageResource(R.drawable.image_not_found);
        else
            imageView.setImageURI(album.getPhotos().get(0).getUri());

        albumThumbnailView.setTag(album);
        albumThumbnailView.setOnClickListener(this::select);
        return albumThumbnailView;
    }

    // Check if album exists with given albumName
    private boolean albumExists(String albumName) {
        for (int i = 0; i < albumScrollContainer.getChildCount(); i++) {
            View albumThumbnail = albumScrollContainer.getChildAt(i);
            if (((Album) albumThumbnail.getTag()).getName().equals(albumName))
                return true;
        }
        return false;
    }

    private Album trimDuplicates(Album album) {
        ArrayList<Photo> photos = album.getPhotos();
        if (photos.size() <= 1) {
            return album;
        }
        photos.sort(Comparator.comparing(Photo::getUri));
        ArrayList<Photo> albumPhotos = new ArrayList<>();
        albumPhotos.add(photos.get(0));
        for (int i = 1; i < photos.size(); i++) {
            if (photos.get(i).getUri().compareTo(photos.get(i-1).getUri()) != 0) {
                albumPhotos.add(photos.get(i));
            }
        }
        return new Album(albumPhotos, album.getName());
    }
}