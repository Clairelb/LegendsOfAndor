package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ChooseMonsterFight extends AppCompatActivity {
    private Button fightBTN;
    private Spinner creatureRegionsSP;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_monster_fight);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        fightBTN = findViewById(R.id.choose_mosnter_fight_BTN);
        creatureRegionsSP = findViewById(R.id.choose_monster_fight_spinner);

        ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, MyPlayer.getInstance().getPossibleCreaturesToFight());
        adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creatureRegionsSP.setAdapter(adapterDrop);

        fightBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPlayer myPlayer = MyPlayer.getInstance();
                AsyncTask<String, Void, Fight> asyncTask;

                try {
                    FightSender messageSender = new FightSender();
                    asyncTask = messageSender.execute(creatureRegionsSP.getSelectedItem().toString());
                    myPlayer.getGame().setCurrentFight(asyncTask.get());

                    Toast.makeText(ChooseMonsterFight.this, "Joining fight...", Toast.LENGTH_LONG).show();

                    Intent fightIntent = new Intent(ChooseMonsterFight.this, MonsterFight.class);
                    startActivity(fightIntent);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static class FightSender extends AsyncTask<String, Void, Fight> {
        @Override
        protected Fight doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://"+myPlayer.getServerIP()+":8080/"+myPlayer.getGame().getGameName() +"/"+ myPlayer.getPlayer().getUsername() + "/fight")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, Fight.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
