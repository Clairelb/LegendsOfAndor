package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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

        TextView helm_war = findViewById(R.id.helm_war);
        TextView shield_bow_falcon_war = findViewById(R.id.shield_bow_falcon_war);
        TextView article1_war = findViewById(R.id.article1_war);
        TextView article2_war = findViewById(R.id.article2_war);
        TextView article3_war = findViewById(R.id.article3_war);

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

        TextView helm_mg = findViewById(R.id.helm_mg);
        TextView shield_bow_falcon_mg = findViewById(R.id.shield_bow_falcon_mg);
        TextView article1_mg = findViewById(R.id.article1_mg);
        TextView article2_mg = findViewById(R.id.article2_mg);
        TextView article3_mg = findViewById(R.id.article3_mg);

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

        TextView helm_dw = findViewById(R.id.helm_dw);
        TextView shield_bow_falcon_dw = findViewById(R.id.shield_bow_falcon_dw);
        TextView article1_dw = findViewById(R.id.article1_dw);
        TextView article2_dw = findViewById(R.id.article2_dw);
        TextView article3_dw = findViewById(R.id.article3_dw);

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

        TextView helm_ar = findViewById(R.id.helm_ar);
        TextView shield_bow_falcon_ar = findViewById(R.id.shield_bow_falcon_ar);
        TextView article1_ar = findViewById(R.id.article1_ar);
        TextView article2_ar = findViewById(R.id.article2_ar);
        TextView article3_ar = findViewById(R.id.article3_ar);

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
            Hero hero = game.getPlayers()[i].getHero();
            ArrayList<Item> items = hero.getItems();
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

                for (Item item : items){
                    ItemType type = item.getItemType();
                    if(type == ItemType.HELM){
                        helm_war.setText(R.string.helm);
                    }
                    if(type == ItemType.BOW){
                        shield_bow_falcon_war.setText(R.string.bow);
                    }
                    if(type == ItemType.FALCON){
                        shield_bow_falcon_war.setText(R.string.falcon);
                    }
                    if(type == ItemType.SHIELD){
                        shield_bow_falcon_war.setText(R.string.shield);
                    }
                    if(type == ItemType.TELESCOPE){
                        if(article1_war.getText().toString().isEmpty()){
                            article1_war.setText(R.string.telescope);
                        }
                        else if(article2_war.getText().toString().isEmpty()){
                            article2_war.setText(R.string.telescope);
                        }
                        else{
                            article3_war.setText(R.string.telescope);
                        }
                    }
                    if(type == ItemType.WINESKIN){
                        if(article1_war.getText().toString().isEmpty()){
                            article1_war.setText(R.string.wineskin);
                        }
                        else if(article2_war.getText().toString().isEmpty()){
                            article2_war.setText(R.string.wineskin);
                        }
                        else{
                            article3_war.setText(R.string.wineskin);
                        }
                    }
                    if(type == ItemType.MEDICINAL_HERB){
                        if(article1_war.getText().toString().isEmpty()){
                            article1_war.setText(R.string.med_herb);
                        }
                        else if(article2_war.getText().toString().isEmpty()){
                            article2_war.setText(R.string.med_herb);
                        }
                        else{
                            article3_war.setText(R.string.med_herb);
                        }
                    }
                    if(type == ItemType.WITCH_BREW){
                        if(article1_war.getText().toString().isEmpty()){
                            article1_war.setText(R.string.witchbrew);
                        }
                        else if(article2_war.getText().toString().isEmpty()){
                            article2_war.setText(R.string.witchbrew);
                        }
                        else{
                            article3_war.setText(R.string.witchbrew);
                        }
                    }
                }
                ArrayList<RuneStone> runes = hero.getRuneStones();
                for (RuneStone rune : runes){
                    Colour colour = rune.getColour();
                    if(colour == Colour.BLUE){
                        if(article1_war.getText().toString().isEmpty()){
                            article1_war.setText(R.string.rune_stoneB);
                        }
                        else if(article2_war.getText().toString().isEmpty()){
                            article2_war.setText(R.string.rune_stoneB);
                        }
                        else{
                            article3_war.setText(R.string.rune_stoneB);
                        }
                    }
                    if(colour == Colour.GREEN){
                        if(article1_war.getText().toString().isEmpty()){
                            article1_war.setText(R.string.rune_stoneG);
                        }
                        else if(article2_war.getText().toString().isEmpty()){
                            article2_war.setText(R.string.rune_stoneG);
                        }
                        else{
                            article3_war.setText(R.string.rune_stoneG);
                        }
                    }
                    if(colour == Colour.YELLOW){
                        if(article1_war.getText().toString().isEmpty()){
                            article1_war.setText(R.string.rune_stoneY);
                        }
                        else if(article2_war.getText().toString().isEmpty()){
                            article2_war.setText(R.string.rune_stoneY);
                        }
                        else{
                            article3_war.setText(R.string.rune_stoneY);
                        }
                    }
                }
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

                for (Item item : items){
                    ItemType type = item.getItemType();
                    if(type == ItemType.HELM){
                        helm_ar.setText(R.string.helm);
                    }
                    if(type == ItemType.BOW){
                        shield_bow_falcon_ar.setText(R.string.bow);
                    }
                    if(type == ItemType.FALCON){
                        shield_bow_falcon_ar.setText(R.string.falcon);
                    }
                    if(type == ItemType.SHIELD){
                        shield_bow_falcon_ar.setText(R.string.shield);
                    }
                    if(type == ItemType.TELESCOPE){
                        if(article1_ar.getText().toString().isEmpty()){
                            article1_ar.setText(R.string.telescope);
                        }
                        else if(article2_ar.getText().toString().isEmpty()){
                            article2_ar.setText(R.string.telescope);
                        }
                        else{
                            article3_ar.setText(R.string.telescope);
                        }
                    }
                    if(type == ItemType.WINESKIN){
                        if(article1_ar.getText().toString().isEmpty()){
                            article1_ar.setText(R.string.wineskin);
                        }
                        else if(article2_ar.getText().toString().isEmpty()){
                            article2_ar.setText(R.string.wineskin);
                        }
                        else{
                            article3_ar.setText(R.string.wineskin);
                        }
                    }
                    if(type == ItemType.MEDICINAL_HERB){
                        if(article1_ar.getText().toString().isEmpty()){
                            article1_ar.setText(R.string.med_herb);
                        }
                        else if(article2_ar.getText().toString().isEmpty()){
                            article2_ar.setText(R.string.med_herb);
                        }
                        else{
                            article3_ar.setText(R.string.med_herb);
                        }
                    }
                    if(type == ItemType.WITCH_BREW){
                        if(article1_ar.getText().toString().isEmpty()){
                            article1_ar.setText(R.string.witchbrew);
                        }
                        else if(article2_ar.getText().toString().isEmpty()){
                            article2_ar.setText(R.string.witchbrew);
                        }
                        else{
                            article3_ar.setText(R.string.witchbrew);
                        }
                    }
                }
                ArrayList<RuneStone> runes = hero.getRuneStones();
                for (RuneStone rune : runes){
                    Colour colour = rune.getColour();
                    if(colour == Colour.BLUE){
                        if(article1_ar.getText().toString().isEmpty()){
                            article1_ar.setText(R.string.rune_stoneB);
                        }
                        else if(article2_ar.getText().toString().isEmpty()){
                            article2_ar.setText(R.string.rune_stoneB);
                        }
                        else{
                            article3_ar.setText(R.string.rune_stoneB);
                        }
                    }
                    if(colour == Colour.GREEN){
                        if(article1_ar.getText().toString().isEmpty()){
                            article1_ar.setText(R.string.rune_stoneG);
                        }
                        else if(article2_ar.getText().toString().isEmpty()){
                            article2_ar.setText(R.string.rune_stoneG);
                        }
                        else{
                            article3_ar.setText(R.string.rune_stoneG);
                        }
                    }
                    if(colour == Colour.YELLOW){
                        if(article1_ar.getText().toString().isEmpty()){
                            article1_ar.setText(R.string.rune_stoneY);
                        }
                        else if(article2_ar.getText().toString().isEmpty()){
                            article2_ar.setText(R.string.rune_stoneY);
                        }
                        else{
                            article3_ar.setText(R.string.rune_stoneY);
                        }
                    }
                }
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

                for (Item item : items){
                    ItemType type = item.getItemType();
                    if(type == ItemType.HELM){
                        helm_mg.setText(R.string.helm);
                    }
                    if(type == ItemType.BOW){
                        shield_bow_falcon_mg.setText(R.string.bow);
                    }
                    if(type == ItemType.FALCON){
                        shield_bow_falcon_mg.setText(R.string.falcon);
                    }
                    if(type == ItemType.SHIELD){
                        shield_bow_falcon_mg.setText(R.string.shield);
                    }
                    if(type == ItemType.TELESCOPE){
                        if(article1_mg.getText().toString().isEmpty()){
                            article1_mg.setText(R.string.telescope);
                        }
                        else if(article2_mg.getText().toString().isEmpty()){
                            article2_mg.setText(R.string.telescope);
                        }
                        else{
                            article3_mg.setText(R.string.telescope);
                        }
                    }
                    if(type == ItemType.WINESKIN){
                        if(article1_mg.getText().toString().isEmpty()){
                            article1_mg.setText(R.string.wineskin);
                        }
                        else if(article2_mg.getText().toString().isEmpty()){
                            article2_mg.setText(R.string.wineskin);
                        }
                        else{
                            article3_mg.setText(R.string.wineskin);
                        }
                    }
                    if(type == ItemType.MEDICINAL_HERB){
                        if(article1_mg.getText().toString().isEmpty()){
                            article1_mg.setText(R.string.med_herb);
                        }
                        else if(article2_mg.getText().toString().isEmpty()){
                            article2_mg.setText(R.string.med_herb);
                        }
                        else{
                            article3_mg.setText(R.string.med_herb);
                        }
                    }
                    if(type == ItemType.WITCH_BREW){
                        if(article1_mg.getText().toString().isEmpty()){
                            article1_mg.setText(R.string.witchbrew);
                        }
                        else if(article2_mg.getText().toString().isEmpty()){
                            article2_mg.setText(R.string.witchbrew);
                        }
                        else{
                            article3_mg.setText(R.string.witchbrew);
                        }
                    }
                }
                ArrayList<RuneStone> runes = hero.getRuneStones();
                for (RuneStone rune : runes){
                    Colour colour = rune.getColour();
                    if(colour == Colour.BLUE){
                        if(article1_mg.getText().toString().isEmpty()){
                            article1_mg.setText(R.string.rune_stoneB);
                        }
                        else if(article2_mg.getText().toString().isEmpty()){
                            article2_mg.setText(R.string.rune_stoneB);
                        }
                        else{
                            article3_mg.setText(R.string.rune_stoneB);
                        }
                    }
                    if(colour == Colour.GREEN){
                        if(article1_mg.getText().toString().isEmpty()){
                            article1_mg.setText(R.string.rune_stoneG);
                        }
                        else if(article2_mg.getText().toString().isEmpty()){
                            article2_mg.setText(R.string.rune_stoneG);
                        }
                        else{
                            article3_mg.setText(R.string.rune_stoneG);
                        }
                    }
                    if(colour == Colour.YELLOW){
                        if(article1_mg.getText().toString().isEmpty()){
                            article1_mg.setText(R.string.rune_stoneY);
                        }
                        else if(article2_mg.getText().toString().isEmpty()){
                            article2_mg.setText(R.string.rune_stoneY);
                        }
                        else{
                            article3_mg.setText(R.string.rune_stoneY);
                        }
                    }
                }
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

                for (Item item : items){
                    ItemType type = item.getItemType();
                    if(type == ItemType.HELM){
                        helm_dw.setText(R.string.helm);
                    }
                    if(type == ItemType.BOW){
                        shield_bow_falcon_dw.setText(R.string.bow);
                    }
                    if(type == ItemType.FALCON){
                        shield_bow_falcon_dw.setText(R.string.falcon);
                    }
                    if(type == ItemType.SHIELD){
                        shield_bow_falcon_dw.setText(R.string.shield);
                    }
                    if(type == ItemType.TELESCOPE){
                        if(article1_dw.getText().toString().isEmpty()){
                            article1_dw.setText(R.string.telescope);
                        }
                        else if(article2_dw.getText().toString().isEmpty()){
                            article2_dw.setText(R.string.telescope);
                        }
                        else{
                            article3_dw.setText(R.string.telescope);
                        }
                    }
                    if(type == ItemType.WINESKIN){
                        if(article1_dw.getText().toString().isEmpty()){
                            article1_dw.setText(R.string.wineskin);
                        }
                        else if(article2_dw.getText().toString().isEmpty()){
                            article2_dw.setText(R.string.wineskin);
                        }
                        else{
                            article3_dw.setText(R.string.wineskin);
                        }
                    }
                    if(type == ItemType.MEDICINAL_HERB){
                        if(article1_dw.getText().toString().isEmpty()){
                            article1_dw.setText(R.string.med_herb);
                        }
                        else if(article2_dw.getText().toString().isEmpty()){
                            article2_dw.setText(R.string.med_herb);
                        }
                        else{
                            article3_dw.setText(R.string.med_herb);
                        }
                    }
                    if(type == ItemType.WITCH_BREW){
                        if(article1_dw.getText().toString().isEmpty()){
                            article1_dw.setText(R.string.witchbrew);
                        }
                        else if(article2_dw.getText().toString().isEmpty()){
                            article2_dw.setText(R.string.witchbrew);
                        }
                        else{
                            article3_dw.setText(R.string.witchbrew);
                        }
                    }
                }
                ArrayList<RuneStone> runes = hero.getRuneStones();
                for (RuneStone rune : runes){
                    Colour colour = rune.getColour();
                    if(colour == Colour.BLUE){
                        if(article1_dw.getText().toString().isEmpty()){
                            article1_dw.setText(R.string.rune_stoneB);
                        }
                        else if(article2_dw.getText().toString().isEmpty()){
                            article2_dw.setText(R.string.rune_stoneB);
                        }
                        else{
                            article3_dw.setText(R.string.rune_stoneB);
                        }
                    }
                    if(colour == Colour.GREEN){
                        if(article1_dw.getText().toString().isEmpty()){
                            article1_dw.setText(R.string.rune_stoneG);
                        }
                        else if(article2_dw.getText().toString().isEmpty()){
                            article2_dw.setText(R.string.rune_stoneG);
                        }
                        else{
                            article3_dw.setText(R.string.rune_stoneG);
                        }
                    }
                    if(colour == Colour.YELLOW){
                        if(article1_dw.getText().toString().isEmpty()){
                            article1_dw.setText(R.string.rune_stoneY);
                        }
                        else if(article2_dw.getText().toString().isEmpty()){
                            article2_dw.setText(R.string.rune_stoneY);
                        }
                        else{
                            article3_dw.setText(R.string.rune_stoneY);
                        }
                    }
                }
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