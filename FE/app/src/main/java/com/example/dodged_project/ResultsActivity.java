package com.example.dodged_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dodged_project.data.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class ResultsActivity extends AppCompatActivity implements PlayerUsernamesFragment.Callbacks{

//    private TextView resultsText;
//    private TextView resultsDescription;
    private String[] usernames = {"", "", "", "", ""};
    private String[] regions = {"", "", "", "", ""};
    private int[] likes = {0, 0, 0, 0, 0};
    private int[] dislikes = {0, 0, 0, 0, 0};
    private double[] kps = {0, 0, 0, 0, 0};
    private double[] aps = {0, 0, 0, 0, 0};
    private double[] dps = {0, 0, 0, 0, 0};
    private double[] gps = {0, 0, 0, 0, 0};
    private double[] vps = {0, 0, 0, 0, 0};
    private String[][] likedPlayers = {{""},{""}, {""}, {""}, {""}};
    private String[][] dislikedPlayers = {{""},{""}, {""}, {""}, {""}};

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

        TextView resultsText;
        TextView resultsDescription;

        Bundle bundle = getIntent().getExtras();

        String predictionData = bundle.getString("response");
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString("activity", "ResultsActivity");
//        Bundle likedPlayersBundle = new Bundle();

        JSONObject data = new JSONObject();
        double prediction = 0;

        try {
            data = new JSONObject(predictionData);
            prediction = data.getDouble("prediction");

//            Log.d("LOG", String.valueOf(data));
//            Log.d("LOG", data.toString());

//            JSONArray likesList = (JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("likes");
////            JSONArray dislikesList = (JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("dislikes");
//
//            Log.d("LOG", String.valueOf(likesList.length()));

            setPlayerData(data);
            setRegions();
            setUsernames();
            setLikes();
            setDislikes();
            setStats();
            setLikedPlayers();
            setDislikedPlayers();

            fragmentBundle.putStringArray("user_input_player_names", usernames);
            fragmentBundle.putStringArray("user_input_player_regions", regions);
            fragmentBundle.putIntArray("user_input_player_likes", likes);
            fragmentBundle.putIntArray("user_input_player_dislikes", dislikes);
            fragmentBundle.putDoubleArray("user_input_player_kps", kps);
            fragmentBundle.putDoubleArray("user_input_player_aps", aps);
            fragmentBundle.putDoubleArray("user_input_player_dps", dps);
            fragmentBundle.putDoubleArray("user_input_player_gps", gps);
            fragmentBundle.putDoubleArray("user_input_player_vps", vps);
            fragmentBundle.putSerializable("user_input_player_liked_players", likedPlayers);
            fragmentBundle.putSerializable("user_input_player_disliked_players", dislikedPlayers);

//            Log.d("ResultsActivity", data.toString());
//            Log.d("ResultsActivity", (String) data.getJSONObject("player2").getJSONObject("reviews").get("likes"));

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
//        player1 = new Player(
//                data.getJSONObject("player1").getString("name"),
//                data.getJSONObject("player1").getString("region"),
//                ((JSONArray) data.getJSONObject("player1").getJSONObject("reviews").get("likes")).length(),
//                ((JSONArray) data.getJSONObject("player1").getJSONObject("reviews").get("dislikes")).length(),
//                data.getJSONObject("player1").getJSONObject("stats").getDouble("kps"),
//                data.getJSONObject("player1").getJSONObject("stats").getDouble("aps"),
//                data.getJSONObject("player1").getJSONObject("stats").getDouble("dps"),
//                data.getJSONObject("player1").getJSONObject("stats").getDouble("gps"),
//                data.getJSONObject("player1").getJSONObject("stats").getDouble("vps"),
//                toStringArray(((JSONArray) data.getJSONObject("player1").getJSONObject("reviews").get("likes"))),
//                toStringArray(((JSONArray) data.getJSONObject("player1").getJSONObject("reviews").get("dislikes")))
////                data.getJSONObject("player1").getJSONObject("stats")
//        );

        player1 = new Player(
                data.getJSONObject("player1").getString("name"),
                data.getJSONObject("player1").getString("region")
        );

        player1.setLikes(((JSONArray) data.getJSONObject("player1").getJSONObject("reviews").get("likes")).length());
        player1.setDislikes( ((JSONArray) data.getJSONObject("player1").getJSONObject("reviews").get("dislikes")).length());
        player1.setKps(data.getJSONObject("player1").getJSONObject("stats").getDouble("kps"));
        player1.setAps(data.getJSONObject("player1").getJSONObject("stats").getDouble("aps"));
        player1.setDps(data.getJSONObject("player1").getJSONObject("stats").getDouble("dps"));
        player1.setGps(data.getJSONObject("player1").getJSONObject("stats").getDouble("gps"));
        player1.setVps(data.getJSONObject("player1").getJSONObject("stats").getDouble("vps"));
        player1.setLikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player1").getJSONObject("reviews").get("likes"))));
        player1.setDislikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player1").getJSONObject("reviews").get("dislikes"))));

        player2 = new Player(
                data.getJSONObject("player2").getString("name"),
                data.getJSONObject("player2").getString("region")
        );

        player2.setLikes(((JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("likes")).length());
        player2.setDislikes( ((JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("dislikes")).length());
        player2.setKps(data.getJSONObject("player2").getJSONObject("stats").getDouble("kps"));
        player2.setAps(data.getJSONObject("player2").getJSONObject("stats").getDouble("aps"));
        player2.setDps(data.getJSONObject("player2").getJSONObject("stats").getDouble("dps"));
        player2.setGps(data.getJSONObject("player2").getJSONObject("stats").getDouble("gps"));
        player2.setVps(data.getJSONObject("player2").getJSONObject("stats").getDouble("vps"));
        player2.setLikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("likes"))));
        player2.setDislikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("dislikes"))));

        player3 = new Player(
                data.getJSONObject("player3").getString("name"),
                data.getJSONObject("player3").getString("region")
        );

        player3.setLikes(((JSONArray) data.getJSONObject("player3").getJSONObject("reviews").get("likes")).length());
        player3.setDislikes( ((JSONArray) data.getJSONObject("player3").getJSONObject("reviews").get("dislikes")).length());
        player3.setKps(data.getJSONObject("player3").getJSONObject("stats").getDouble("kps"));
        player3.setAps(data.getJSONObject("player3").getJSONObject("stats").getDouble("aps"));
        player3.setDps(data.getJSONObject("player3").getJSONObject("stats").getDouble("dps"));
        player3.setGps(data.getJSONObject("player3").getJSONObject("stats").getDouble("gps"));
        player3.setVps(data.getJSONObject("player3").getJSONObject("stats").getDouble("vps"));
        player3.setLikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player3").getJSONObject("reviews").get("likes"))));
        player3.setDislikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player3").getJSONObject("reviews").get("dislikes"))));

        player4 = new Player(
                data.getJSONObject("player4").getString("name"),
                data.getJSONObject("player4").getString("region")
        );

        player4.setLikes(((JSONArray) data.getJSONObject("player4").getJSONObject("reviews").get("likes")).length());
        player4.setDislikes( ((JSONArray) data.getJSONObject("player4").getJSONObject("reviews").get("dislikes")).length());
        player4.setKps(data.getJSONObject("player4").getJSONObject("stats").getDouble("kps"));
        player4.setAps(data.getJSONObject("player4").getJSONObject("stats").getDouble("aps"));
        player4.setDps(data.getJSONObject("player4").getJSONObject("stats").getDouble("dps"));
        player4.setGps(data.getJSONObject("player4").getJSONObject("stats").getDouble("gps"));
        player4.setVps(data.getJSONObject("player4").getJSONObject("stats").getDouble("vps"));
        player4.setLikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player4").getJSONObject("reviews").get("likes"))));
        player4.setDislikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player4").getJSONObject("reviews").get("dislikes"))));

        player5 = new Player(
                data.getJSONObject("player5").getString("name"),
                data.getJSONObject("player5").getString("region")
        );

        player5.setLikes(((JSONArray) data.getJSONObject("player5").getJSONObject("reviews").get("likes")).length());
        player5.setDislikes( ((JSONArray) data.getJSONObject("player5").getJSONObject("reviews").get("dislikes")).length());
        player5.setKps(data.getJSONObject("player5").getJSONObject("stats").getDouble("kps"));
        player5.setAps(data.getJSONObject("player5").getJSONObject("stats").getDouble("aps"));
        player5.setDps(data.getJSONObject("player5").getJSONObject("stats").getDouble("dps"));
        player5.setGps(data.getJSONObject("player5").getJSONObject("stats").getDouble("gps"));
        player5.setVps(data.getJSONObject("player5").getJSONObject("stats").getDouble("vps"));
        player5.setLikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player5").getJSONObject("reviews").get("likes"))));
        player5.setDislikedPlayers(toStringArray(((JSONArray) data.getJSONObject("player5").getJSONObject("reviews").get("dislikes"))));



