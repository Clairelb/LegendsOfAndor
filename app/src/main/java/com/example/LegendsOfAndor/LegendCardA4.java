package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LegendCardA4 extends AppCompatActivity {

    private Button nextLegendCardBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend_card_a4);

        nextLegendCardBTN = findViewById(R.id.next_legend_card_a4);

        nextLegendCardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LegendCardA4.this, LegendCardA5.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {}
}
