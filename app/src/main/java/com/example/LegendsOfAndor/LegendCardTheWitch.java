package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class LegendCardTheWitch extends AppCompatActivity {
    private Button toBoard;
    private Button rollDie;
    private ImageView heroDieIV;
    private Die heroDie;
    private int roll = 0;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend_card_the_witch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toBoard = findViewById(R.id.to_board_the_witch);
        rollDie = findViewById(R.id.roll_die_the_witch);
        heroDieIV = findViewById(R.id.hero_die_the_witch);
        heroDie = new Die(DieType.REGULAR_DIE);
        final MyPlayer myPlayer = MyPlayer.getInstance();

        if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
            rollDie.setVisibility(View.VISIBLE);
            heroDieIV.setVisibility(View.VISIBLE);
        }

        toBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
                    if (roll != 0) {
                        ActivateLegendCardTheWitchSender activateLegendCardTheWitchSender = new ActivateLegendCardTheWitchSender();
                        activateLegendCardTheWitchSender.execute(new Gson().toJson(roll));

                        Intent intent = new Intent(LegendCardTheWitch.this, Board.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LegendCardTheWitch.this, "You must roll the die before proceeding to distribute items.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intent = new Intent(LegendCardTheWitch.this, Board.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        rollDie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roll == 0) {
                    roll = heroDie.rollDie();

                    if (roll == 1) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_1", "drawable", getApplicationContext())));
                    } else if (roll == 2) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_2", "drawable", getApplicationContext())));
                    } else if (roll == 3) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_3", "drawable", getApplicationContext())));
                    } else if (roll == 4) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_4", "drawable", getApplicationContext())));
                    } else if (roll == 5) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_5", "drawable", getApplicationContext())));
                    } else { // roll == 6
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_6", "drawable", getApplicationContext())));
                    }
                } else {
                    Toast.makeText(LegendCardTheWitch.this, "You may only roll the die once.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected final static int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        }
        else {
            return ResourceID;
        }
    }

    private static class ActivateLegendCardTheWitchSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/activateLegendCardTheWitch")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
