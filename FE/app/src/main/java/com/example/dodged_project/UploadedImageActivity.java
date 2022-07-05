package com.example.dodged_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

public class UploadedImageActivity extends AppCompatActivity {

    private ImageView uploadedImage;
    private Button confirmButton;
    private Button cancelButton;

    private TextView userLoggedInStatusText;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_image);

        userLoggedInStatusText = findViewById(R.id.userLoggedInStatus);

        String userLoggedInStatus = getIntent().getStringExtra("USER_ACCOUNT_INFO");

        if(userLoggedInStatus == null) {
            userLoggedInStatusText.setText("Not logged in");
        } else {
            userLoggedInStatusText.setText("Logged in as: " + userLoggedInStatus);
        }

        Bitmap imageBitmap = (Bitmap) getIntent().getExtras().get("imageBitmap");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(bArray, Base64.DEFAULT);

        Log.d("UploadedImageActivity", encodedImage);

        uploadedImage = findViewById(R.id.uploadedImage);
        uploadedImage.setImageBitmap(imageBitmap);

        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make a POST request to the BE then make a GET request? Or wait until data or something idk
                sendImage(encodedImage);
                // then start results intent
                Intent resultsIntent = new Intent(UploadedImageActivity.this, ResultsActivity.class);
                resultsIntent.putExtra("USER_ACCOUNT_INFO", userLoggedInStatus);
                startActivity(resultsIntent);
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelFromUploadedImageActivityIntent = new Intent(UploadedImageActivity.this, ChooseTeammatesActivity.class);
                cancelFromUploadedImageActivityIntent.putExtra("USER_ACCOUNT_INFO", userLoggedInStatus);
                startActivity(cancelFromUploadedImageActivityIntent);
            }
        });
    }

    public void sendImage(String encodedImage) {
        // send image bitmap to the BE
        RequestQueue queue = Volley.newRequestQueue(this);
        String sendImageEndpoint = "ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/image";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sendImageEndpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadedImageActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();

                params.put("encodedImage", encodedImage);
                return params;
            }
        };
        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backToChooseTeammates = new Intent(UploadedImageActivity.this, ChooseTeammatesActivity.class);
        startActivity(backToChooseTeammates);
    }
}