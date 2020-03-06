package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class InteractItem extends AppCompatActivity {

    private Spinner dropSpinner;
    private Spinner pickUpSpinner;
    MyPlayer myPlayer = MyPlayer.getInstance();
    int playerGold = myPlayer.getGame().getSinglePlayer(myPlayer.getPlayer().getUsername()).getHero().getGold();
    int goldOnGround = 4; //NEED TO CHANGE FOR SERVER VALUE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interact_item);

        //Updates corresponding spinner for drop gold
        ArrayList<Integer> spinnerGoldDrop = new ArrayList<Integer>();
        for (int i = 0; i < playerGold; i++) {
            spinnerGoldDrop.add(i+1);
        }
        dropSpinner = (Spinner) findViewById(R.id.dropSpinner);
        ArrayAdapter<Integer> adapterDrop = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerGoldDrop);
        adapterDrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropSpinner.setAdapter(adapterDrop);

        //Updates corresponding spinner for pick up gold
        ArrayList<Integer> spinnerPickUpGold = new ArrayList<Integer>();
        for (int i = 0; i < goldOnGround; i++) {
            spinnerPickUpGold.add(i+1);
        }
        pickUpSpinner = (Spinner) findViewById(R.id.pickUpSpinner);
        ArrayAdapter<Integer> adapterPick = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerPickUpGold);
        adapterPick.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropSpinner.setAdapter(adapterPick);
    }
}
