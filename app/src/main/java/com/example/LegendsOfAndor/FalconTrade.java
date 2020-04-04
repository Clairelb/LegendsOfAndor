package com.example.LegendsOfAndor;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

public class FalconTrade extends AppCompatActivity {

    Spinner p1_gold;
    Spinner p1_runestone_blue;
    Spinner p1_wineskin;
    Spinner p1_runestone_green;
    Spinner p1_telescope;
    Spinner p1_helm;
    Spinner p1_medicinal_herb;
    Spinner p1_witch_brew;
    Spinner p1_runestone_yellow;

    ImageButton p1_pic_gold;
    ImageButton p1_pic_runestone_blue;
    ImageButton p1_pic_wineskin;
    ImageButton p1_pic_runestone_green;
    ImageButton p1_pic_telescope;
    ImageButton p1_pic_helm;
    ImageButton p1_pic_medicinal_herb;
    ImageButton p1_pic_witch_brew;
    ImageButton p1_pic_runestone_yellow;

    Spinner p2_gold;
    Spinner p2_runestone_blue;
    Spinner p2_wineskin;
    Spinner p2_runestone_green;
    Spinner p2_telescope;
    Spinner p2_helm;
    Spinner p2_medicinal_herb;
    Spinner p2_witch_brew;
    Spinner p2_runestone_yellow;

    ImageButton p2_pic_gold;
    ImageButton p2_pic_runestone_blue;
    ImageButton p2_pic_wineskin;
    ImageButton p2_pic_runestone_green;
    ImageButton p2_pic_telescope;
    ImageButton p2_pic_helm;
    ImageButton p2_pic_medicinal_herb;
    ImageButton p2_pic_witch_brew;
    ImageButton p2_pic_runestone_yellow;
    MyPlayer myPlayer;
    TextView p1_heroclass;
    TextView p2_heroclass;
    HeroClass p2_hero_class;
    HeroClass p1_hero_class;
    private Thread t;
    private boolean threadTerminated = false;
    FalconTradeObject clientTrade;
    Boolean isPlayer1;
    Button p1_confirm;
    Button p2_confirm;
    TextView p1_hasConfirmed;
    TextView p2_hasConfirmed;
    Button trade;



    @Override
    public void onBackPressed(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.falcon_trade);

        p1_gold = findViewById(R.id.p1_gold);
        p1_runestone_yellow = findViewById(R.id.p1_runestone_yellow);
        p1_runestone_blue = findViewById(R.id.p1_runestone_blue);
        p1_runestone_green = findViewById(R.id.p1_runestone_green);
        p1_helm = findViewById(R.id.p1_helm);
        p1_medicinal_herb = findViewById(R.id.p1_medicinal_herb);
        p1_wineskin = findViewById(R.id.p1_wineskin);
        p1_telescope = findViewById(R.id.p1_telescope);
        p1_witch_brew = findViewById(R.id.p1_witch_brew);

        p1_gold.setVisibility(View.INVISIBLE);
        p1_runestone_yellow.setVisibility(View.INVISIBLE);
        p1_runestone_blue.setVisibility(View.INVISIBLE);
        p1_runestone_green.setVisibility(View.INVISIBLE);
        p1_helm.setVisibility(View.INVISIBLE);
        p1_medicinal_herb.setVisibility(View.INVISIBLE);
        p1_wineskin.setVisibility(View.INVISIBLE);
        p1_telescope.setVisibility(View.INVISIBLE);
        p1_witch_brew.setVisibility(View.INVISIBLE);

        p1_pic_gold = findViewById(R.id.p1_gold_pic);
        p1_pic_runestone_yellow = findViewById(R.id.p1_runestone_yellow_pic);
        p1_pic_runestone_blue = findViewById(R.id.p1_runestone_blue_pic);
        p1_pic_runestone_green = findViewById(R.id.p1_runestone_green_pic);
        p1_pic_helm = findViewById(R.id.p1_helm_pic);
        p1_pic_medicinal_herb = findViewById(R.id.p1_medicinal_herb_pic);
        p1_pic_wineskin = findViewById(R.id.p1_wineskin_pic);
        p1_pic_telescope = findViewById(R.id.p1_telescope_pic);
        p1_pic_witch_brew = findViewById(R.id.p1_witch_brew_pic);

