package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsTab extends AppCompatActivity {

    @Override
    public void onBackPressed(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button backb = (Button) findViewById(R.id.backb);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Board.class);
                startActivity(myIntent);
            }
        });

        Button characterb = (Button) findViewById(R.id.characterb);
        characterb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Character_Tab.class);
                startActivity(myIntent);
            }
        });

        Button actionsb = (Button) findViewById(R.id.actionsb);
        actionsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Free_Actions.class);
                startActivity(myIntent);
            }
        });

        Button interactItem = (Button) findViewById(R.id.interactItem);
        interactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), InteractItem.class);
                startActivity(myIntent);
            }
        });

    }
}
