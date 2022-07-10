package com.example.dodged_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.dodged_project.data.Player;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ResultsActivity extends AppCompatActivity implements PlayerUsernamesFragment.Callbacks{

    private TextView resultsText;
    private TextView resultsDescription;
    private String[] usernames = {"", "", "", "", ""};

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_results);

        Bundle bundle = getIntent().getExtras();

//        Log.d("ResultsActivity", bundle.toString());
//
//
//
//        String userLoggedInStatus;
//
//        if(bundle != null && bundle.getString("USER_ACCOUNT_INFO_FROM_IMAGE_ACTIVITY") != null) {
//            userLoggedInStatus = bundle.getString("USER_ACCOUNT_INFO_FROM_IMAGE_ACTIVITY");
//        } else if(bundle != null & bundle.getString("USER_ACCOUNT_INFO_FROM_FINALIZE_ACTIVITY") != null) {
//            userLoggedInStatus = bundle.getString("USER_ACCOUNT_INFO_FROM_FINALIZE_ACTIVITY");
//        }
//
        String predictionData = bundle.getString("response");
        Bundle fragmentBundle = new Bundle();

        JSONObject data = new JSONObject();
        double prediction = 0;

////
        try {
            data = new JSONObject(predictionData);
            prediction = data.getDouble("prediction");

            setPlayerData(data);

            usernames[0] = player1.getUsername();
            usernames[1] = player2.getUsername();
            usernames[2] = player3.getUsername();
            usernames[3] = player4.getUsername();
            usernames[4] = player5.getUsername();

            fragmentBundle.putStringArray("user_input_player_names", usernames);

            Log.d("ResultsActivity", data.toString());
            Log.d("ResultsActivity", player1.getUsername());

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        {
//            prediction: double,
//            player1: {
//                likes: int,
//                dislikes: int,
//                comments: String[]
//                _id: String (just the name),
//            }
//            player2: {
//                ...
//            }
//        }



        // get some boolean or win rate from the BE
        // temp results text checker
//        boolean tempRemoveLater = true;

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
//
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
        );

        player2 = new Player(
                data.getJSONObject("player2").getString("name"),
                data.getJSONObject("player2").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player2").getJSONObject("reviews").getInt("dislikes")
        );

        player3 = new Player(
                data.getJSONObject("player3").getString("name"),
                data.getJSONObject("player3").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player3").getJSONObject("reviews").getInt("dislikes")
        );

        player4 = new Player(
                data.getJSONObject("player4").getString("name"),
                data.getJSONObject("player4").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player4").getJSONObject("reviews").getInt("dislikes")
        );

        player5 = new Player(
                data.getJSONObject("player5").getString("name"),
                data.getJSONObject("player5").getJSONObject("reviews").getInt("likes"),
                data.getJSONObject("player5").getJSONObject("reviews").getInt("dislikes")
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
}