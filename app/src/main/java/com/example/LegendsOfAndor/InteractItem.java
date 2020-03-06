package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class InteractItem extends AppCompatActivity {

    private Spinner spinner;
    MyPlayer myPlayer = MyPlayer.getInstance();
    int playerGold = myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getGold();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interact_item);

        ArrayList<Integer> spinnerGoldDrop = new ArrayList<Integer>();
        for (int i = 0; i < playerGold; i++) {
            spinnerGoldDrop.add(i+1);
        }
        spinner = (Spinner) findViewById(R.id.dropSpinner);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerGoldDrop);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
