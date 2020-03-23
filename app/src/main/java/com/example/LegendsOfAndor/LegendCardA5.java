package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class LegendCardA5 extends AppCompatActivity {
    private ImageView heroDie;
    private Button rollDie;
    private Button toDistributeItems;
    private Die die;
    private int roll = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend_card_a5);

        final MyPlayer myPlayer = MyPlayer.getInstance();
        heroDie = findViewById(R.id.legend_card_a5_die);
        rollDie = findViewById(R.id.roll_die_button_a5);
        toDistributeItems = findViewById(R.id.to_distribute_items_a5);
        die = new Die(DieType.REGULAR_DIE);

        if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
            heroDie.setVisibility(View.VISIBLE);
            rollDie.setVisibility(View.VISIBLE);
        }

        rollDie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (roll == 0) {
                    roll = die.rollDie();

                    if (roll == 1) {
                        heroDie.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_1", "drawable", getApplicationContext())));
                    } else if (roll == 2) {
                        heroDie.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_2", "drawable", getApplicationContext())));
                    } else if (roll == 3) {
                        heroDie.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_3", "drawable", getApplicationContext())));
                    } else if (roll == 4) {
                        heroDie.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_4", "drawable", getApplicationContext())));
                    } else if (roll == 5) {
                        heroDie.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_5", "drawable", getApplicationContext())));
                    } else { // roll == 6
                        heroDie.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_6", "drawable", getApplicationContext())));
                    }
                } else {
                    Toast.makeText(LegendCardA5.this, "You may only roll the die once.", Toast.LENGTH_LONG).show();
                }
            }
        });

        toDistributeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
                    if (roll != 0) {
                        ActivateLegendCardRuneStonesSender activateLegendCardRuneStonesSender = new ActivateLegendCardRuneStonesSender();
                        activateLegendCardRuneStonesSender.execute(new Gson().toJson(roll));

                        Intent intent = new Intent(LegendCardA5.this, DistributeItems.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LegendCardA5.this, "You must roll the die before proceeding to distribute items.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intent = new Intent(LegendCardA5.this, DistributeItemsWaitPage.class);
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

    private static class ActivateLegendCardRuneStonesSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/activateLegendCardRuneStones")
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
