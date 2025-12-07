package com.example.android_45;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    LinearLayout albumScrollContainer;

    Button openAlbumButon, createAlbumButon, renameAlbumButon, deleteAlbumButon, tagSearchButon, dateSearchButon;

    Spinner tagDropdown1, conjunctionBox, tagDropdown2, valueDropdown1, valueDropdown2;

    // Date fields
    EditText fromDate, toDate;

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

//        Match XML elements to Java objects
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

//        Set onClick Listeners
        openAlbumButon.setOnClickListener(view -> openAlbum());
        createAlbumButon.setOnClickListener(view -> createAlbum());
        renameAlbumButon.setOnClickListener(view -> renameAlbum());
        deleteAlbumButon.setOnClickListener(view -> deleteAlbum());
        tagSearchButon.setOnClickListener(view -> tagSearch());
        dateSearchButon.setOnClickListener(view -> dateSearch());

//        Set Spinner values
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"Person", "Location"});
        tagDropdown1.setAdapter(tagAdapter);
        tagDropdown2.setAdapter(tagAdapter);
        ArrayAdapter<String> conjunctionAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"And", "Or"});
        conjunctionBox.setAdapter(conjunctionAdapter);
    }

    void openAlbum() {

    }

    void createAlbum() {

    }

    void renameAlbum() {

    }

    void deleteAlbum() {

    }

    void tagSearch() {

    }

    void dateSearch() {

    }
}