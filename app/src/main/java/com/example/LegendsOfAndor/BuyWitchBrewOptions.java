package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BuyWitchBrewOptions extends AppCompatActivity {
    private Button yesBTN;
    private Button noBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_witch_brew_options);

        yesBTN = findViewById(R.id.buy_witch_brew_options_yes);
        noBTN = findViewById(R.id.buy_witch_brew_options_no);

        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyWitchBrewOptions.this, BuyWitchBrew.class);
                startActivity(intent);
            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyWitchBrewOptions.this, Board.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {}
}