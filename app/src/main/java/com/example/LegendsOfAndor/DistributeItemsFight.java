package com.example.LegendsOfAndor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DistributeItemsFight extends AppCompatActivity {

    @Override
    public void onBackPressed(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distribute_items_fight);


        final Spinner warriorGold = findViewById(R.id.warrior_fight_gold);
        warriorGold.setVisibility(View.INVISIBLE);
        final Spinner archerGold = findViewById(R.id.archer_fight_gold);
        archerGold.setVisibility(View.INVISIBLE);
        final Spinner wizardGold = findViewById(R.id.wizard_fight_gold);
        wizardGold.setVisibility(View.INVISIBLE);
        final Spinner dwarfGold = findViewById(R.id.dwarf_fight_gold);
        dwarfGold.setVisibility(View.INVISIBLE);

        final Spinner warriorWillpower = findViewById(R.id.warrior_fight_willpower);
        warriorWillpower.setVisibility(View.INVISIBLE);
        final Spinner archerWillpower = findViewById(R.id.archer_fight_willpower);
        archerWillpower.setVisibility(View.INVISIBLE);
        final Spinner wizardWillpower = findViewById(R.id.wizard_fight_willpower);
        wizardWillpower.setVisibility(View.INVISIBLE);
        final Spinner dwarfWillpower = findViewById(R.id.dwarf_fight_willpower);
        dwarfWillpower.setVisibility(View.INVISIBLE);

        final TextView warrior_text = findViewById(R.id.warrior_fight);
        warrior_text.setVisibility(View.INVISIBLE);
        final TextView archer_text = findViewById(R.id.archer_fight);
        archer_text.setVisibility(View.INVISIBLE);
        final TextView dwarf_text = findViewById(R.id.dwarf_fight);
        dwarf_text.setVisibility(View.INVISIBLE);
        final TextView wizard_text = findViewById(R.id.wizard_fight);
        wizard_text.setVisibility(View.INVISIBLE);

        try{
            AsyncTask<String, Void, Game> asyncTask1;
            Game gameToSet;
            GetGame getGame = new GetGame();
            asyncTask1 = getGame.execute();
            gameToSet = asyncTask1.get();
            System.out.println(gameToSet);
            MyPlayer.getInstance().setGame(gameToSet);
        }catch (Exception e){
            e.printStackTrace();
        }

        final Game currentGame = MyPlayer.getInstance().getGame();


        for(int i = 0; i < currentGame.getCurrentNumPlayers(); i++){
            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.ARCHER){
                archerGold.setVisibility(View.VISIBLE);
                archerWillpower.setVisibility(View.VISIBLE);
                archer_text.setVisibility(View.VISIBLE);
            }
            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WARRIOR){
                warriorGold.setVisibility(View.VISIBLE);
                warriorWillpower.setVisibility(View.VISIBLE);
                warrior_text.setVisibility(View.VISIBLE);
            }
            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WIZARD){
                wizardGold.setVisibility(View.VISIBLE);
                wizardWillpower.setVisibility(View.VISIBLE);
                wizard_text.setVisibility(View.VISIBLE);
            }
            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.DWARF){
                dwarfGold.setVisibility(View.VISIBLE);
                dwarfWillpower.setVisibility(View.VISIBLE);
                dwarf_text.setVisibility(View.VISIBLE);
            }
        }


        Button confirmDistribution = findViewById(R.id.finalize_fight_distribution);

        confirmDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int war_gold = Integer.parseInt(warriorGold.getSelectedItem().toString());
                int arch_gold = Integer.parseInt(archerGold.getSelectedItem().toString());
                int wiz_gold = Integer.parseInt(wizardGold.getSelectedItem().toString());
                int dwarf_gold = Integer.parseInt(dwarfGold.getSelectedItem().toString());

                int wiz_willpower = Integer.parseInt(wizardWillpower.getSelectedItem().toString());
                int war_willpower = Integer.parseInt(warriorWillpower.getSelectedItem().toString());
                int arch_willpower = Integer.parseInt(archerWillpower.getSelectedItem().toString());
                int dwarf_willpower = Integer.parseInt(dwarfWillpower.getSelectedItem().toString());

                if((war_gold + arch_gold + wiz_gold + dwarf_gold == currentGame.getCurrentFight().getCreature().getGoldReward()) && (wiz_willpower+war_willpower+arch_willpower+dwarf_willpower == currentGame.getCurrentFight().getCreature().getWillpowerReward())){
                    FightDistribution fightDistribution = new FightDistribution();
                    fightDistribution.setArcherGold(arch_gold);
                    fightDistribution.setArcherWillpower(arch_willpower);
                    fightDistribution.setDwarfGold(dwarf_gold);
                    fightDistribution.setDwarfWillpower(dwarf_willpower);
                    fightDistribution.setWarriorGold(war_gold);
                    fightDistribution.setWarriorWillpower(war_willpower);
                    fightDistribution.setWizardGold(wiz_gold);
                    fightDistribution.setWizardWillpower(wiz_willpower);
                    //SEND DISTRIBUTION TO SERVER

                    try {
                        DistributeAfterFightSender distributeAfterFightSender = new DistributeAfterFightSender();
                        distributeAfterFightSender.execute(new Gson().toJson(fightDistribution));

                        Toast.makeText(DistributeItemsFight.this, "Distribution successful. Going back to the board...", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DistributeItemsFight.this, Board.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //IF DISTRIBUTION OK, GO BACK TO BOARD

                }else{
                    Toast.makeText(DistributeItemsFight.this, "Invalid distribution. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button chatb = findViewById(R.id.distribute_items_fight_chatb);
        chatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ChatScreen.class);
                startActivity(myIntent);
            }
        });


    }

    private static class GetGame extends AsyncTask<String, Void, Game > {
        @Override
        protected Game doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getGameByUsername")
                        .asString();

                String resultAsJsonString = response.getBody();
                System.out.println("RESPONSE BODY " + response.getBody());
                return new Gson().fromJson(resultAsJsonString, Game.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class DistributeAfterFightSender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();

            try {
                Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/distributeAfterFight")
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
