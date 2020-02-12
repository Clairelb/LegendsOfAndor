package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


import com.github.chrisbanes.photoview.PhotoView;

public class Board extends AppCompatActivity {
    public ImageView warrior;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        warrior = findViewById(R.id.warrior);
        warrior.setX(100);  //get the color, then determine the location
        warrior.setY(100);

        Button move = (Button)findViewById(R.id.move);
        move.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setFlag();
            }
        });
        Button chatb = (Button)findViewById(R.id.chatb);
        chatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ChatScreen.class);
                startActivity(myIntent);
            }
        });

        Button optionsb = (Button)findViewById(R.id.optionsb);
        optionsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), OptionsTab.class);
                startActivity(myIntent);
            }
        });


    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(flag){
            this.flag = false;

            this.warrior.setX(event.getX());
            this.warrior.setY(event.getY());
            return true;
        }
        return super.dispatchTouchEvent(event);
    }
    public void setFlag(){
        this.flag = true;
    }
}