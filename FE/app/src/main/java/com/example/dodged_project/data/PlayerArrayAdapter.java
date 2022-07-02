package com.example.dodged_project.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dodged_project.R;

import java.util.List;

public class PlayerArrayAdapter extends ArrayAdapter<Player> {

    private Context context;
    private List<Player> players;

    public PlayerArrayAdapter(@NonNull Context context, int resource, List<Player> players) {
        super(context, resource, players);
        this.context = context;
        this.players = players;
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
                players.remove(player);
                notifyDataSetChanged();
            }
        });


        return view;
    }
}
