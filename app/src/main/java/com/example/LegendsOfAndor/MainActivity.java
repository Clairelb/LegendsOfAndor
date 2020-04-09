//khalil
package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //display start screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = MediaPlayer.create(this, R.raw.bgm);
        player.setLooping(true);
        player.start();
        //when start button is pressed go to login page

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Login.class);
                startActivity(myIntent);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}