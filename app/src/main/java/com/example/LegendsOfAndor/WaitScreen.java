package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WaitScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button leave_lobby = findViewById(R.id.leave_lobby);

        leave_lobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EXIT LOBBY AND HEAD TO CREATE GAME
                startActivity(new Intent(WaitScreen.this, CreateGame.class));
            }
        });
    }

}
