package com.example.dodged_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dodged_project.data.DodgedUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    public static String googleAccountName;
    public static String googleId;

    private DodgedUser dodgedUser = new DodgedUser();

    SharedPreferences sharedPreferences;
    RequestQueue queue;
    private String registerUserURL = "http://ec2-52-32-39-246.us-west-2.compute.amazonaws.com:8080/playerdb/add_user";
    public static final String FIREBASE_CHANNEL_ID = "dodged";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        SignInButton signInButton;

        Button continueAsGuestButton;

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    default:
                        break;
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        continueAsGuestButton = findViewById(R.id.continue_as_guest_button);
        continueAsGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseTeammatesIntent = new Intent(MainActivity.this, ChooseTeammatesActivity.class);
                startActivity(chooseTeammatesIntent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        Button darkModeButton = findViewById(R.id.dark_mode_button);

        darkModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDarkModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            updateUI(account);
            googleAccountName = account.getDisplayName();
            googleId = account.getEmail();
            dodgedUser.setGoogleID(googleId);

            //***** REMOVE THIS AFTER GETTING PUSH ENDPOINTS
            boolean riotIDSet = false;
            //***** REMOVE THIS AFTER GETTING PUSH ENDPOINTS

            // IF RIOT ID IS NOT SET
            if (!riotIDSet) {
                openRiotIDInputDialog();
            }
            else {
                Intent chooseTeammatesIntent = new Intent(MainActivity.this, ChooseTeammatesActivity.class);
                startActivity(chooseTeammatesIntent);
            }

        } catch (ApiException e) {
            updateUI(null);
        }
    }

    private void openRiotIDInputDialog() {
        BottomSheetDialog riotIDBottomSlideUpCardDialog = new BottomSheetDialog(MainActivity.this, R.style.riotIDBottomSlideUpCardDialog);
        riotIDBottomSlideUpCardDialog.setCancelable(false);
        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View riotIDBottomSlideUpCardView = layoutInflater.inflate(R.layout.riot_id_bottom_slide_up_card, null);
        riotIDBottomSlideUpCardDialog.setContentView(riotIDBottomSlideUpCardView);
        riotIDBottomSlideUpCardDialog.show();

        riotIDBottomSlideUpCardView.findViewById(R.id.riot_id_confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editRiotID = (EditText) v.getRootView().findViewById(R.id.riot_id_textinput);
                TextView riotIDCautionLabel = v.getRootView().findViewById(R.id.riot_id_caution_label);
                if(editRiotID == null || TextUtils.isEmpty(editRiotID.getText().toString())) {
                    riotIDCautionLabel.setVisibility(View.VISIBLE);
                }
                else {
                    dodgedUser.setRiotID(editRiotID.getText().toString());
                    createFirebaseTokenAndSubscribe();
                }
            }
        });
    }

    private void createFirebaseTokenAndSubscribe() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.d("PUSH", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                dodgedUser.setFirebaseToken(task.getResult());
                Log.d("PUSH", "Firebase Token: " + task.getResult());
                FirebaseMessaging.getInstance().subscribeToTopic(dodgedUser.getRiotID());
                try {
                    postRegisteredUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveDodgedUserInSP();
                Intent chooseTeammatesIntent = new Intent(MainActivity.this, ChooseTeammatesActivity.class);
                startActivity(chooseTeammatesIntent);
            }
        });
    }

    private void saveDodgedUserInSP() {
        sharedPreferences = getSharedPreferences("spDodgedUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("spDodgedUserGoogleID", dodgedUser.getGoogleID());
        editor.putString("spDodgedUserRiotID", dodgedUser.getRiotID());
        editor.putString("spDodgedUserFirebaseToken", dodgedUser.getFirebaseToken());
        editor.commit();
    }

    private void postRegisteredUserInfo() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("googleId", dodgedUser.getGoogleID());
        jsonBody.put("riotId", dodgedUser.getRiotID());
        jsonBody.put("token", dodgedUser.getFirebaseToken());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, registerUserURL, jsonBody,
                response ->
                {
                    Log.d("PUSH", "PUSER ADDED TO BACKEND");
                },
                error -> {
                    Log.d("JSON", error.toString());
                });

        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(jsonObjectRequest);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account == null) {
            Toast.makeText(this, "There is no user signed in. To use this feature please sign in", Toast.LENGTH_SHORT).show();
        }
    }
}