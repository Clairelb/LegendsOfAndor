package com.example.LegendsOfAndor;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.github.chrisbanes.photoview.PhotoView;

public class Board extends AppCompatActivity {
    public ImageView warrior;
    boolean flag = false;
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
            Bitmap layout = BitmapFactory.decodeResource(getResources(),R.drawable.overlay);

            int newColor = layout.getPixel((int)(event.getX()),(int)(event.getY()));
            int newRegionNumber = Color.blue(newColor);
            //Region newRegion = RegionDatabase.getInstance().getRegionDatabase().get(newRegionNumber);

            int oldColor = layout.getPixel((int)(this.warrior.getX()),(int)(this.warrior.getY()));
            int oldRegionNumber = Color.blue(oldColor);
            //Region currentRegion = RegionDatabase.getInstance().getRegionDatabase().get(oldRegionNumber);

            //if(currentRegion.getAdjacentRegions().contains(newRegion))
            //{
                //this.warrior.setX(event.getX());
                //this.warrior.setY(event.getY());
                //return true;
            //}
            //else
                //{
                    //Toast.makeText(Board.this, "The clicked region is not a neighbor to your current region. Please choose your destination again.", Toast.LENGTH_LONG).show();
                    //return super.dispatchTouchEvent(event);
                //}
            //check if the region is a neighbor to the current region;
            //set the player's current region to new region;
            //add the player to the new region and remove the player from the previous region
        }
        return super.dispatchTouchEvent(event);
    }
    public void setFlag(){
        this.flag = true;
    }
}