package com.example.android_45;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private LinearLayout photoScrollContainer;

    private Button displayPhotoButon, addPhotoButon, movePhotoButon, removePhotoButon;

    private Album album;
    private ArrayList<Album> albums;
    private ArrayList<Album> otherAlbums;
    private int index;
    private View curSelected = null;
    private boolean notTemporary;

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
        albums = getIntent().getSerializableExtra("albums", ArrayList.class);
        notTemporary = getIntent().getBooleanExtra("notTemporary", true);

        if (album == null) {
            Log.d("DEBUG", "null album from intent");
        }

        otherAlbums = (ArrayList<Album>)albums.clone();
        index = otherAlbums.indexOf(album);
        otherAlbums.remove(album);

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
        getUserImage.launch("image/*");
    }

    ActivityResultLauncher<String> getUserImage = registerForActivityResult(new ActivityResultContracts.GetContent(), (Uri uri) -> {
        if (uri == null)
            return;
        String photoName = getNameFromUri(uri);

        File internalFile = new File(getFilesDir(), "photo_" + System.currentTimeMillis() + ".jpg");
        try (InputStream in = getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(internalFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Photo newPhoto = new Photo(internalFile, photoName);
        if (album.getPhotos().contains(newPhoto)) {
            Toast toast = Toast.makeText(this, "Photo already exists in album.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
//        LinearLayout photoBox;
//        if (album.getPhotos().size() % 3 == 0)
//            photoBox = createPhotoBox();
//        else
//            photoBox = (LinearLayout) photoScrollContainer.getChildAt(photoScrollContainer.getChildCount() - 1);
//        View photoThumbnail = createPhotoThumbnailView(newPhoto);
//        photoBox.addView(photoThumbnail);
//        photoScrollContainer.addView(photoBox);
        album.getPhotos().add(newPhoto);
        setupPhotoThumbnails(album.getPhotos());
    });

    // Listener for display photo button
    private void displayPhoto() {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent
                .putExtra("Photo", (Photo) curSelected.getTag())
                .putExtra("album", album)
                .putExtra("otherAlbums", otherAlbums)
                .putExtra("albumIndex", index);
        launchPhoto.launch(intent);
        deselect();
    }

    ActivityResultLauncher<Intent> launchPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() != RESULT_OK)
            return;
        Intent intent = result.getData();
        Photo photo = intent.getSerializableExtra("Photo", Photo.class);
        curSelected.setTag(photo);
    });

    // Listener for move photo button
    private void movePhoto() {
        movePhotoDialog().show();
    }

    private AlertDialog movePhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] albumNames = albums.stream()
                .map(Album::getName)
                .filter(name -> !name.equals(album.getName()))
                .toArray(String[]::new);
        builder.setSingleChoiceItems(albumNames, -1, (dialog, which) -> {
            if (which == -1)
                return;
            Album destAlbum = albums.stream()
                    .filter(a -> a.getName().equals(albumNames[which]))
                    .findFirst()
                    .get();

            Photo photo = (Photo) curSelected.getTag();
            if (destAlbum.getPhotos().contains(photo)) {
                Toast toast = Toast.makeText(this, "Photo already exists in album.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            destAlbum.getPhotos().add(photo);
            removePhoto();
            Toast toast = Toast.makeText(this, "Photo moved to " + destAlbum.getName() + " successfully!", Toast.LENGTH_LONG);
            toast.show();
            dialog.dismiss();
        });
        return builder.create();
    }

    // Listener for remove photo button
    private void removePhoto() {
        album.getPhotos().remove((Photo) curSelected.getTag());
        ((ViewGroup) curSelected.getParent()).removeView(curSelected);
        deselect();
    }

    // Set up photo thumbnails when launching activity
    private void setupPhotoThumbnails(ArrayList<Photo> photos) {
        photoScrollContainer.removeAllViews();
        if (photos == null)
            return;

        int num = photos.size();
        for (int i = 0; i < (num+2)/3; i++) {
            LinearLayout photoBox = createPhotoBox();
            for (int j = 0; j < 3; j++) {
                if (3*i + j < num) {
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

    // Helper to get file name from URI
    private String getNameFromUri(Uri uri) {
        String name = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1)
                    name = cursor.getString(index);
            }
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
        if (name == null)
            Log.d("DEBUG", "Couldn't get name from URI");
        return name;
    }

    // User pressed back button
    @Override
    public boolean onSupportNavigateUp() {
        deselect();
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("DEBUG", "starting albumactivity");
        setupPhotoThumbnails(album.getPhotos());
    }

    @Override
    protected void onPause() {
        super.onPause();
        ArrayList<Album> updatedAlbums = new ArrayList<Album>(otherAlbums);
        updatedAlbums.add(index, album);
        MainActivity.saveData(this, updatedAlbums);
    }
}