//        player2 = new Player(
//                data.getJSONObject("player2").getString("name"),
//                data.getJSONObject("player2").getString("region"),
//                ((JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("likes")).length(),
//                ((JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("dislikes")).length(),
//                data.getJSONObject("player2").getJSONObject("stats").getDouble("kps"),
//                data.getJSONObject("player2").getJSONObject("stats").getDouble("aps"),
//                data.getJSONObject("player2").getJSONObject("stats").getDouble("dps"),
//                data.getJSONObject("player2").getJSONObject("stats").getDouble("gps"),
//                data.getJSONObject("player2").getJSONObject("stats").getDouble("vps"),
//                toStringArray(((JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("likes"))),
//                toStringArray(((JSONArray) data.getJSONObject("player2").getJSONObject("reviews").get("dislikes")))
////                data.getJSONObject("player2").getJSONObject("stats")
//        );
//
//        player3 = new Player(
//                data.getJSONObject("player3").getString("name"),
//                data.getJSONObject("player3").getString("region"),
//                ((JSONArray) data.getJSONObject("player3").getJSONObject("reviews").get("likes")).length(),
//                ((JSONArray) data.getJSONObject("player3").getJSONObject("reviews").get("dislikes")).length(),
//                data.getJSONObject("player3").getJSONObject("stats").getDouble("kps"),
//                data.getJSONObject("player3").getJSONObject("stats").getDouble("aps"),
//                data.getJSONObject("player3").getJSONObject("stats").getDouble("dps"),
//                data.getJSONObject("player3").getJSONObject("stats").getDouble("gps"),
//                data.getJSONObject("player3").getJSONObject("stats").getDouble("vps"),
//                toStringArray(((JSONArray) data.getJSONObject("player3").getJSONObject("reviews").get("likes"))),
//                toStringArray(((JSONArray) data.getJSONObject("player3").getJSONObject("reviews").get("dislikes")))
////                data.getJSONObject("player3").getJSONObject("stats")
//        );
//
//        player4 = new Player(
//                data.getJSONObject("player4").getString("name"),
//                data.getJSONObject("player4").getString("region"),
//                ((JSONArray) data.getJSONObject("player4").getJSONObject("reviews").get("likes")).length(),
//                ((JSONArray) data.getJSONObject("player4").getJSONObject("reviews").get("dislikes")).length(),
//                data.getJSONObject("player4").getJSONObject("stats").getDouble("kps"),
//                data.getJSONObject("player4").getJSONObject("stats").getDouble("aps"),
//                data.getJSONObject("player4").getJSONObject("stats").getDouble("dps"),
//                data.getJSONObject("player4").getJSONObject("stats").getDouble("gps"),
//                data.getJSONObject("player4").getJSONObject("stats").getDouble("vps"),
//                toStringArray(((JSONArray) data.getJSONObject("player4").getJSONObject("reviews").get("likes"))),
//                toStringArray(((JSONArray) data.getJSONObject("player4").getJSONObject("reviews").get("dislikes")))
////                data.getJSONObject("player4").getJSONObject("stats")
//        );
//
//        player5 = new Player(
//                data.getJSONObject("player5").getString("name"),
//                data.getJSONObject("player5").getString("region"),
//                ((JSONArray) data.getJSONObject("player5").getJSONObject("reviews").get("likes")).length(),
//                ((JSONArray) data.getJSONObject("player5").getJSONObject("reviews").get("dislikes")).length(),
//                data.getJSONObject("player5").getJSONObject("stats").getDouble("kps"),
//                data.getJSONObject("player5").getJSONObject("stats").getDouble("aps"),
//                data.getJSONObject("player5").getJSONObject("stats").getDouble("dps"),
//                data.getJSONObject("player5").getJSONObject("stats").getDouble("gps"),
//                data.getJSONObject("player5").getJSONObject("stats").getDouble("vps"),
//                toStringArray(((JSONArray) data.getJSONObject("player5").getJSONObject("reviews").get("likes"))),
//                toStringArray(((JSONArray) data.getJSONObject("player5").getJSONObject("reviews").get("dislikes")))
////                data.getJSONObject("player5").getJSONObject("stats")
//        );
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

    public void setRegions() {
        regions[0] = player1.getRegion();
        regions[1] = player2.getRegion();
        regions[2] = player3.getRegion();
        regions[3] = player4.getRegion();
        regions[4] = player5.getRegion();
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

    public void setStats() {

        kps[0] = player1.getKps();
        kps[1] = player2.getKps();
        kps[2] = player3.getKps();
        kps[3] = player4.getKps();
        kps[4] = player5.getKps();

        aps[0] = player1.getAps();
        aps[1] = player2.getAps();
        aps[2] = player3.getAps();
        aps[3] = player4.getAps();
        aps[4] = player5.getAps();

        dps[0] = player1.getDps();
        dps[1] = player2.getDps();
        dps[2] = player3.getDps();
        dps[3] = player4.getDps();
        dps[4] = player5.getDps();

        gps[0] = player1.getGps();
        gps[1] = player2.getGps();
        gps[2] = player3.getGps();
        gps[3] = player4.getGps();
        gps[4] = player5.getGps();

        vps[0] = player1.getVps();
        vps[1] = player2.getVps();
        vps[2] = player3.getVps();
        vps[3] = player4.getVps();
        vps[4] = player5.getVps();

    }

    public void setLikedPlayers() {
        likedPlayers[0] = player1.getLikedPlayers();
        likedPlayers[1] = player2.getLikedPlayers();
        likedPlayers[2] = player3.getLikedPlayers();
        likedPlayers[3] = player4.getLikedPlayers();
        likedPlayers[4] = player5.getLikedPlayers();
    }

    public void setDislikedPlayers() {
        dislikedPlayers[0] = player1.getDislikedPlayers();
        dislikedPlayers[1] = player2.getDislikedPlayers();
        dislikedPlayers[2] = player3.getDislikedPlayers();
        dislikedPlayers[3] = player4.getDislikedPlayers();
        dislikedPlayers[4] = player5.getDislikedPlayers();
    }

    public static String[] toStringArray(JSONArray array) {
        if(array==null)
            return new String[0];

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }
}