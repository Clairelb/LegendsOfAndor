package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class LegendCardRuneStonesEasy extends AppCompatActivity {
    private ImageView heroDieIV;
    private ImageView enemyDieIV;
    private Button rollDice;
    private Button toBoard;
    private TextView runeStonePositionsTV;
    private Die heroDie;
    private Die enemyDie;
    private ArrayList<Integer> runeStonePositions;
    private String runeStonesPositionsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend_card_rune_stones_easy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final MyPlayer myPlayer = MyPlayer.getInstance();

        heroDieIV = findViewById(R.id.hero_die_rune_stones_easy);
        enemyDieIV = findViewById(R.id.enemy_die_rune_stones_easy);
        rollDice = findViewById(R.id.roll_dice_rune_stones_easy);
        toBoard = findViewById(R.id.to_board_rune_stones_easy);
        runeStonePositionsTV = findViewById(R.id.text_view_rune_stones_easy);
        heroDie = new Die(DieType.REGULAR_DIE);
        enemyDie = new Die(DieType.REGULAR_DIE);
        runeStonePositions = new ArrayList<>();
        runeStonesPositionsText = "Rune Stone Positions: ";

        if(myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
            heroDieIV.setVisibility(View.VISIBLE);
            enemyDieIV.setVisibility(View.VISIBLE);
            rollDice.setVisibility(View.VISIBLE);
            runeStonePositionsTV.setVisibility(View.VISIBLE);
        }

        rollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (runeStonePositions.size() < 5) {
                    int heroDieValue = heroDie.rollDie();
                    int enemyDieValue = enemyDie.rollDie();

                    if (heroDieValue == 1) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_1", "drawable", getApplicationContext())));
                    } else if (heroDieValue == 2) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_2", "drawable", getApplicationContext())));
                    } else if (heroDieValue == 3) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_3", "drawable", getApplicationContext())));
                    } else if (heroDieValue == 4) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_4", "drawable", getApplicationContext())));
                    } else if (heroDieValue == 5) {
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_5", "drawable", getApplicationContext())));
                    } else { // heroDieValue == 6
                        heroDieIV.setImageDrawable(getResources().getDrawable(getResourceID("archer_dice_6", "drawable", getApplicationContext())));
                    }

                    if (enemyDieValue == 1) {
                        enemyDieIV.setImageDrawable(getResources().getDrawable(getResourceID("enemy_dice_1", "drawable", getApplicationContext())));
                    } else if (enemyDieValue == 2) {
                        enemyDieIV.setImageDrawable(getResources().getDrawable(getResourceID("enemy_dice_2", "drawable", getApplicationContext())));
                    } else if (enemyDieValue == 3) {
                        enemyDieIV.setImageDrawable(getResources().getDrawable(getResourceID("enemy_dice_3", "drawable", getApplicationContext())));
                    } else if (enemyDieValue == 4) {
                        enemyDieIV.setImageDrawable(getResources().getDrawable(getResourceID("enemy_dice_4", "drawable", getApplicationContext())));
                    } else if (enemyDieValue == 5) {
                        enemyDieIV.setImageDrawable(getResources().getDrawable(getResourceID("enemy_dice_5", "drawable", getApplicationContext())));
                    } else { // enemyDieValue == 6
                        enemyDieIV.setImageDrawable(getResources().getDrawable(getResourceID("enemy_dice_6", "drawable", getApplicationContext())));
                    }

                    runeStonePositions.add(10 * enemyDieValue + heroDieValue);
                    runeStonesPositionsText += runeStonePositions.get(runeStonePositions.size()-1) + " ";
                    runeStonePositionsTV.setText(runeStonesPositionsText);
                } else {
                    Toast.makeText(LegendCardRuneStonesEasy.this, "You may only roll the die five times.", Toast.LENGTH_LONG).show();
                }
            }
        });

        toBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myPlayer.getPlayer().getUsername().equals(myPlayer.getGame().getPlayers()[0].getUsername())) {
                    if (runeStonePositions.size() == 5) {
                        ActivateLegendCardRuneStonesSender activateLegendCardRuneStonesSender = new ActivateLegendCardRuneStonesSender();
                        activateLegendCardRuneStonesSender.execute(new Gson().toJson(runeStonePositions));

                        Intent intent = new Intent(LegendCardRuneStonesEasy.this, Board.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LegendCardRuneStonesEasy.this, "You must roll the die five times before proceeding to the board.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent intent = new Intent(LegendCardRuneStonesEasy.this, Board.class);
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
