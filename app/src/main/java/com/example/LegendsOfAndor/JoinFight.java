package com.example.LegendsOfAndor;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class JoinFight extends AppCompatActivity {
    Button joinFight;
    Button declineFight;
    MyPlayer myPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_fight_options);

        myPlayer = MyPlayer.getInstance();

        joinFight = findViewById(R.id.joinFight);
        joinFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JoinFight.this, MonsterFight.class));
                finish();
            }
        });

        declineFight = findViewById(R.id.joinFight);
        declineFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JoinFight.this, Board.class));
                finish();
            }
        });





    }
}
