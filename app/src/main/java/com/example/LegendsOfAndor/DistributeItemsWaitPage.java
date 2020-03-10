package com.example.LegendsOfAndor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;

enum GameStartedResponses{
    GAME_STARTED, GAME_NOT_STARTED
}

public class DistributeItemsWaitPage extends AppCompatActivity {

    private Thread t;
    final MyPlayer myPlayer = MyPlayer.getInstance();
    Handler mHandler;
    @Override
    public void onBackPressed(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distribute_items_wait_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_runnable,5000);








//        t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(!Thread.currentThread().isInterrupted()){
//                    try{
//                        final HttpResponse<String> response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getGameUpdate")
//                                .asString();
//                        if(response.getCode()==200) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    final Game game = new Gson().fromJson(response.getBody(), Game.class);
//                                    if (game.isItemsDistributed()) {
//                                        myPlayer.setGame(game);
//                                        interruptThreadAndStartActivity(true);
//                                    } else {
//                                        System.out.println("======================= > WAITING ON ITEM DISTRIBUTION ");
//                                    }
//                                }
//                            });
//                        }
//                    }catch(Exception e){
//                        if(e instanceof  InterruptedException){
//                            Thread.currentThread().interrupt();
//                        }
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        t.start();

        Button chatb = findViewById(R.id.distribute_items_chatb);
        chatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interruptThreadAndStartChat(false);

            }
        });
    }

    private final Runnable m_runnable = new Runnable() {
        @Override
        public void run() {
            AsyncTask<String, Void, GameStartedResponses> asyncTask;
            GameStartedResponses gameStartedResponses;
            try{
                GameStartedCheck gameStartedCheck = new GameStartedCheck();
                asyncTask = gameStartedCheck.execute();
                gameStartedResponses = asyncTask.get();

                if(gameStartedResponses == null){
                    Toast.makeText(DistributeItemsWaitPage.this, "Error. No response from server.", Toast.LENGTH_LONG).show();
                }else if(gameStartedResponses == GameStartedResponses.GAME_NOT_STARTED){
                    Toast.makeText(DistributeItemsWaitPage.this, "Host has not decided item distribution please wait.", Toast.LENGTH_LONG).show();
                }else if(gameStartedResponses == GameStartedResponses.GAME_STARTED){
                    Toast.makeText(DistributeItemsWaitPage.this, "Host has decided Item distribution", Toast.LENGTH_LONG).show();
                    try{
                        AsyncTask<String, Void, Game> asyncTask1;
                        Game gameToSet;
                        GetGame getGame = new GetGame();
                        asyncTask1 = getGame.execute();
                        gameToSet = asyncTask1.get();
                        System.out.println(gameToSet);
                        myPlayer.setGame(gameToSet);
                        interruptThreadAndStartActivity(false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }catch(Exception e){
                e.printStackTrace();
            }
            DistributeItemsWaitPage.this.mHandler.postDelayed(m_runnable,5000);
        }
    };

    @Override
    protected void onPause(){
        super.onPause();
        mHandler.removeCallbacks(m_runnable);
    }
    public void interruptThreadAndStartActivity(Boolean isThreadRunning) {
        int toastDurationInMilliSeconds = 1000000;
        final Toast toastToShow = Toast.makeText(DistributeItemsWaitPage.this,"Item distribution is as follows: \n " + myPlayer.getGame().getItemsDistributedMessage() + " Welcome to the Game. Good Luck" , Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                toastToShow.show();
            }
            public void onFinish() {
                toastToShow.cancel();
            }
        };

        // Show the toast and starts the countdown
        toastToShow.show();
        toastCountDown.start();
        startActivity(new Intent(DistributeItemsWaitPage.this, Board.class));
        if(isThreadRunning){
            t.interrupt();
        }

    }

    public void interruptThreadAndStartChat(Boolean isThreadRunning) {
        Bundle bundle = new Bundle();
        bundle.putString("key1", new Gson().toJson(PreviousPage.DISTRIBUTE_ITEMS_WAIT_PAGE));
        Intent intent = new Intent(DistributeItemsWaitPage.this, ChatScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        startActivity(intent);

        if(isThreadRunning){
            t.interrupt();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
            startActivity(getIntent());
        }
    }

    private static class GameStartedCheck extends AsyncTask<String, Void, GameStartedResponses > {
        @Override
        protected GameStartedResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/isGameStarted")
                        .asString();

                String resultAsJsonString = response.getBody();
                System.out.println("RESPONSE BODY " + response.getBody());
                return new Gson().fromJson(resultAsJsonString, GameStartedResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
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
