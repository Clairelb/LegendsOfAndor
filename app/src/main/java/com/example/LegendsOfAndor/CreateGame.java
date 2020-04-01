package com.example.LegendsOfAndor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


enum LogoutResponses {
    LOGOUT_SUCCESS, LOGOUT_FAILURE
}

public class CreateGame extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Opens host page with host options
        Button btn = (Button)findViewById(R.id.host_game);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateGame.this, HostLobby.class));
            }
        });

        //Opens join page with possible lobbies available
        Button btn2 = (Button)findViewById(R.id.join_game);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateGame.this, JoinLobby.class));
            }
        });

        final Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String,Void,LogoutResponses> asyncTask;
                LogoutResponses logoutResponse;
                MyPlayer.getInstance().getPlayer();
                try{
                    CreateGame.LogoutSender logoutSender = new CreateGame.LogoutSender();
                    asyncTask = logoutSender.execute(new Gson().toJson(MyPlayer.getInstance().getPlayer()));
                    logoutResponse = asyncTask.get();
                    if(logoutResponse == LogoutResponses.LOGOUT_FAILURE){
                        Toast.makeText(CreateGame.this, "Logout Failure", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(CreateGame.this, "Logout Success", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CreateGame.this, Login.class));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private static class LogoutSender extends AsyncTask<String, Void, LogoutResponses> {
        @Override
        protected LogoutResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() +":8080/logout")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, LogoutResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
