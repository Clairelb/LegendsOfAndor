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

public class LegendCardC1Easy extends AppCompatActivity {
    private Button rollDieBTN;
    private Button nextLegendCardBTN;
    private ImageView heroDieIV;
    private Die myDie;
    private int roll = 0;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend_card_c1_easy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final MyPlayer myPlayer = MyPlayer.getInstance();
        rollDieBTN = findViewById(R.id.roll_die_c1_easy);
        nextLegendCardBTN = findViewById(R.id.next_legend_card_c1_easy);
        heroDieIV = findViewById(R.id.hero_die_c1_easy);
        myDie = new Die(DieType.REGULAR_DIE);

        if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
            rollDieBTN.setVisibility(View.VISIBLE);
            heroDieIV.setVisibility(View.VISIBLE);
        }

        rollDieBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roll == 0) {
                    roll = myDie.rollDie();

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
                    Toast.makeText(LegendCardC1Easy.this, "You may only roll the die once.", Toast.LENGTH_LONG).show();
                }
            }
        });

        nextLegendCardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
                    if (roll != 0) {
                        ActivateLegendCardCSender activateLegendCardCSender = new ActivateLegendCardCSender();
                        activateLegendCardCSender.execute(new Gson().toJson(roll));

                        Intent intent = new Intent(LegendCardC1Easy.this, LegendCardC2.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LegendCardC1Easy.this, "You must roll the die before proceeding to the next legend card.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intent = new Intent(LegendCardC1Easy.this, LegendCardC2.class);
                    startActivity(intent);
                    finish();
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

    @Override
    public void onBackPressed() {}

    private static class ActivateLegendCardCSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/activateLegendCardC")
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