        p1_pic_gold.setVisibility(View.INVISIBLE);
        p1_pic_runestone_yellow.setVisibility(View.INVISIBLE);
        p1_pic_runestone_blue.setVisibility(View.INVISIBLE);
        p1_pic_runestone_green.setVisibility(View.INVISIBLE);
        p1_pic_helm.setVisibility(View.INVISIBLE);
        p1_pic_medicinal_herb.setVisibility(View.INVISIBLE);
        p1_pic_wineskin.setVisibility(View.INVISIBLE);
        p1_pic_telescope.setVisibility(View.INVISIBLE);
        p1_pic_witch_brew.setVisibility(View.INVISIBLE);

        p2_gold = findViewById(R.id.p2_gold);
        p2_runestone_yellow = findViewById(R.id.p2_runestone_yellow);
        p2_runestone_blue = findViewById(R.id.p2_runestone_blue);
        p2_runestone_green = findViewById(R.id.p2_runestone_green);
        p2_helm = findViewById(R.id.p2_helm);
        p2_medicinal_herb = findViewById(R.id.p2_medicinal_herb);
        p2_wineskin = findViewById(R.id.p2_wineskin);
        p2_telescope = findViewById(R.id.p2_telescope);
        p2_witch_brew = findViewById(R.id.p2_witch_brew);

        p2_gold.setVisibility(View.INVISIBLE);
        p2_runestone_yellow.setVisibility(View.INVISIBLE);
        p2_runestone_blue.setVisibility(View.INVISIBLE);
        p2_runestone_green.setVisibility(View.INVISIBLE);
        p2_helm.setVisibility(View.INVISIBLE);
        p2_medicinal_herb.setVisibility(View.INVISIBLE);
        p2_wineskin.setVisibility(View.INVISIBLE);
        p2_telescope.setVisibility(View.INVISIBLE);
        p2_witch_brew.setVisibility(View.INVISIBLE);

        p2_pic_gold = findViewById(R.id.p2_gold_pic);
        p2_pic_runestone_yellow = findViewById(R.id.p2_runestone_yellow_pic);
        p2_pic_runestone_blue = findViewById(R.id.p2_runestone_blue_pic);
        p2_pic_runestone_green = findViewById(R.id.p2_runestone_green_pic);
        p2_pic_helm = findViewById(R.id.p2_helm_pic);
        p2_pic_medicinal_herb = findViewById(R.id.p2_medicinal_herb_pic);
        p2_pic_wineskin = findViewById(R.id.p2_wineskin_pic);
        p2_pic_telescope = findViewById(R.id.p2_telescope_pic);
        p2_pic_witch_brew = findViewById(R.id.p2_witch_brew_pic);

        p2_pic_gold.setVisibility(View.INVISIBLE);
        p2_pic_runestone_yellow.setVisibility(View.INVISIBLE);
        p2_pic_runestone_blue.setVisibility(View.INVISIBLE);
        p2_pic_runestone_green.setVisibility(View.INVISIBLE);
        p2_pic_helm.setVisibility(View.INVISIBLE);
        p2_pic_medicinal_herb.setVisibility(View.INVISIBLE);
        p2_pic_wineskin.setVisibility(View.INVISIBLE);
        p2_pic_telescope.setVisibility(View.INVISIBLE);
        p2_pic_witch_brew.setVisibility(View.INVISIBLE);
        myPlayer = MyPlayer.getInstance();
        p1_heroclass = findViewById(R.id.p1_heroclass);
        p2_heroclass = findViewById(R.id.p2_heroclass);
        p2_hero_class = null;
        p1_hero_class = null;
        p1_confirm = findViewById(R.id.p1_confirm);
        p2_confirm = findViewById(R.id.p2_confirm);
        p1_hasConfirmed = findViewById(R.id.p1_has_confirmed);
        p2_hasConfirmed = findViewById(R.id.p2_has_confirmed);
        trade = findViewById(R.id.trade_items_falcon);


