package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LegendCardA1 extends AppCompatActivity {

    private Button nextLegendCardBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend_card_a1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        nextLegendCardBTN = findViewById(R.id.next_legend_card_a1);

        nextLegendCardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LegendCardA1.this, LegendCardA2.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {}
}
