package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class UploadedImageActivity extends AppCompatActivity {

    ImageView uploadedImage;
    Button confirmButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_image);

        Bitmap imageBitmap = (Bitmap) getIntent().getExtras().get("imageBitmap");

        uploadedImage = findViewById(R.id.uploadedImage);
        uploadedImage.setImageBitmap(imageBitmap);

        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelFromUploadedImageActivityIntent = new Intent(UploadedImageActivity.this, ChooseTeammatesActivity.class);
                startActivity(cancelFromUploadedImageActivityIntent);
            }
        });
    }
}