package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class DistributeItemsWaitPage extends AppCompatActivity {

    private Thread t;
    Game updatedGame = null;

    @Override
    public void onBackPressed(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distribute_items_wait_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final MyPlayer myPlayer = MyPlayer.getInstance();

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()){
                    try{
                        final HttpResponse<String> response = Unirest.get("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getPlayer().getUsername() + "/getGameUpdate")
                                .asString();
                        if(response.getCode()==200){
                            final Game game = new Gson().fromJson(response.getBody(),Game.class);
                            System.out.println("============================= CORRECT RESPONSE CODE");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!game.isItemsDistributed()){
                                        Toast.makeText(DistributeItemsWaitPage.this,"Please wait. Host is still deciding item distribution", Toast.LENGTH_LONG).show();
                                    }else{
                                        updatedGame = game;
                                        interruptThreadAndStartActivity();
                                    }
                                }
                            });

                        }else{
                            System.out.println("======================= > WAITING ON ITEM DISTRIBUTION ");
                        }
                    }catch(Exception e){
                        if(e instanceof  InterruptedException){
                            Thread.currentThread().interrupt();
                        }
                        e.printStackTrace();
                    }


                }
            }
        });
        t.start();

        Button chatb = findViewById(R.id.distribute_items_chatb);
        chatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ChatScreen.class);
                startActivity(myIntent);
            }
        });
    }

    public void interruptThreadAndStartActivity() {
        int toastDurationInMilliSeconds = 1000000;
        final Toast toastToShow = Toast.makeText(DistributeItemsWaitPage.this,"Item distribution is as follows: \n " + updatedGame.getItemsDistributedMessage() + " Welcome to the Game. Good Luck" , Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
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
        t.interrupt();
    }
}
