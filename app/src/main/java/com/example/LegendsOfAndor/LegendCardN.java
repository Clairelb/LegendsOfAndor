package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LegendCardN extends AppCompatActivity {
    private Button checkHowLegendEnded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend_card_n);

        checkHowLegendEnded = findViewById(R.id.check_how_legend_ended_n);
        checkHowLegendEnded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPlayer myPlayer = MyPlayer.getInstance();
                Intent intent;

                if (myPlayer.getGame().getGameStatus() == GameStatus.GAME_WON) {
                    intent = new Intent(LegendCardN.this, Victory.class);
                } else {
                    intent = new Intent(LegendCardN.this, GameOver.class);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {}
}
