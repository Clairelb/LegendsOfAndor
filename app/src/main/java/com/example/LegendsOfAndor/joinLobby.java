package com.example.LegendsOfAndor;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class joinLobby extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //SET BUTTON TEXT FONT
        Typeface gothicFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "LeagueGothic-Regular.otf");
        Button join_lobby_btn = findViewById(R.id.join_lobby_button);
        Button ready_btn = findViewById(R.id.ready_button);

        join_lobby_btn.setTypeface(gothicFont);
        ready_btn.setTypeface(gothicFont);



    }
}
