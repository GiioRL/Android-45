package com.example.android_45;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout albumScrollContainer;

    private Button openAlbumButon, createAlbumButon, renameAlbumButon, deleteAlbumButon, tagSearchButon, dateSearchButon;

    private Spinner tagDropdown1, conjunctionBox, tagDropdown2, valueDropdown1, valueDropdown2;

    // Date fields
    private EditText fromDate, toDate;

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
        dateSearchButon = findViewById(R.id.dateSearchButon);

        tagDropdown1 = findViewById(R.id.tagDropdown1);
        conjunctionBox = findViewById(R.id.conjunctionBox);
        tagDropdown2 = findViewById(R.id.tagDropdown2);
        valueDropdown1 = findViewById(R.id.valueDropdown1);
        valueDropdown2 = findViewById(R.id.valueDropdown2);

        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);

        // Set onClick Listeners
        openAlbumButon.setOnClickListener(view -> openAlbum());
        createAlbumButon.setOnClickListener(view -> createAlbum());
        renameAlbumButon.setOnClickListener(view -> renameAlbum());
        deleteAlbumButon.setOnClickListener(view -> deleteAlbum());
        tagSearchButon.setOnClickListener(view -> tagSearch());
        dateSearchButon.setOnClickListener(view -> dateSearch());

        // Set Spinner values
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"Person", "Location"});
        ArrayAdapter<String> conjunctionAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"And", "Or"});
        tagDropdown1.setAdapter(tagAdapter);
        tagDropdown2.setAdapter(tagAdapter);
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

    private void openAlbum() {

    }

    private void createAlbum() {
        Log.d("DEBUG", "in createAlbum()");
        createAlbumDialog().show();
    }

    private AlertDialog createAlbumDialog() {
        Log.d("DEBUG", "in createAlbumDialog()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.create_album_dialog, null);
        EditText albumField = dialogView.findViewById(R.id.albumNameTextField);

        builder.setView(dialogView)
                .setNegativeButton("Cancel", (dialog, id) -> {})
                .setPositiveButton("OK", (dialog, id) -> {
                    Log.d("DEBUG", "in setPositiveButton()");
                    String albumName = albumField.getText().toString();
                    Album newAlbum = new Album(albumName);
                    View albumThumbnailView = createAlbumThumbnailView(newAlbum);
                    albumScrollContainer.addView(albumThumbnailView);
                });
        return builder.create();
    }

    // Helper to create View for album thumbnail
    private View createAlbumThumbnailView(Album album) {
        Log.d("DEBUG", "in createAlbumThumbnailView()");
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

    private void renameAlbum() {

    }

    private void deleteAlbum() {

    }

    private void tagSearch() {

    }

    private void dateSearch() {

    }
}