package com.example.dodged_project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChooseTeammatesActivity extends AppCompatActivity {

//    private Button addTeammatesButton;
//    private Button uploadPhotoButton;
//    private TextView userLoggedInStatusText;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    File testFile = null;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_choose_teammates);

        Button addTeammatesButton;
        Button uploadPhotoButton;
        TextView userLoggedInStatusText;

        userLoggedInStatusText = findViewById(R.id.userLoggedInStatus);

        if(MainActivity.googleAccountName == null) {
            userLoggedInStatusText.setText("Not logged in");
        } else {
            userLoggedInStatusText.setText("Logged in as: " + MainActivity.googleAccountName);
        }

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
                    String fileName = "photo";
                    File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    try {
                        File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                        currentPhotoPath = imageFile.getAbsolutePath();
                        Uri imageUri = FileProvider.getUriForFile(ChooseTeammatesActivity.this, "com.example.dodged_project.fileprovider", imageFile);
                        Intent uploadImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        uploadImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(uploadImageIntent, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    Intent uploadImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    uploadImageIntentLauncher.launch(uploadImageIntent);
//                    dispatchTakePictureIntent();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == 0) {

            String fileName = "photo";
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                currentPhotoPath = imageFile.getAbsolutePath();
                Uri imageUri = FileProvider.getUriForFile(ChooseTeammatesActivity.this, "com.example.dodged_project.fileprovider", imageFile);
                Intent uploadImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                uploadImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(uploadImageIntent, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Intent uploadImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            uploadImageIntentLauncher.launch(uploadImageIntent);
//            dispatchTakePictureIntent();
//            Intent uploadedImageActivityIntent = new Intent(ChooseTeammatesActivity.this, UploadedImageActivity.class);
//            uploadedImageActivityIntent.putExtra("imageBitmap", bitmap);
//            startActivity(uploadedImageActivityIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bArray = byteArrayOutputStream.toByteArray();
            String encodedImageFromCamera = Base64.encodeToString(bArray, Base64.DEFAULT);

            Log.d("ChooseActivity", String.valueOf(bitmap));
            Intent uploadedImageActivityIntent = new Intent(ChooseTeammatesActivity.this, UploadedImageActivity.class);
            uploadedImageActivityIntent.putExtra("encodedImage", encodedImageFromCamera);
            startActivity(uploadedImageActivityIntent);
        }
    }

    //    ActivityResultLauncher<Intent> uploadImageIntentLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//
//                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                        Intent uploadedImageActivityIntent = new Intent(ChooseTeammatesActivity.this, UploadedImageActivity.class);
//                        uploadedImageActivityIntent.putExtra("imageBitmap", bitmap);
//                        startActivity(uploadedImageActivityIntent);
//                    }
//                }
//            }
//    );

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToMainIntent = new Intent(ChooseTeammatesActivity.this, MainActivity.class);
        startActivity(backToMainIntent);
    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        return image;
//    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
////                Log.d("ChooseActivity", String.valueOf(testFile));
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.dodged_project.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }
}