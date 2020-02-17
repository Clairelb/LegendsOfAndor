package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import org.w3c.dom.Text;

import java.util.ArrayList;


enum DistributeItemsResponses{
    DISTRIBUTE_ITEMS_SUCCESS, DISTRIBUTE_ITEMS_FAILURE
}

public class DistributeItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributeitemspage);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final Spinner warriorGold = findViewById(R.id.warriorGold);
        warriorGold.setVisibility(View.INVISIBLE);
        final Spinner archerGold = findViewById(R.id.archerGold);
        archerGold.setVisibility(View.INVISIBLE);
        final Spinner wizardGold = findViewById(R.id.wizardGold);
        wizardGold.setVisibility(View.INVISIBLE);
        final Spinner dwarfGold = findViewById(R.id.dwarfGold);
        dwarfGold.setVisibility(View.INVISIBLE);

        final Spinner warriorWineskin = findViewById(R.id.warriorWineskin);
        warriorWineskin.setVisibility(View.INVISIBLE);
        final Spinner archerWineskin = findViewById(R.id.archerWineskin);
        archerWineskin.setVisibility(View.INVISIBLE);
        final Spinner wizardWineskin = findViewById(R.id.wizardWineskin);
        wizardWineskin.setVisibility(View.INVISIBLE);
        final Spinner dwarfWineskin = findViewById(R.id.dwarfWineskin);
        dwarfWineskin.setVisibility(View.INVISIBLE);

        final TextView warrior_text = findViewById(R.id.warrior);
        warrior_text.setVisibility(View.INVISIBLE);
        final TextView archer_text = findViewById(R.id.archer);
        archer_text.setVisibility(View.INVISIBLE);
        final TextView dwarf_text = findViewById(R.id.dwarf);
        dwarf_text.setVisibility(View.INVISIBLE);
        final TextView wizard_text = findViewById(R.id.wizard);
        wizard_text.setVisibility(View.INVISIBLE);

        final Game currentGame = MyPlayer.getInstance().getGame();

        for(int i = 0; i < currentGame.getCurrentNumPlayers(); i++){
            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.ARCHER){
                archerGold.setVisibility(View.VISIBLE);
                archerWineskin.setVisibility(View.VISIBLE);
                archer_text.setVisibility(View.VISIBLE);
            }
            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WARRIOR){
                warriorGold.setVisibility(View.VISIBLE);
                warriorWineskin.setVisibility(View.VISIBLE);
                warrior_text.setVisibility(View.VISIBLE);
            }
            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WIZARD){
                wizardGold.setVisibility(View.VISIBLE);
                wizardWineskin.setVisibility(View.VISIBLE);
                wizard_text.setVisibility(View.VISIBLE);
            }
            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.DWARF){
                dwarfGold.setVisibility(View.VISIBLE);
                dwarfWineskin.setVisibility(View.VISIBLE);
                dwarf_text.setVisibility(View.VISIBLE);
            }
        }


        Button confirmDistribution = findViewById(R.id.finalizeDistribution);

        confirmDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int war_gold = Integer.parseInt(warriorGold.getSelectedItem().toString());
                int arch_gold = Integer.parseInt(archerGold.getSelectedItem().toString());
                int wiz_gold = Integer.parseInt(wizardGold.getSelectedItem().toString());
                int dwarf_gold = Integer.parseInt(dwarfGold.getSelectedItem().toString());

                int wiz_wineskin = Integer.parseInt(wizardWineskin.getSelectedItem().toString());
                int war_wineskin = Integer.parseInt(warriorWineskin.getSelectedItem().toString());
                int arch_wineskin = Integer.parseInt(archerWineskin.getSelectedItem().toString());
                int dwarf_wineskin = Integer.parseInt(dwarfWineskin.getSelectedItem().toString());

                if((war_gold + arch_gold + wiz_gold + dwarf_gold == 5) && (wiz_wineskin + war_wineskin + arch_wineskin + dwarf_wineskin == 2)){
                    ItemDistribution itemDistribution = new ItemDistribution();
                    for(int i = 0; i < currentGame.getCurrentNumPlayers(); i++){
                        if(i == 1){
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.ARCHER){
                                itemDistribution.setPlayer1Gold(arch_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < arch_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer1Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WARRIOR){
                                itemDistribution.setPlayer1Gold(war_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < war_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer1Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WIZARD){
                                itemDistribution.setPlayer1Gold(wiz_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < arch_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer1Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.DWARF){
                                itemDistribution.setPlayer1Gold(dwarf_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < dwarf_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer1Items(startingItems);
                            }
                        }
                        if(i == 2){
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.ARCHER){
                                itemDistribution.setPlayer2Gold(arch_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < arch_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer2Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WARRIOR){
                                itemDistribution.setPlayer2Gold(war_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < war_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer2Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WIZARD){
                                itemDistribution.setPlayer2Gold(wiz_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < arch_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer2Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.DWARF){
                                itemDistribution.setPlayer2Gold(dwarf_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < dwarf_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer2Items(startingItems);
                            }

                        }
                        if(i == 3){
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.ARCHER){
                                itemDistribution.setPlayer3Gold(arch_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < arch_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer3Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WARRIOR){
                                itemDistribution.setPlayer3Gold(war_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < war_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer3Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WIZARD){
                                itemDistribution.setPlayer3Gold(wiz_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < arch_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer3Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.DWARF){
                                itemDistribution.setPlayer3Gold(dwarf_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < dwarf_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer3Items(startingItems);
                            }
                        }
                        if(i == 4){
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.ARCHER){
                                itemDistribution.setPlayer4Gold(arch_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < arch_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer2Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WARRIOR){
                                itemDistribution.setPlayer4Gold(war_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < war_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer2Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.WIZARD){
                                itemDistribution.setPlayer4Gold(wiz_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < arch_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer2Items(startingItems);
                            }
                            if(currentGame.getPlayers()[i].getHero().getHeroClass() == HeroClass.DWARF){
                                itemDistribution.setPlayer4Gold(dwarf_gold);
                                ArrayList<Item> startingItems = new ArrayList<>();
                                for(int j = 0; j < dwarf_wineskin; j++){
                                    startingItems.add(new Wineskin());
                                }
                                if(startingItems.size()> 0) itemDistribution.setPlayer2Items(startingItems);
                            }

                        }
                    }
                    AsyncTask<String, Void, DistributeItemsResponses> asyncTask;
                    DistributeItemsResponses distributeItemsResponses;
                    try{
                        SendItemDistribution sendItemDistribution = new SendItemDistribution();
                        asyncTask = sendItemDistribution.execute(new Gson().toJson(itemDistribution));
                        distributeItemsResponses = asyncTask.get();

                        if(distributeItemsResponses == DistributeItemsResponses.DISTRIBUTE_ITEMS_SUCCESS){
                            Toast.makeText(DistributeItems.this,"Valid Item Distribution. Welcome to the Game. Good luck",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(DistributeItems.this, Board.class));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(DistributeItems.this, "Error. Invalid item distribution", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private static class SendItemDistribution extends AsyncTask<String, Void, DistributeItemsResponses> {
        @Override
        protected DistributeItemsResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/distributeItems")
                            .header("Content-Type", "application/json")
                            .body(strings[0])
                            .asString();
                String resultAsJsonString = response.getBody();
                System.out.println("RESPONSE: =========> " + resultAsJsonString);
                return new Gson().fromJson(resultAsJsonString,DistributeItemsResponses.class );
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return DistributeItemsResponses.DISTRIBUTE_ITEMS_FAILURE;
        }
    }

}
