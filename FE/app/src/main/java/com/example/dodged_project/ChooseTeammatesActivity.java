package com.example.dodged_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ChooseTeammatesActivity extends AppCompatActivity {

    private Button addTeammatesButton;
    private Button uploadPhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_teammates);

        addTeammatesButton = findViewById(R.id.add_teammates_button);
        addTeammatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTeammatesIntent = new Intent(ChooseTeammatesActivity.this, AddTeammatesActivity.class);
                startActivity(addTeammatesIntent);
            }
        });

        uploadPhotoButton = findViewById(R.id.upload_photo_button);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request camera runtime permissions
                if(ContextCompat.checkSelfPermission(ChooseTeammatesActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChooseTeammatesActivity.this, new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
                } else {
                    Intent uploadImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(uploadImageIntent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ChooseTeammatesActivity", String.valueOf(requestCode));
        if(requestCode == 100) {
            //for now just log a bitmap
            // later we need to implement the Photo Upload Activity and show a small preview of the image
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Log.d("ChooseTeammatesActivity", bitmap.toString());
        }
    }
}