        try{
            AsyncTask<String, Void, Game> asyncTaskGame;
            Game gameToSet;
            GetGame getGame = new GetGame();
            asyncTaskGame = getGame.execute();
            gameToSet = asyncTaskGame.get();
            System.out.println(gameToSet);
            myPlayer.setGame(gameToSet);
            for(int i = 0; i < gameToSet.getCurrentNumPlayers(); i++){
                if(gameToSet.getPlayers()[i].getUsername().equals(myPlayer.getPlayer().getUsername())){
                    myPlayer.setPlayer(gameToSet.getPlayers()[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        String p1_hero_string = getIntent().getExtras().getString("p1");
        if(p1_hero_string != null){
            p1_hero_class = getHeroClass(p1_hero_string);
            p1_heroclass.setText(p1_hero_string);
        }

        String p2_hero_string = getIntent().getExtras().getString("p2");
        if(p2_hero_string != null){
            p2_hero_class = getHeroClass(p2_hero_string);
            p2_heroclass.setText(p2_hero_string);
        }

        if(p1_hero_class != null){
            if(p1_hero_class == myPlayer.getPlayer().getHero().getHeroClass()){
                isPlayer1 = true;
            }else{
                isPlayer1 = false;
            }
            Hero p1_hero = null;
            for(int i = 0; i < myPlayer.getGame().getCurrentNumPlayers(); i++){
                if(myPlayer.getGame().getPlayers()[i].getHero().getHeroClass() == p1_hero_class){
                    p1_hero = myPlayer.getGame().getPlayers()[i].getHero();
                    clientTrade = myPlayer.getGame().getPlayers()[i].getHero().getCurrentFalconTrade();
                    break;
                }
            }
            if(p1_hero != null){
                int p1_num_helms = getNumItems(p1_hero, ItemType.HELM);
                int p1_num_telescopes = getNumItems(p1_hero, ItemType.TELESCOPE);
                int p1_num_wineskins = getNumItems(p1_hero, ItemType.WINESKIN);
                int p1_num_medicinal_herbs = getNumItems(p1_hero, ItemType.MEDICINAL_HERB);
                int p1_num_witch_brew = getNumItems(p1_hero, ItemType.WITCH_BREW);
                int p1_num_gold = p1_hero.getGold();
                int p1_num_runestone_yellow = 0;
                int p1_num_runestone_blue = 0;
                int p1_num_runestone_green = 0;
                for(RuneStone runeStone : p1_hero.getRuneStones()){
                    if(runeStone.getColour() == Colour.YELLOW){
                        p1_num_runestone_yellow++;
                    }
                    if(runeStone.getColour() == Colour.BLUE){
                        p1_num_runestone_blue++;
                    }
                    if(runeStone.getColour() == Colour.GREEN){
                        p1_num_runestone_green++;
                    }
                }

                if(p1_num_helms > 0){
                    p1_pic_helm.setVisibility(View.VISIBLE);
                    p1_helm.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_helms; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_helm.setAdapter(adapterDrop);
                }

                if(p1_num_telescopes > 0){
                    p1_pic_telescope.setVisibility(View.VISIBLE);
                    p1_telescope.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_telescopes; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_telescope.setAdapter(adapterDrop);
                }

                if(p1_num_wineskins > 0){
                    p1_pic_wineskin.setVisibility(View.VISIBLE);
                    p1_wineskin.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_wineskins; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_wineskin.setAdapter(adapterDrop);
                }

                if(p1_num_medicinal_herbs > 0){
                    p1_pic_medicinal_herb.setVisibility(View.VISIBLE);
                    p1_medicinal_herb.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_medicinal_herbs; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_medicinal_herb.setAdapter(adapterDrop);
                }

                if(p1_num_witch_brew > 0){
                    p1_pic_witch_brew.setVisibility(View.VISIBLE);
                    p1_witch_brew.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_witch_brew; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_witch_brew.setAdapter(adapterDrop);
                }

                if(p1_num_gold > 0){
                    p1_pic_gold.setVisibility(View.VISIBLE);
                    p1_gold.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_gold; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_gold.setAdapter(adapterDrop);
                }

                if(p1_num_runestone_blue > 0){
                    p1_pic_runestone_blue.setVisibility(View.VISIBLE);
                    p1_runestone_blue.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_runestone_blue; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_runestone_blue.setAdapter(adapterDrop);
                }

                if(p1_num_runestone_yellow > 0){
                    p1_pic_runestone_yellow.setVisibility(View.VISIBLE);
                    p1_runestone_yellow.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_runestone_yellow; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_runestone_yellow.setAdapter(adapterDrop);
                }

                if(p1_num_runestone_green > 0){
                    p1_pic_runestone_green.setVisibility(View.VISIBLE);
                    p1_runestone_green.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p1_num_runestone_green; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p1_runestone_green.setAdapter(adapterDrop);
                }
            }
        }

        if(p2_hero_class != null){
            Hero p2_hero = null;
            for(int i = 0; i < myPlayer.getGame().getCurrentNumPlayers(); i++){
                if(myPlayer.getGame().getPlayers()[i].getHero().getHeroClass() == p2_hero_class){
                    p2_hero = myPlayer.getGame().getPlayers()[i].getHero();
                    break;
                }
            }
            if(p2_hero != null){
                int p2_num_helms = getNumItems(p2_hero, ItemType.HELM);
                int p2_num_telescopes = getNumItems(p2_hero, ItemType.TELESCOPE);
                int p2_num_wineskins = getNumItems(p2_hero, ItemType.WINESKIN);
                int p2_num_medicinal_herbs = getNumItems(p2_hero, ItemType.MEDICINAL_HERB);
                int p2_num_witch_brew = getNumItems(p2_hero, ItemType.WITCH_BREW);
                int p2_num_gold = p2_hero.getGold();
                int p2_num_runestone_yellow = 0;
                int p2_num_runestone_blue = 0;
                int p2_num_runestone_green = 0;
                for(RuneStone runeStone : p2_hero.getRuneStones()){
                    if(runeStone.getColour() == Colour.YELLOW){
                        p2_num_runestone_yellow++;
                    }
                    if(runeStone.getColour() == Colour.BLUE){
                        p2_num_runestone_blue++;
                    }
                    if(runeStone.getColour() == Colour.GREEN){
                        p2_num_runestone_green++;
                    }
                }

                if(p2_num_helms > 0){
                    p2_pic_helm.setVisibility(View.VISIBLE);
                    p2_helm.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_helms; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_helm.setAdapter(adapterDrop);
                }

                if(p2_num_telescopes > 0){
                    p2_pic_telescope.setVisibility(View.VISIBLE);
                    p2_telescope.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_telescopes; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_telescope.setAdapter(adapterDrop);
                }

                if(p2_num_wineskins > 0){
                    p2_pic_wineskin.setVisibility(View.VISIBLE);
                    p2_wineskin.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_wineskins; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_wineskin.setAdapter(adapterDrop);
                }

                if(p2_num_medicinal_herbs > 0){
                    p2_pic_medicinal_herb.setVisibility(View.VISIBLE);
                    p2_medicinal_herb.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_medicinal_herbs; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_medicinal_herb.setAdapter(adapterDrop);
                }

                if(p2_num_witch_brew > 0){
                    p2_pic_witch_brew.setVisibility(View.VISIBLE);
                    p2_witch_brew.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_witch_brew; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_witch_brew.setAdapter(adapterDrop);
                }

                if(p2_num_gold > 0){
                    p2_pic_gold.setVisibility(View.VISIBLE);
                    p2_gold.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_gold; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_gold.setAdapter(adapterDrop);
                }

                if(p2_num_runestone_blue > 0){
                    p2_pic_runestone_blue.setVisibility(View.VISIBLE);
                    p2_runestone_blue.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_runestone_blue; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_runestone_blue.setAdapter(adapterDrop);
                }

                if(p2_num_runestone_yellow > 0){
                    p2_pic_runestone_yellow.setVisibility(View.VISIBLE);
                    p2_runestone_yellow.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_runestone_yellow; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_runestone_yellow.setAdapter(adapterDrop);
                }

                if(p2_num_runestone_green > 0){
                    p2_pic_runestone_green.setVisibility(View.VISIBLE);
                    p2_runestone_green.setVisibility(View.VISIBLE);
                    ArrayList<Integer> values = new ArrayList<>();
                    values.add(0);
                    for(int i = 0; i < p2_num_runestone_green; i++){
                        values.add(i+1);
                    }
                    ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    p2_runestone_green.setAdapter(adapterDrop);
                }
            }
        }

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                myPlayer = MyPlayer.getInstance();
                while(!threadTerminated){
                    try{
                        final HttpResponse<String> httpResponse = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getGameUpdate")
                                .asString();
                        if(httpResponse.getCode() == 200){
                            final Game game = new Gson().fromJson(httpResponse.getBody(), Game.class);
                            MyPlayer.getInstance().setGame(game);
                            for(int i = 0; i < game.getCurrentNumPlayers(); i++){
                                if(game.getPlayers()[i].getUsername().equals(myPlayer.getPlayer().getUsername())){
                                    myPlayer.setPlayer(game.getPlayers()[i]);
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int i = 0; i < game.getCurrentNumPlayers(); i++){
                                        if(game.getPlayers()[i].getHero().getHeroClass() == p1_hero_class){
                                            FalconTradeObject currentTrade = game.getPlayers()[i].getHero().getCurrentFalconTrade();
                                            p1_gold.setSelection(getIndex(p1_gold, currentTrade.getP1_gold()));
                                            p1_wineskin.setSelection(getIndex(p1_wineskin, currentTrade.getP1_wineskin()));
                                            p1_runestone_blue.setSelection(getIndex(p1_runestone_blue, currentTrade.getP1_runestone_blue()));
                                            p1_runestone_green.setSelection(getIndex(p1_runestone_green, currentTrade.getP1_runestone_green()));
                                            p1_runestone_yellow.setSelection(getIndex(p1_runestone_yellow,currentTrade.getP1_runestone_yellow()));
                                            p1_telescope.setSelection(getIndex(p1_telescope, currentTrade.getP1_telescope()));
                                            p1_helm.setSelection(getIndex(p1_helm, currentTrade.getP1_helm()));
                                            p1_medicinal_herb.setSelection(getIndex(p1_medicinal_herb, currentTrade.getP1_medicinal_herb()));
                                            p1_witch_brew.setSelection(getIndex(p1_witch_brew,currentTrade.getP1_witch_brew()));
                                            p2_gold.setSelection(getIndex(p2_gold, currentTrade.getP2_gold()));
                                            p2_wineskin.setSelection(getIndex(p2_wineskin, currentTrade.getP2_wineskin()));
                                            p2_runestone_blue.setSelection(getIndex(p2_runestone_blue, currentTrade.getP2_runestone_blue()));
                                            p2_runestone_green.setSelection(getIndex(p2_runestone_green, currentTrade.getP2_runestone_green()));
                                            p2_runestone_yellow.setSelection(getIndex(p2_runestone_yellow,currentTrade.getP2_runestone_yellow()));
                                            p2_telescope.setSelection(getIndex(p2_telescope, currentTrade.getP2_telescope()));
                                            p2_helm.setSelection(getIndex(p2_helm, currentTrade.getP2_helm()));
                                            p2_medicinal_herb.setSelection(getIndex(p2_medicinal_herb, currentTrade.getP2_medicinal_herb()));
                                            p2_witch_brew.setSelection(getIndex(p2_witch_brew,currentTrade.getP2_witch_brew()));
                                            if(!currentTrade.isDontUpdate()){
                                                if(currentTrade.isP1_hasConfirmed()){
                                                    p1_hasConfirmed.setBackgroundColor(Color.GREEN);
                                                }else{
                                                    p1_hasConfirmed.setBackgroundColor(Color.RED);
                                                }
                                            }
                                            if(currentTrade.isP2_hasConfirmed()){
                                                p2_hasConfirmed.setBackgroundColor(Color.GREEN);
                                            }else{
                                                p2_hasConfirmed.setBackgroundColor(Color.RED);
                                            }
                                            currentTrade.setDontUpdate(false);
                                        }
                                    }
                                }
                            });
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

        if(!isPlayer1){
            p1_gold.setEnabled(false);
            p1_runestone_yellow.setEnabled(false);
            p1_runestone_blue.setEnabled(false);
            p1_runestone_green.setEnabled(false);
            p1_helm.setEnabled(false);
            p1_medicinal_herb.setEnabled(false);
            p1_wineskin.setEnabled(false);
            p1_telescope.setEnabled(false);
            p1_witch_brew.setEnabled(false);
            p2_gold.setEnabled(false);
            p2_runestone_yellow.setEnabled(false);
            p2_runestone_blue.setEnabled(false);
            p2_runestone_green.setEnabled(false);
            p2_helm.setEnabled(false);
            p2_medicinal_herb.setEnabled(false);
            p2_wineskin.setEnabled(false);
            p2_telescope.setEnabled(false);
            p2_witch_brew.setEnabled(false);
            p1_hasConfirmed.setEnabled(false);
            p1_confirm.setEnabled(false);
            p1_confirm.setVisibility(View.INVISIBLE);
            trade.setVisibility(View.INVISIBLE);
        }else{
            p2_hasConfirmed.setEnabled(false);
            p2_confirm.setEnabled(false);
            p2_confirm.setVisibility(View.INVISIBLE);
        }

        p1_gold.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_gold((Integer) p1_gold.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1_wineskin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_wineskin((Integer) p1_wineskin.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1_telescope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_telescope((Integer) p1_telescope.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1_helm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_helm((Integer) p1_helm.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1_medicinal_herb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_medicinal_herb((Integer) p1_medicinal_herb.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1_witch_brew.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_witch_brew((Integer) p1_witch_brew.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1_runestone_blue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_runestone_blue((Integer) p1_runestone_blue.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        p1_runestone_yellow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_runestone_yellow((Integer) p1_runestone_yellow.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1_runestone_green.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP1_runestone_green((Integer) p1_runestone_green.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p2_gold.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_gold((Integer) p2_gold.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p2_wineskin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_wineskin((Integer) p2_wineskin.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p2_telescope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_telescope((Integer) p2_telescope.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p2_helm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_helm((Integer) p2_helm.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p2_medicinal_herb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_medicinal_herb((Integer) p2_medicinal_herb.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p2_witch_brew.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_witch_brew((Integer) p2_witch_brew.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p2_runestone_blue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_runestone_blue((Integer) p2_runestone_blue.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        p2_runestone_yellow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_runestone_yellow((Integer) p2_runestone_yellow.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p2_runestone_green.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clientTrade.setP2_runestone_green((Integer) p2_runestone_green.getSelectedItem());
                if(!clientTrade.isDontUpdate()){
                    if(isPlayer1){
                        p1_hasConfirmed.setBackgroundColor(Color.RED);
                        p2_hasConfirmed.setBackgroundColor(Color.RED);
                        clientTrade.setP1_hasConfirmed(false);
                    }
                    clientTrade.setP2_hasConfirmed(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientTrade.setP1_hasConfirmed(true);
                sendUpdatedTradeObject(clientTrade);
            }
        });

        p2_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientTrade.setP2_hasConfirmed(true);
                clientTrade.setDontUpdate(true);
                sendUpdatedTradeObject(clientTrade);
            }
        });

        trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable p1_color = (ColorDrawable) p1_hasConfirmed.getBackground();
                ColorDrawable p2_color = (ColorDrawable) p2_hasConfirmed.getBackground();
                if(p1_color.getColor() == Color.GREEN && p2_color.getColor() == Color.GREEN){
                    Toast.makeText(FalconTrade.this,"READY TO TRADE", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(FalconTrade.this,"NOT READY TO TRADE. Hero 1: " + clientTrade.isP1_hasConfirmed() + " Hero 2: " + clientTrade.isP2_hasConfirmed(), Toast.LENGTH_LONG).show();
                }
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

    public static int getNumItems(Hero hero, ItemType type){
        int count = 0;
        for(Item item : hero.getItems()){
            if(item.getItemType() == type){
                count++;
            }
        }
        return count;
    }

    public static HeroClass getHeroClass(String heroString){
        if(heroString.equals("wizard")){
            return HeroClass.WIZARD;
        }else if(heroString.equals("warrior")){
            return HeroClass.WARRIOR;
        }else if (heroString.equals("archer")){
            return HeroClass.ARCHER;
        }else if(heroString.equals("dwarf")){
            return HeroClass.DWARF;
        }
        return null;
    }

    private int getIndex(Spinner spinner, Integer num){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i) == num){
                return i;
            }
        }
        return -1;
    }

    private static class SendFalconTradeObject extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/updateFalconTradeObject")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void sendUpdatedTradeObject(FalconTradeObject falconTradeObject){
        AsyncTask<String,Void,Void> asyncTask;
        SendFalconTradeObject sendFalconTradeObject = new SendFalconTradeObject();
        asyncTask = sendFalconTradeObject.execute(new Gson().toJson(falconTradeObject));
    }
}
