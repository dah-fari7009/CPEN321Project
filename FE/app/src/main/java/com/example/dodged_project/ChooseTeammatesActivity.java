package com.example.dodged_project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChooseTeammatesActivity extends AppCompatActivity {

    private Button addTeammatesButton;
    private Button uploadPhotoButton;
    private TextView userLoggedInStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_teammates);

//        userLoggedInStatusText = findViewById(R.id.userLoggedInStatus);
//
//        String userLoggedInStatus = getIntent().getStringExtra("USER_ACCOUNT_INFO");
//
//        if(userLoggedInStatus == null) {
//            userLoggedInStatusText.setText("Not logged in");
//        } else {
//            userLoggedInStatusText.setText("Logged in as: " + userLoggedInStatus);
//        }

        addTeammatesButton = findViewById(R.id.add_teammates_button);
        addTeammatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTeammatesIntent = new Intent(ChooseTeammatesActivity.this, AddTeammatesActivity.class);
//                addTeammatesIntent.putExtra("USER_ACCOUNT_INFO", userLoggedInStatus);
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
                    uploadImageIntentLauncher.launch(uploadImageIntent);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == 0) {
            Intent uploadImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            uploadImageIntentLauncher.launch(uploadImageIntent);
        }
    }

    ActivityResultLauncher<Intent> uploadImageIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Log.d("ChooseTeammatesActivity", String.valueOf(result.getData().getExtras()));

                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                        String userLoggedInStatus = getIntent().getStringExtra("USER_ACCOUNT_INFO");

                        Intent uploadedImageActivityIntent = new Intent(ChooseTeammatesActivity.this, UploadedImageActivity.class);
                        uploadedImageActivityIntent.putExtra("imageBitmap", bitmap);
//                        uploadedImageActivityIntent.putExtra("USER_ACCOUNT_INFO", userLoggedInStatus);
                        startActivity(uploadedImageActivityIntent);
                    }
                }
            }
    );

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToMainIntent = new Intent(ChooseTeammatesActivity.this, MainActivity.class);
        startActivity(backToMainIntent);
    }
}