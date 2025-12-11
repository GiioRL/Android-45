package com.example.android_45;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class PhotoActivity extends AppCompatActivity {

    private Photo photo;

    private int photoIndex;

    private Album album;

    private ArrayList<Album> albums;

    private int albumIndex;

    private ArrayList<Album> otherAlbums;

    private ImageView imageView;

    private TextView title;

    private TextView tagText;

    private Button leftButton;

    private Button rightButton;

    private Button addTagButon;

    private Button removeTagButon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Match XML elements to Java objects
        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        tagText = findViewById(R.id.tagText);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        addTagButon = findViewById(R.id.addTagButon);
        removeTagButon = findViewById(R.id.removeTagButon);

        // Set onClick Listeners
        leftButton.setOnClickListener(view -> left());
        rightButton.setOnClickListener(view -> right());
        addTagButon.setOnClickListener(view -> addTag());
        removeTagButon.setOnClickListener(view -> removeTag());

        // Get data from Main Activity
        photo = getIntent().getSerializableExtra("Photo", Photo.class);
        album = getIntent().getSerializableExtra("album", Album.class);
        otherAlbums = getIntent().getSerializableExtra("otherAlbums", ArrayList.class);
        albumIndex = getIntent().getIntExtra("albumIndex", -1);

        photoIndex = album.getPhotos().indexOf(photo);

        // Set up scene
        setupScene(photo);

        // Action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(album.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void left() {
        if (photoIndex == 0) {
            return;
        } else {
            setupScene(album.getPhotos().get(--photoIndex));
        }
    }

    private void right() {
        if (photoIndex == (album.getPhotos().size() - 1)) {
            return;
        } else {
            setupScene(album.getPhotos().get(++photoIndex));
        }
    }
    private void addTag() {
        createTagDialog().show();
    }

    private AlertDialog createTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_tag_dialog, null);
        Spinner tagDropdown = dialogView.findViewById(R.id.tagDropdown);
        ArrayAdapter<String> tag1Adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"--Tag Type--", "Person", "Location"});
        tagDropdown.setAdapter(tag1Adapter);
        EditText valueField = dialogView.findViewById(R.id.valueField);

        builder.setView(dialogView)
                .setNegativeButton("Cancel", (dialog, id) -> {})
                .setPositiveButton("OK", (dialog, id) -> {
                    // Input validation
                    String tagType = tagDropdown.getSelectedItem().toString();
                    String tagValue = valueField.getText().toString();
                    if (tagType.equals("--Tag Type--")) {
                        Toast toast = Toast.makeText(this, "Select a tag type.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    if (tagValue.isEmpty()) {
                        Toast toast = Toast.makeText(this, "Enter a non-empty value.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    // Create tag for photo
                    Tag tag = new Tag(tagType, tagValue);
                    if (tagExists(tag)) {
                        Toast toast;
                        if (tagType.equals("Location"))
                            toast = Toast.makeText(this, "Photo already has a location tag.", Toast.LENGTH_LONG);
                        else
                            toast = Toast.makeText(this, "Photo already has that tag.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    album.getPhotos().remove(photo);
                    photo.addTag(tag);
                    album.getPhotos().add(photoIndex, photo);
                    setupScene(photo);
                });

        return builder.create();
    }

    private boolean tagExists(Tag newTag) {
        ArrayList<Tag> tags = photo.getTags();
        if (newTag.getType().equals("Location")) {
            return tags.contains(newTag);
        } else {
            for (Tag tag: tags) {
                if (tag.equals(newTag)) {
                    if (tag.tagEquals(newTag)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    private void removeTag() {}

    private void setupScene(Photo photo) {
        this.photo = photo;
        imageView.setImageURI(photo.getUri());
        title.setText(photo.getName());
        setupTags();
    }

    private void setupTags() {
        ArrayList<Tag> tags = photo.getTags();
        String text = "Tags:";
        if (tags != null) {
            for (Tag tag : tags) {
                text += "\n" + tag.getType() + ": " + tag.getValue();
            }
        }
        tagText.setText(text);
    }

    @Override
    public boolean onSupportNavigateUp() {
        returnUpdatedAlbum();
        finish();
        return true;
    }

    private void returnUpdatedAlbum() {
        Intent result = new Intent();
        result.putExtra("Album", album);
        setResult(RESULT_OK, result);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ArrayList<Album> updatedAlbums = new ArrayList<Album>(otherAlbums);
        updatedAlbums.add(albumIndex, album);
        MainActivity.saveData(this, updatedAlbums);
    }
}