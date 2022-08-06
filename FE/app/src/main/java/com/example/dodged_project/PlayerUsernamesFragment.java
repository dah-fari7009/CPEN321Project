package com.example.dodged_project;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.dodged_project.data.Player;
import com.example.dodged_project.data.PlayerArrayAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerUsernamesFragment extends ListFragment implements PlayerArrayAdapter.Callbacks {
    private ArrayList<Player> players = new ArrayList<>();
    private Callbacks callback;
    public PlayerUsernamesFragment(Callbacks callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String screen = null;

        if (getArguments() != null) {
            screen = getArguments().getString("activity");
            if(screen.equals("ResultsActivity")) {
//                String stats = getArguments().getString("user_input_player_stats");
//                JSONObject[] statsObjectArray = new JSONObject[5];

                String[][] getLikedPlayers = null;
                String[][] getDislikedPlayers = null;

                Object[] likedObjectArray = (Object[]) getArguments().getSerializable("user_input_player_liked_players");
                if(likedObjectArray!=null){
                    getLikedPlayers = new String[likedObjectArray.length][];
                    for(int i=0;i<likedObjectArray.length;i++){
                        getLikedPlayers[i]=(String[]) likedObjectArray[i];
                    }
                }

                Object[] dislikedObjectArray = (Object[]) getArguments().getSerializable("user_input_player_disliked_players");
                if(dislikedObjectArray!=null){
                    getDislikedPlayers = new String[dislikedObjectArray.length][];
                    for(int i=0;i<dislikedObjectArray.length;i++){
                        getDislikedPlayers[i]=(String[]) dislikedObjectArray[i];
                    }
                }

                createPlayerArrayListForResults(
                        getArguments().getStringArray("user_input_player_names"),
                        getArguments().getStringArray("user_input_player_regions"),
                        getArguments().getIntArray("user_input_player_likes"),
                        getArguments().getIntArray("user_input_player_dislikes"),
                        getArguments().getDoubleArray("user_input_player_kps"),
                        getArguments().getDoubleArray("user_input_player_aps"),
                        getArguments().getDoubleArray("user_input_player_dps"),
                        getArguments().getDoubleArray("user_input_player_gps"),
                        getArguments().getDoubleArray("user_input_player_vps"),
                        getLikedPlayers,
                        getDislikedPlayers
                );
            } else {
                createPlayerArrayList(getArguments().getStringArray("user_input_player_names"));
            }
        }

        PlayerArrayAdapter adapter = new PlayerArrayAdapter(getActivity(), R.layout.player_username_item, players, this, screen);
        setListAdapter(adapter);
    }

    private void createPlayerArrayList(String[] playerUsernames) {
        for (int i = 0; i < playerUsernames.length; i++) {
            Player player = new Player(UUID.randomUUID().toString(), playerUsernames[i], playerUsernames[i].equals("") ? "no_user_added_icon" : "username_added");
            players.add(player);
        }
    }

    private void createPlayerArrayListForResults(
            String[] playerUsernames,
            String[] playerRegion,
            int[] playerLikes,
            int[] playerDislikes,
            double[] playerKPS,
            double[] playerAPS,
            double[] playerDPS,
            double[] playerGPS,
            double[] playerVPS,
            String[][] likedPlayers,
            String[][] dislikedPlayers
    ) {
        for (int i = 0; i < playerUsernames.length; i++) {

            Player player = new Player(
                    playerUsernames[i],
                    playerRegion[i]
//                    playerLikes[i],
//                    playerDislikes[i],
//                    playerKPS[i],
//                    playerAPS[i],
//                    playerDPS[i],
//                    playerGPS[i],
//                    playerVPS[i],
//                    likedPlayers[i],
//                    dislikedPlayers[i]
            );
            player.setLikes(playerLikes[i]);
            player.setDislikes(playerDislikes[i]);
            player.setKps(playerKPS[i]);
            player.setAps(playerAPS[i]);
            player.setDps(playerDPS[i]);
            player.setGps(playerGPS[i]);
            player.setVps(playerVPS[i]);
            player.setLikedPlayers(likedPlayers[i]);
            player.setDislikedPlayers(dislikedPlayers[i]);
//            Log.d("Fragment", player.getUsername());
            players.add(player);
        }
    }



//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.player_usernames_fragment, container, false);
//        Button button = (Button) view.findViewById(R.id.confirm_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("fragment button", "CLICKED ");
//            }
//        });
//        return view;
//    }

    @Override
    public void getPlayersFromPlayerArrayAdapter(ArrayList<Player> players) {
        this.players = players;
        String[] usernames = {"", "", "", "", ""};
        if (!players.isEmpty()) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i) != null)
                usernames[i] = players.get(i).getUsername() != null ? players.get(i).getUsername() : "";
            }
        }
        callback.getPlayersFromPlayerUsernamesFragment(usernames);
    }

    public interface Callbacks {
        void getPlayersFromPlayerUsernamesFragment(String[] playerUserNames);
    }

}
