package com.example.LegendsOfAndor;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

enum PendingFalconTradeResponses {
    HERO_IN_FALCON_TRADE, PENDING_TRADE_SUCCESS,PENDING_TRADE_FAILURE, HERO_AWAITING_FALCON_TRADE
}


public class FalconTradeSelectHero extends AppCompatActivity {

    private ImageButton archer;
    private ImageButton warrior;
    private ImageButton dwarf;
    private ImageButton wizard;
    private TextView selectedHero;
    private HeroClass heroSelected;
    private Button confirm;
    private MyPlayer myPlayer;
    private TextView pleaseWait;
    private TextView headerText;
    private Thread t;
    private boolean threadTerminated = false;
    boolean confirmedHeroSelection = false;
    Button back;

    @Override
    public void onBackPressed(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.falcon_trade_select_hero);

        archer = findViewById(R.id.falcon_trade_archer);
        warrior = findViewById(R.id.falcon_trade_warrior);
        dwarf = findViewById(R.id.falcon_trade_dwarf);
        wizard = findViewById(R.id.falcon_trade_wizard);
        archer.setVisibility(View.INVISIBLE);
        warrior.setVisibility(View.INVISIBLE);
        dwarf.setVisibility(View.INVISIBLE);
        wizard.setVisibility(View.INVISIBLE);
        pleaseWait = findViewById(R.id.please_wait_for_falcon_hero);
        headerText = findViewById(R.id.select_hero_for_falcon_trade);
        selectedHero = findViewById(R.id.selected_hero_for_falcon_trade);
        confirm = findViewById(R.id.confirm_falcon_hero_selection);
        myPlayer = MyPlayer.getInstance();
        back = findViewById(R.id.select_falcon_hero_backb);


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

        Game game = myPlayer.getGame();
        for(int i = 0; i < game.getCurrentNumPlayers(); i++){
            if(game.getPlayers()[i].getHero().getHeroClass() == myPlayer.getPlayer().getHero().getHeroClass()){
                continue;
            }
            if(game.getPlayers()[i].getHero().getHeroClass() == HeroClass.WIZARD){
                wizard.setVisibility(View.VISIBLE);
            }
            if(game.getPlayers()[i].getHero().getHeroClass() == HeroClass.DWARF){
                dwarf.setVisibility(View.VISIBLE);
            }
            if(game.getPlayers()[i].getHero().getHeroClass() == HeroClass.WARRIOR){
                warrior.setVisibility(View.VISIBLE);
            }
            if(game.getPlayers()[i].getHero().getHeroClass() == HeroClass.ARCHER){
                archer.setVisibility(View.VISIBLE);
            }
        }

        archer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHero.setText("SELECTED: Archer");
                heroSelected = HeroClass.ARCHER;
            }
        });

        warrior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHero.setText("SELECTED: Warrior");
                heroSelected = HeroClass.WARRIOR;
            }
        });

        dwarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHero.setText("SELECTED: Dwarf");
                heroSelected = HeroClass.DWARF;
            }
        });

        wizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedHero.setText("SELECTED: Wizard");
                heroSelected = HeroClass.WIZARD;
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heroSelected == null){
                    Toast.makeText(FalconTradeSelectHero.this, "Please select hero to trade with", Toast.LENGTH_LONG).show();
                }else{
                    try{
                        AsyncTask<String, Void,PendingFalconTradeResponses> asyncTask;
                        PendingFalconTradeResponses response;
                        SendTradeRequest sendTradeRequest = new SendTradeRequest();
                        asyncTask = sendTradeRequest.execute(new Gson().toJson(heroSelected));
                        response = asyncTask.get();
                        if(response == PendingFalconTradeResponses.PENDING_TRADE_SUCCESS){
                            pleaseWait.setVisibility(View.VISIBLE);
                            archer.setVisibility(View.INVISIBLE);
                            warrior.setVisibility(View.INVISIBLE);
                            dwarf.setVisibility(View.INVISIBLE);
                            wizard.setVisibility(View.INVISIBLE);
                            selectedHero.setVisibility(View.INVISIBLE);
                            confirm.setVisibility(View.INVISIBLE);
                            headerText.setVisibility(View.INVISIBLE);
                            confirmedHeroSelection = true;
                            t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    myPlayer = MyPlayer.getInstance();
                                    while(!threadTerminated && confirmedHeroSelection){
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
                                                        if(myPlayer.getPlayer().getHero().getFalconTradeStatus() == FalconTradeStatus.IN_TRADE){
                                                            Toast.makeText(FalconTradeSelectHero.this, "Joining Falcon Trade", Toast.LENGTH_LONG).show();
                                                            Intent myIntent = new Intent(FalconTradeSelectHero.this, FalconTrade.class);
                                                            Bundle extras = new Bundle();
                                                            if(myPlayer.getPlayer().getHero().getHeroClass() == HeroClass.WIZARD){
                                                                extras.putString("p1","wizard");
                                                            }else if(myPlayer.getPlayer().getHero().getHeroClass() == HeroClass.DWARF){
                                                                extras.putString("p1","dwarf");
                                                            }else if(myPlayer.getPlayer().getHero().getHeroClass() == HeroClass.WARRIOR){
                                                                extras.putString("p1","warrior");
                                                            }else if(myPlayer.getPlayer().getHero().getHeroClass() == HeroClass.ARCHER){
                                                                extras.putString("p1","archer");
                                                            }
                                                            if(heroSelected == HeroClass.WIZARD){
                                                                extras.putString("p2","wizard");
                                                            }else if(heroSelected == HeroClass.DWARF){
                                                                extras.putString("p2","dwarf");
                                                            }else if(heroSelected == HeroClass.WARRIOR){
                                                                extras.putString("p2","warrior");
                                                            }else if(heroSelected == HeroClass.ARCHER){
                                                                extras.putString("p2","archer");
                                                            }
                                                            threadTerminated = true;
                                                            myIntent.putExtras(extras);
                                                            startActivity(myIntent);
                                                            finish();
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
                        }else if (response == PendingFalconTradeResponses.HERO_IN_FALCON_TRADE){
                            Toast.makeText(FalconTradeSelectHero.this, "Hero already in falcon trade", Toast.LENGTH_LONG).show();
                        } else if (response == PendingFalconTradeResponses.HERO_AWAITING_FALCON_TRADE) {
                            Toast.makeText(FalconTradeSelectHero.this, "Hero is awaiting a falcon trade already", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FalconTradeSelectHero.this, "Request Failure. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmedHeroSelection){
                    AsyncTask<String,Void,Void> asyncTask;
                    LeaveFalconTrade leaveFalconTrade = new LeaveFalconTrade();
                    asyncTask = leaveFalconTrade.execute(new Gson().toJson(heroSelected));
                }
                threadTerminated = true;
                startActivity(new Intent(FalconTradeSelectHero.this, UseArticle.class));
                finish();
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

    private static class SendTradeRequest extends AsyncTask<String, Void, PendingFalconTradeResponses > {
        @Override
        protected PendingFalconTradeResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/sendFalconTradeRequest")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

                String resultAsJsonString = response.getBody();
                System.out.println("RESPONSE BODY " + response.getBody());
                return new Gson().fromJson(resultAsJsonString, PendingFalconTradeResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class LeaveFalconTrade extends AsyncTask<String, Void, Void > {
        @Override
        protected Void doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/leaveFalconTrade")
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
