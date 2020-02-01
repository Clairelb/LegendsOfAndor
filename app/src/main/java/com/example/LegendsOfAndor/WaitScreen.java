package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class WaitScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Wait animation
        ProgressDialog nDialog;
        nDialog = new ProgressDialog(WaitScreen.this);
        nDialog.setTitle("Please wait...");
        nDialog.setCancelable(true);
        nDialog.setIndeterminate(true); //Progress bar doesn't do anything, infinite loop

    }

}
