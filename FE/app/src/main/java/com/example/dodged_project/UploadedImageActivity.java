package com.example.dodged_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UploadedImageActivity extends AppCompatActivity {

//    private ImageView uploadedImage;
//    private Button confirmButton;
//    private Button cancelButton;
//
//    private TextView userLoggedInStatusText;
//    private String selectedRegion;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_uploaded_image);

        ImageView uploadedImage;
        Button confirmButton;
        Button cancelButton;

        TextView userLoggedInStatusText;
        String selectedRegion;

        userLoggedInStatusText = findViewById(R.id.userLoggedInStatus);

        if(MainActivity.googleAccountName == null) {
            userLoggedInStatusText.setText("Not logged in");
        } else {
            userLoggedInStatusText.setText("Logged in as: " + MainActivity.googleAccountName);
        }

//        Bitmap imageBitmap = (Bitmap) getIntent().getExtras().get("imageBitmap");
        String encodedImageFromCamera = getIntent().getExtras().getString("encodedImage");

//        Log.d("UploadedImageActivity", String.valueOf(imageBitmap));

//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        byte[] bArray = byteArrayOutputStream.toByteArray();
//        String encodedImageFromCamera = Base64.encodeToString(bArray, Base64.DEFAULT);

        // FOR M5, USE A DEFAULT IMAGE SINCE WE ARE USING AN EMULATOR
//        String defaultImagePath = "../assets/defaultUploadImage.jpg";
//        Bitmap defaultImageBitmap = BitmapFactory.decodeFile(defaultImagePath);

//        Bitmap defaultImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_upload_image);
//        Bitmap scaled = Bitmap.createScaledBitmap(defaultImageBitmap, 150, 100, true);
//
//        uploadedImage = findViewById(R.id.uploadedImage);
//        uploadedImage.setImageBitmap(imageBitmap);
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        defaultImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//        byte[] bArray = byteArrayOutputStream.toByteArray();
//        String encodedImage = Base64.encodeToString(bArray, Base64.DEFAULT);

        // TEMP ADD REGION DROPDOWN
        selectedRegion = "NA1";

        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make a POST request to the BE
                sendImage(encodedImageFromCamera, selectedRegion);
                // then start results intent
//                Intent resultsIntent = new Intent(UploadedImageActivity.this, ResultsActivity.class);
//                startActivity(resultsIntent);
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelFromUploadedImageActivityIntent = new Intent(UploadedImageActivity.this, ChooseTeammatesActivity.class);
                startActivity(cancelFromUploadedImageActivityIntent);
            }
        });
    }

    public void sendImage(String encodedImage, String region) {
        String sendUsernamesEndpoint = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/image";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("region", region);
            jsonObject.put("base64EncodedImage", encodedImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, sendUsernamesEndpoint, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(UploadedImageActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        Intent resultsIntent = new Intent(UploadedImageActivity.this, ResultsActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("response", response.toString());

                        resultsIntent.putExtras(bundle);
                        startActivity(resultsIntent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadedImageActivity.this, "Sorry, your image may not be clear enough", Toast.LENGTH_LONG).show();
                    }
             }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backToChooseTeammates = new Intent(UploadedImageActivity.this, ChooseTeammatesActivity.class);
        startActivity(backToChooseTeammates);
    }
}