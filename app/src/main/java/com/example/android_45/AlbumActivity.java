package com.example.android_45;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AlbumActivity extends AppCompatActivity {

    private LinearLayout photoScrollContainer;

    private Button displayPhotoButon, addPhotoButon, movePhotoButon, removePhotoButon;

    private Album album;
    private View curSelected = null;

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

        // Action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Test");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addPhoto() {

    }

    private void displayPhoto() {

    }

    private void movePhoto() {

    }

    private void removePhoto() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}