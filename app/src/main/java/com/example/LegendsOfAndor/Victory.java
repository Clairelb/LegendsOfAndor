package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Victory extends AppCompatActivity {
    private Button returnToLobby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        returnToLobby = findViewById(R.id.return_to_lobby_victory);
        returnToLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), CreateGame.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {}
}
