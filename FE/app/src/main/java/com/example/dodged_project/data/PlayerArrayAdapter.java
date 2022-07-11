package com.example.dodged_project.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dodged_project.PlayerProfileActivity;
import com.example.dodged_project.R;
import com.example.dodged_project.ResultsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerArrayAdapter extends ArrayAdapter<Player>{

    private Context context;
    private ArrayList<Player> players;
    private Callbacks callback;
    private String screen;

    private boolean likeClicked = false;
    private boolean dislikeClicked = false;

    public PlayerArrayAdapter(@NonNull Context context, int resource, ArrayList<Player> players, Callbacks callback, String screen) {
        super(context, resource, players);
        this.context = context;
        this.players = players;
        this.callback = callback;
        this.screen = screen;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Player player = players.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Log.d("ArrayAdapter", player.getUsername());

        View view = inflater.inflate(R.layout.player_username_item, null);

        if(screen != null && screen == "FinalizeTeammatesActivity") {
            CardView cardView = view.findViewById(R.id.alt_card_view);
            cardView.setVisibility(View.GONE);

            ImageView playerAvatarImageView = (ImageView) view.findViewById(R.id.playerAvatar);
            playerAvatarImageView.setImageResource(player.getAvatarResourceId(context));

            TextView textView = (TextView) view.findViewById(R.id.playerUsername);
            textView.setText(player.getUsername());

            textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        player.setUsername(textView.getText().toString());
                        player.setAvatar("username_added");
                        callback.getPlayersFromPlayerArrayAdapter(players);
                        notifyDataSetChanged();
                        return true;
                    }
                    return false;
                }
            });

            ImageView usernameEditImageView = (ImageView) view.findViewById(R.id.usernameEditButton);
            usernameEditImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.requestFocus();
                }
            });

            ImageView usernameDeleteImageView = (ImageView) view.findViewById(R.id.usernameDeleteButton);
            usernameDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setText("");
                    player.setUsername("");
                    player.setAvatar("no_user_added_icon");
                    callback.getPlayersFromPlayerArrayAdapter(players);
                    notifyDataSetChanged();
                }
            });
        } else if(screen != null && screen == "ResultsActivity") {
            CardView cardView = view.findViewById(R.id.card_view);
            cardView.setVisibility(View.GONE);

            ImageView playerAvatarImageView = (ImageView) view.findViewById(R.id.alt_playerAvatar);
//            playerAvatarImageView.setImageResource(player.getAvatarResourceId(context));

            TextView textView = (TextView) view.findViewById(R.id.alt_playerUsername);
            textView.setText(player.getUsername());


            ImageView playerLikeImageView = (ImageView) view.findViewById(R.id.player_like_button);
            ImageView playerDislikeImageView = (ImageView) view.findViewById(R.id.player_dislike_button);

            TextView playerNumberLikes = (TextView) view.findViewById(R.id.player_number_likes);
            TextView playerNumberDislikes = (TextView) view.findViewById(R.id.player_number_dislikes);

            playerNumberLikes.setText(String.valueOf(player.getLikes()));
            playerNumberDislikes.setText(String.valueOf(player.getDislikes()));


            playerLikeImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (!likeClicked || dislikeClicked) {
                        setUserLikes(player.getUsername());
                        playerLikeImageView.setColorFilter(Color.rgb(0, 255, 0));
                        player.setLikes(player.getLikes() + 1);
                        playerDislikeImageView.setColorFilter(Color.rgb(16, 24, 40));
                        player.setDislikes(player.getDislikes() > 0 ? player.getDislikes() - 1 : 0);
                        likeClicked = true;
                        playerNumberLikes.setText(String.valueOf(player.getLikes()));
                        playerNumberDislikes.setText(String.valueOf(player.getDislikes()));
                    } else {
                        setUserDislikes(player.getUsername());
                        playerLikeImageView.setColorFilter(Color.rgb(16, 24, 40));
                        player.setLikes(player.getLikes() > 0 ? player.getLikes() - 1 : 0);
                        likeClicked = false;
                        playerNumberLikes.setText(String.valueOf(player.getLikes()));
                    }

                    Log.d("ArrayAdapter", String.valueOf(playerLikeImageView.getColorFilter()));
                }
            });

            playerDislikeImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!dislikeClicked || likeClicked) {
                        setUserDislikes(player.getUsername());
                        playerDislikeImageView.setColorFilter(Color.rgb(255, 0, 0));
                        player.setDislikes(player.getDislikes() + 1);
                        playerLikeImageView.setColorFilter(Color.rgb(16, 24, 40));
                        player.setLikes(player.getLikes() > 0 ? player.getLikes() - 1 : 0);
                        dislikeClicked = true;
                        playerNumberDislikes.setText(String.valueOf(player.getDislikes()));
                        playerNumberLikes.setText(String.valueOf(player.getLikes()));
                    } else {
                        setUserLikes(player.getUsername());
                        playerDislikeImageView.setColorFilter(Color.rgb(16, 24, 40));
                        player.setDislikes(player.getDislikes() > 0 ? player.getDislikes() - 1 : 0);
                        dislikeClicked = false;
                        playerNumberDislikes.setText(String.valueOf(player.getDislikes()));
                    }
                }
            });

            CardView userProfileCard = view.findViewById(R.id.alt_card_view);
            userProfileCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ArrayAdapter", "CLICKED");
                    Intent playerProfileIntent = new Intent(getContext(), PlayerProfileActivity.class);

                    Bundle playerProfileBundle = new Bundle();
                    playerProfileBundle.putString("player_username", player.getUsername());
                    playerProfileBundle.putString("region", player.getRegion());
                    playerProfileBundle.putDouble("kps", player.getKps());
                    playerProfileBundle.putDouble("aps", player.getAps());
                    playerProfileBundle.putDouble("dps", player.getDps());
                    playerProfileBundle.putDouble("gps", player.getGps());
                    playerProfileBundle.putDouble("vps", player.getVps());

                    playerProfileIntent.putExtras(playerProfileBundle);
                    context.startActivity(playerProfileIntent);
                }
            });

        }

        return view;
    }

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
//                Toast.makeText(ResultsActivity.this, "Error: Hmm something went wrong while trying to like this user profile", Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void setUserDislikes(String userName) {
        String sendUsernamesEndpoint = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/playerdb/dislike";
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
//                Toast.makeText(ResultsActivity.this, "Error: Hmm something went wrong while trying to like this user profile", Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        }
    }

    public interface Callbacks {
        void getPlayersFromPlayerArrayAdapter(ArrayList<Player> players);
    }
}
