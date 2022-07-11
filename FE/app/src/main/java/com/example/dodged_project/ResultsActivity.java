package com.example.dodged_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dodged_project.data.Player;
import com.example.dodged_project.databinding.ActivityFinalizeTeammatesBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity implements PlayerUsernamesFragment.Callbacks{

    private TextView resultsText;
    private TextView resultsDescription;
    private String[] usernames = {"", "", "", "", ""};
    private int[] likes = {0, 0, 0, 0, 0};
    private int[] dislikes = {0, 0, 0, 0, 0};
    private JSONObject[] stats;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;

    private ActivityFinalizeTeammatesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_results);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_results);

        Bundle bundle = getIntent().getExtras();

        String predictionData = bundle.getString("response");
        Bundle fragmentBundle = new Bundle();

        JSONObject data = new JSONObject();
        double prediction = 0;

        try {
            data = new JSONObject(predictionData);
            prediction = data.getDouble("prediction");

            setPlayerData(data);
            setUsernames();
            setLikes();
            setDislikes();
//            setStats();

            fragmentBundle.putStringArray("user_input_player_names", usernames);
            fragmentBundle.putString("activity", "ResultsActivity");
            fragmentBundle.putIntArray("user_input_player_likes", likes);
            fragmentBundle.putIntArray("user_input_player_dislikes", dislikes);
//            fragmentBundle.putString("user_input_player_stats", stats.toString());

            Log.d("ResultsActivity", data.toString());
            Log.d("ResultsActivity", String.valueOf(player3.getLikes()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get some boolean or win rate from the BE
        // temp results text checker

        resultsText = findViewById(R.id.resultsText);
        resultsDescription = findViewById(R.id.resultsDescription);

        if(prediction > 0.50) {
            resultsText.setText(R.string.results_play);
            resultsDescription.setText(R.string.high_odds_of_winning_text);
        } else {
            resultsText.setText(R.string.results_dodge);
            resultsDescription.setText(R.string.low_odds_of_winning_text);
        }


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.player_usernames_fragment);

        if (fragment == null) {
            fragment = new PlayerUsernamesFragment(ResultsActivity.this);
            fragment.setArguments(fragmentBundle);
            fm.beginTransaction().add(R.id.player_usernames_fragment, fragment).commit();
        }

    }

    private void setPlayerData(JSONObject data) throws JSONException {
        player1 = new Player(
                data.getJSONObject("player1").getString("name"),
                data.getJSONObject("player1").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player1").getJSONObject("reviews").getInt("dislikes")
//                data.getJSONObject("player1").getJSONObject("stats")
        );

        player2 = new Player(
                data.getJSONObject("player2").getString("name"),
                data.getJSONObject("player2").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player2").getJSONObject("reviews").getInt("dislikes")
//                data.getJSONObject("player2").getJSONObject("stats")
        );

        player3 = new Player(
                data.getJSONObject("player3").getString("name"),
                data.getJSONObject("player3").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player3").getJSONObject("reviews").getInt("dislikes")
//                data.getJSONObject("player3").getJSONObject("stats")
        );

        player4 = new Player(
                data.getJSONObject("player4").getString("name"),
                data.getJSONObject("player4").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player4").getJSONObject("reviews").getInt("dislikes")
//                data.getJSONObject("player4").getJSONObject("stats")
        );

        player5 = new Player(
                data.getJSONObject("player5").getString("name"),
                data.getJSONObject("player5").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player5").getJSONObject("reviews").getInt("dislikes")
//                data.getJSONObject("player5").getJSONObject("stats")
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backToChooseTeammates = new Intent(ResultsActivity.this, ChooseTeammatesActivity.class);
        startActivity(backToChooseTeammates);
    }

    @Override
    public void getPlayersFromPlayerUsernamesFragment(String[] playerUsernames) {
        this.usernames = Arrays.copyOf(playerUsernames,playerUsernames.length);
    }

    public void setUsernames() {
        usernames[0] = player1.getUsername();
        usernames[1] = player2.getUsername();
        usernames[2] = player3.getUsername();
        usernames[3] = player4.getUsername();
        usernames[4] = player5.getUsername();
    }

    public void setLikes() {
        likes[0] = player1.getLikes();
        likes[1] = player2.getLikes();
        likes[2] = player3.getLikes();
        likes[3] = player4.getLikes();
        likes[4] = player5.getLikes();
    }

    public void setDislikes() {
        dislikes[0] = player1.getDislikes();
        dislikes[1] = player2.getDislikes();
        dislikes[2] = player3.getDislikes();
        dislikes[3] = player4.getDislikes();
        dislikes[4] = player5.getDislikes();
    }

//    public void setStats() {
//        stats[0] = player1.getStats();
//        stats[1] = player2.getStats();
//        stats[2] = player3.getStats();
//        stats[3] = player4.getStats();
//        stats[4] = player5.getStats();
//    }

    public void setUserLikes(String userName) {
        String sendUsernamesEndpoint = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/playerdb/like";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", userName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, sendUsernamesEndpoint, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(ResultsActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        // on Response add 1 to the like counter
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResultsActivity.this, "Error: Hmm something went wrong while trying to like this user profile", Toast.LENGTH_LONG).show();
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

    public void setUserDislikes(String userName) {

    }
}