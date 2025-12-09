package com.example.android_45;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private LinearLayout photoScrollContainer;

    private Button displayPhotoButon, addPhotoButon, movePhotoButon, removePhotoButon;

    private Album album;
    private View curSelected = null;
    private boolean notTemporary;
    private static final int REQUEST_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_album);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Match XML elements to Java objects
        photoScrollContainer = findViewById(R.id.photoScrollContainer);

        displayPhotoButon = findViewById(R.id.displayPhotoButon);
        addPhotoButon = findViewById(R.id.addPhotoButon);
        movePhotoButon = findViewById(R.id.movePhotoButon);
        removePhotoButon = findViewById(R.id.removePhotoButon);

        // Set onClick Listeners
        displayPhotoButon.setOnClickListener(view -> displayPhoto());
        addPhotoButon.setOnClickListener(view -> addPhoto());
        movePhotoButon.setOnClickListener(view -> movePhoto());
        removePhotoButon.setOnClickListener(view -> removePhoto());

        // Disable buttons
        displayPhotoButon.setEnabled(false);
        movePhotoButon.setEnabled(false);
        removePhotoButon.setEnabled(false);

        // Get data from Main Activity
        album = getIntent().getSerializableExtra("Album", Album.class);
        notTemporary = getIntent().getBooleanExtra("notTemporary", true);

        // Set up photo thumbnails
        setupPhotoThumbnails(album.getPhotos());

        // Delete buttons if album is temporary
        if (!notTemporary) {
            ((ViewGroup) displayPhotoButon.getParent()).removeView(displayPhotoButon);
            ((ViewGroup) addPhotoButon.getParent()).removeView(addPhotoButon);
            ((ViewGroup) movePhotoButon.getParent()).removeView(movePhotoButon);
            ((ViewGroup) removePhotoButon.getParent()).removeView(removePhotoButon);
        }

        // Action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(album.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void select(View photoThumbnail) {
        // User clicked on currently selected thumbnail - simply deselect
        if (curSelected != null && ((Photo) curSelected.getTag()).equals((Photo) photoThumbnail.getTag())) {
            deselect();
            return;
        }

        deselect();
        curSelected = photoThumbnail;
        curSelected.setBackgroundResource(R.drawable.selection_background);

        if (notTemporary) {
            displayPhotoButon.setEnabled(true);
            movePhotoButon.setEnabled(true);
            removePhotoButon.setEnabled(true);
        }
    }

    private void deselect() {
        if (curSelected == null)
            return;
        curSelected.setBackgroundResource(android.R.color.transparent);
        curSelected = null;

        if (notTemporary) {
            displayPhotoButon.setEnabled(false);
            movePhotoButon.setEnabled(false);
            removePhotoButon.setEnabled(false);
        }
    }

    // Listener for add album button
    private void addPhoto() {

    }

    // Listener for display photo button
    private void displayPhoto() {

    }

    // Listener for move photo button
    private void movePhoto() {

    }

    // Listener for remove photo button
    private void removePhoto() {

    }

    // Set up photo thumbnails when launching activity
    private void setupPhotoThumbnails(ArrayList<Photo> photos) {
        if (photos == null)
            return;

        int num = photos.size();
        for (int i = 0; i < (num+2)/3; i++) {
            LinearLayout photoBox = createPhotoBox();
            for (int j = 0; j < 3; j++) {
                if (3*i + j >= num) {
                    View photoThumbnail = createPhotoThumbnailView(photos.get(3*i + j));
                    photoBox.addView(photoThumbnail);
                }
            }
            photoScrollContainer.addView(photoBox);
        }
    }

    // Helper to create album thumbnail View for album
    private View createPhotoThumbnailView(Photo photo) {
        View photoThumbnail = getLayoutInflater().inflate(R.layout.photo_thumbnail, photoScrollContainer, false); // creates View object from XML design

        ImageView imageView = photoThumbnail.findViewById(R.id.imageView);
        TextView captionLabel = photoThumbnail.findViewById(R.id.captionLabel);
        captionLabel.setText(photo.getName());
        imageView.setImageURI(photo.getUri());

        photoThumbnail.setTag(photo);
        photoThumbnail.setOnClickListener(this::select);
        return photoThumbnail;
    }

    // Helper to create new photoBox
    private LinearLayout createPhotoBox() {
        LinearLayout photoBox = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        photoBox.setLayoutParams(params);
        photoBox.setOrientation(LinearLayout.HORIZONTAL);
        return photoBox;
    }

    // User pressed back button
    @Override
    public boolean onSupportNavigateUp() {
        deselect();
        finish();
        return true;
    }
}