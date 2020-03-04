package com.example.LegendsOfAndor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GameOver extends AppCompatActivity {
    private Button returnToLobby;
    private TextView gameOverText;
    MyPlayer myPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        myPlayer = MyPlayer.getInstance();

        returnToLobby = findViewById(R.id.returnToLobby);
        returnToLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), CreateGame.class);
                startActivity(myIntent);
            }
        });

        gameOverText = findViewById(R.id.gameOverMessage);
        Game finalGame = myPlayer.getGame();
        if(finalGame.getGameStatus() == GameStatus.GAME_WON){
            gameOverText.setText("CONGRATULATIONS. YOU HAVE WON THE GAME");
        }else if(finalGame.getGameStatus() == GameStatus.GAME_LOST){
            gameOverText.setText("GAME OVER. YOU HAVE LOST");
        }else{
            gameOverText.setText("GAME OVER. GAME ERROR");
        }

        try {
            SendGameOver gameOverSender = new SendGameOver();
            gameOverSender.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private static class SendGameOver extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;
            try {
                response = Unirest.delete("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/gameOver")
                        .asString();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return "";
        }
    }


}
