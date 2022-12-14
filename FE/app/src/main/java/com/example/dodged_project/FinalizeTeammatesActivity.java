package com.example.dodged_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dodged_project.databinding.ActivityFinalizeTeammatesBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FinalizeTeammatesActivity extends AppCompatActivity implements PlayerUsernamesFragment.Callbacks {

//    private ActivityFinalizeTeammatesBinding binding;
    private String[] usernames = {"", "", "", "", ""};
    private String selectedRegion;

//    private Button confirmButton;
//    private Button uploadPhotoButton;
//    private TextView userLoggedInStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        ActivityFinalizeTeammatesBinding binding;

        Button confirmButton;
        Button uploadPhotoButton;
        TextView userLoggedInStatusText;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_finalize_teammates);

        Bundle finalizeTeammatesActivityBundle = getIntent().getExtras();
        usernames = finalizeTeammatesActivityBundle.getStringArray("user_input_player_names");

        String[] regions = new String[] {"NA1", "BR1", "EUN1", "EUW1", "JP1", "KR", "LA1", "LA2", "OC1", "RU", "TR1"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(FinalizeTeammatesActivity.this, R.layout.region_dropdown_item, regions);
        binding.regionDropdownItem.setAdapter(adapter);

        binding.regionDropdownItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRegion = (String) parent.getItemAtPosition(position);
            }
        });

        userLoggedInStatusText = findViewById(R.id.userLoggedInStatus);

        if(MainActivity.googleAccountName == null) {
            userLoggedInStatusText.setText("Not logged in");
        } else {
            userLoggedInStatusText.setText("Logged in as: " + MainActivity.googleAccountName);
        }


        binding.addTeammatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTeammatesIntent = new Intent(FinalizeTeammatesActivity.this, AddTeammatesActivity.class);
                addTeammatesIntent.putExtra("user_input_player_names", usernames);
                addTeammatesIntent.putExtra("confirmedTeammatesBefore", true);
                startActivity(addTeammatesIntent);
            }
        });

        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUsernames(usernames, selectedRegion);
            }
        });

        uploadPhotoButton = findViewById(R.id.upload_photo_button);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadPhotoIntent = new Intent(FinalizeTeammatesActivity.this, ChooseTeammatesActivity.class);
                startActivity(uploadPhotoIntent);
            }
        });

        //check if on FinalizeTeammates screen
        finalizeTeammatesActivityBundle.putString("activity", "FinalizeTeammatesActivity");

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.player_usernames_fragment);

        if (fragment == null) {
            fragment = new PlayerUsernamesFragment(FinalizeTeammatesActivity.this);
            fragment.setArguments(finalizeTeammatesActivityBundle);
            fm.beginTransaction().add(R.id.player_usernames_fragment, fragment).commit();
        }

    }

    @Override
    public void getPlayersFromPlayerUsernamesFragment(String[] playerUsernames) {
        this.usernames = Arrays.copyOf(playerUsernames,playerUsernames.length);
    }

    public void sendUsernames(String[] playerUsenames, String region) {
        String sendUsernamesEndpoint = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/image/usernames";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("region", region);
            jsonObject.put("id1", playerUsenames[0]);
            jsonObject.put("id2", playerUsenames[1]);
            jsonObject.put("id3", playerUsenames[2]);
            jsonObject.put("id4", playerUsenames[3]);
            jsonObject.put("id5", playerUsenames[4]);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, sendUsernamesEndpoint, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(FinalizeTeammatesActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        Intent resultsActivityIntent = new Intent(FinalizeTeammatesActivity.this, ResultsActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("response", response.toString());

                        resultsActivityIntent.putExtras(bundle);
                        startActivity(resultsActivityIntent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FinalizeTeammatesActivity.this, "Error: Make sure ALL Usernames are correct and a Region is selected", Toast.LENGTH_LONG).show();
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
}