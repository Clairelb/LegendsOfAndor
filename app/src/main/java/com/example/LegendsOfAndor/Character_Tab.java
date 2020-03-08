package com.example.LegendsOfAndor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class Character_Tab extends AppCompatActivity {

    @Override
    public void onBackPressed(){
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_tab);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //warrior - invisible
        ImageView warrior = findViewById(R.id.warrior);
        warrior.setVisibility(View.INVISIBLE);
        ImageView gold_war = findViewById(R.id.gold_war);
        gold_war.setVisibility(View.INVISIBLE);
        ImageView SP_war = findViewById(R.id.SP_war);
        SP_war.setVisibility(View.INVISIBLE);
        TextView WP_war = findViewById(R.id.willpower_war);
        WP_war.setVisibility(View.INVISIBLE);
        TextView amt_SP_war = findViewById(R.id.amt_SP_war);
        amt_SP_war.setVisibility(View.INVISIBLE);
        TextView amt_gold_war = findViewById(R.id.amt_gold_war);
        amt_gold_war.setVisibility(View.INVISIBLE);
        TextView amt_WP_war = findViewById(R.id.amt_WP_war);
        amt_WP_war.setVisibility(View.INVISIBLE);

        /*TextView helm_war = findViewById(R.id.helm_war);
        helm_war.setVisibility(View.INVISIBLE);
        TextView shield_bow_falcon_war = findViewById(R.id.shield_bow_falcon_war);
        shield_bow_falcon_war.setVisibility(View.INVISIBLE);
        TextView article1_war = findViewById(R.id.article1_war);
        article1_war.setVisibility(View.INVISIBLE);
        TextView article2_war = findViewById(R.id.article2_war);
        article2_war.setVisibility(View.INVISIBLE);
        TextView article3_war = findViewById(R.id.article3_war);
        article3_war.setVisibility(View.INVISIBLE);*/

        //mage - invisible
        ImageView mage = findViewById(R.id.mage);
        mage.setVisibility(View.INVISIBLE);
        ImageView gold_mg = findViewById(R.id.gold_mg);
        gold_mg.setVisibility(View.INVISIBLE);
        ImageView SP_mg = findViewById(R.id.SP_mg);
        SP_mg.setVisibility(View.INVISIBLE);
        TextView WP_mg = findViewById(R.id.willpower_mg);
        WP_mg.setVisibility(View.INVISIBLE);
        TextView amt_SP_mg = findViewById(R.id.amt_SP_mg);
        amt_SP_mg.setVisibility(View.INVISIBLE);
        TextView amt_gold_mg = findViewById(R.id.amt_gold_mg);
        amt_gold_mg.setVisibility(View.INVISIBLE);
        TextView amt_WP_mg = findViewById(R.id.amt_WP_mg);
        amt_WP_mg.setVisibility(View.INVISIBLE);

        /*TextView helm_mg = findViewById(R.id.helm_mg);
        helm_mg.setVisibility(View.INVISIBLE);
        TextView shield_bow_falcon_mg = findViewById(R.id.shield_bow_falcon_mg);
        shield_bow_falcon_mg.setVisibility(View.INVISIBLE);
        TextView article1_mg = findViewById(R.id.article1_mg);
        article1_mg.setVisibility(View.INVISIBLE);
        TextView article2_mg = findViewById(R.id.article2_mg);
        article2_mg.setVisibility(View.INVISIBLE);
        TextView article3_mg = findViewById(R.id.article3_mg);
        article3_mg.setVisibility(View.INVISIBLE);*/

        //dwarf - invisible
        ImageView dwarf = findViewById(R.id.dwarf);
        dwarf.setVisibility(View.INVISIBLE);
        ImageView gold_dw = findViewById(R.id.gold_dw);
        gold_dw.setVisibility(View.INVISIBLE);
        ImageView SP_dw = findViewById(R.id.SP_dw);
        SP_dw.setVisibility(View.INVISIBLE);
        TextView WP_dw = findViewById(R.id.willpower_dw);
        WP_dw.setVisibility(View.INVISIBLE);
        TextView amt_SP_dw = findViewById(R.id.amt_SP_dw);
        amt_SP_dw.setVisibility(View.INVISIBLE);
        TextView amt_gold_dw = findViewById(R.id.amt_gold_dw);
        amt_gold_dw.setVisibility(View.INVISIBLE);
        TextView amt_WP_dw = findViewById(R.id.amt_WP_dw);
        amt_WP_dw.setVisibility(View.INVISIBLE);

        /*TextView helm_dw = findViewById(R.id.helm_dw);
        helm_dw.setVisibility(View.INVISIBLE);
        TextView shield_bow_falcon_dw = findViewById(R.id.shield_bow_falcon_dw);
        shield_bow_falcon_dw.setVisibility(View.INVISIBLE);
        TextView article1_dw = findViewById(R.id.article1_dw);
        article1_dw.setVisibility(View.INVISIBLE);
        TextView article2_dw = findViewById(R.id.article2_dw);
        article2_dw.setVisibility(View.INVISIBLE);
        TextView article3_dw = findViewById(R.id.article3_dw);
        article3_dw.setVisibility(View.INVISIBLE);*/

        //archer - invisible
        ImageView archer = findViewById(R.id.archer);
        archer.setVisibility(View.INVISIBLE);
        ImageView gold_ar = findViewById(R.id.gold_ar);
        gold_ar.setVisibility(View.INVISIBLE);
        ImageView SP_ar = findViewById(R.id.SP_ar);
        SP_ar.setVisibility(View.INVISIBLE);
        TextView WP_ar = findViewById(R.id.willpower_ar);
        WP_ar.setVisibility(View.INVISIBLE);
        TextView amt_SP_ar = findViewById(R.id.amt_SP_ar);
        amt_SP_ar.setVisibility(View.INVISIBLE);
        TextView amt_gold_ar = findViewById(R.id.amt_gold_ar);
        amt_gold_ar.setVisibility(View.INVISIBLE);
        TextView amt_WP_ar = findViewById(R.id.amt_WP_ar);
        amt_WP_ar.setVisibility(View.INVISIBLE);

        /*TextView helm_ar = findViewById(R.id.helm_ar);
        helm_ar.setVisibility(View.INVISIBLE);
        TextView shield_bow_falcon_ar = findViewById(R.id.shield_bow_falcon_ar);
        shield_bow_falcon_ar.setVisibility(View.INVISIBLE);
        TextView article1_ar = findViewById(R.id.article1_ar);
        article1_ar.setVisibility(View.INVISIBLE);
        TextView article2_ar = findViewById(R.id.article2_ar);
        article2_ar.setVisibility(View.INVISIBLE);
        TextView article3_ar = findViewById(R.id.article3_ar);
        article3_ar.setVisibility(View.INVISIBLE);*/

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

        final Game game  = MyPlayer.getInstance().getGame();

        for (int i = 0; i < game .getCurrentNumPlayers(); i++) {
            HeroClass hClass = game.getPlayers()[i].getHero().getHeroClass();
            //ArrayList<Item> items = game.getPlayers()[i].getHero().getItems();
            if (hClass == HeroClass.WARRIOR) {
                warrior.setVisibility(View.VISIBLE);
                gold_war.setVisibility(View.VISIBLE);
                SP_war.setVisibility(View.VISIBLE);
                WP_war.setVisibility(View.VISIBLE);
                amt_SP_war.setVisibility(View.VISIBLE);
                amt_gold_war.setVisibility(View.VISIBLE);
                amt_WP_war.setVisibility(View.VISIBLE);

                amt_SP_war.setText(String.valueOf(game.getPlayers()[i].getHero().getStrength()));
                amt_gold_war.setText(String.valueOf(game.getPlayers()[i].getHero().getGold()));
                amt_WP_war.setText(String.valueOf(game.getPlayers()[i].getHero().getWillPower()));

            }
            if(hClass == HeroClass.ARCHER){
                archer.setVisibility(View.VISIBLE);
                gold_ar.setVisibility(View.VISIBLE);
                SP_ar.setVisibility(View.VISIBLE);
                WP_ar.setVisibility(View.VISIBLE);
                amt_SP_ar.setVisibility(View.VISIBLE);
                amt_gold_ar.setVisibility(View.VISIBLE);
                amt_WP_ar.setVisibility(View.VISIBLE);

                amt_SP_ar.setText(String.valueOf(game.getPlayers()[i].getHero().getStrength()));
                amt_gold_ar.setText(String.valueOf(game.getPlayers()[i].getHero().getGold()));
                amt_WP_ar.setText(String.valueOf(game.getPlayers()[i].getHero().getWillPower()));
            }
            if(hClass == HeroClass.WIZARD){
                mage.setVisibility(View.VISIBLE);
                gold_mg.setVisibility(View.VISIBLE);
                SP_mg.setVisibility(View.VISIBLE);
                WP_mg.setVisibility(View.VISIBLE);
                amt_SP_mg.setVisibility(View.VISIBLE);
                amt_gold_mg.setVisibility(View.VISIBLE);
                amt_WP_mg.setVisibility(View.VISIBLE);

                amt_SP_mg.setText(String.valueOf(game.getPlayers()[i].getHero().getStrength()));
                amt_gold_mg.setText(String.valueOf(game.getPlayers()[i].getHero().getGold()));
                amt_WP_mg.setText(String.valueOf(game.getPlayers()[i].getHero().getWillPower()));
            }
            if(hClass == HeroClass.DWARF){
                dwarf.setVisibility(View.VISIBLE);
                gold_dw.setVisibility(View.VISIBLE);
                SP_dw.setVisibility(View.VISIBLE);
                WP_dw.setVisibility(View.VISIBLE);
                amt_SP_dw.setVisibility(View.VISIBLE);
                amt_gold_dw.setVisibility(View.VISIBLE);
                amt_WP_dw.setVisibility(View.VISIBLE);

                amt_SP_dw.setText(String.valueOf(game.getPlayers()[i].getHero().getStrength()));
                amt_gold_dw.setText(String.valueOf(game.getPlayers()[i].getHero().getGold()));
                amt_WP_dw.setText(String.valueOf(game.getPlayers()[i].getHero().getWillPower()));
            }
        }

        ImageButton backb = (ImageButton) findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), OptionsTab.class);
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
}