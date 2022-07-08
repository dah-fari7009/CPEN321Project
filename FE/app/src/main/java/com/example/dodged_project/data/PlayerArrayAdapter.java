package com.example.dodged_project.data;

import android.app.Activity;
import android.content.Context;
import android.hardware.input.InputManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dodged_project.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerArrayAdapter extends ArrayAdapter<Player>{

    private Context context;
    private ArrayList<Player> players;
    private Callbacks callback;

    public PlayerArrayAdapter(@NonNull Context context, int resource, ArrayList<Player> players, Callbacks callback) {
        super(context, resource, players);
        this.context = context;
        this.players = players;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Player player = players.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.player_username_item, null);

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

        return view;
    }

    public interface Callbacks {
        void getPlayersFromPlayerArrayAdapter(ArrayList<Player> players);
    }
}
