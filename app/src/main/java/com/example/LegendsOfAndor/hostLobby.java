package com.example.LegendsOfAndor;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class hostLobby extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_lobby);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //SET BUTTON TEXT FONT
        Typeface gothicFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "LeagueGothic-Regular.otf");
        TextView lobby_name = findViewById(R.id.lobby_name);
        Button start_game_btn = findViewById(R.id.host_start_game);
        Button create_lobby_btn = findViewById(R.id.create_lobby);
        start_game_btn.setTypeface(gothicFont);
        create_lobby_btn.setTypeface(gothicFont);
        lobby_name.setTypeface(gothicFont);




//        spinner.setPrompt("NUMBER OF PLAYERS");

//        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.)
//        final ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(this, R.array.number_players,
//                android.R.layout.simple_spinner_item);
//        {
//            public boolean isEnabled ( int position){
//            if (position == 0) {
//                // Disable the first item from Spinner
//                // First item will be use for hint
//                return false;
//            } else {
//                return true;
//            }
//        }
//
//
//            public View getDropDownView ( int position, View convertView,
//                ViewGroup parent){
//            View view = super.getDropDownView(position, convertView, parent);
//            TextView tv = (TextView) view;
//            if (position == 0) {
//                // Set the hint text color gray
//                tv.setTextColor(Color.GRAY);
//            } else {
//                tv.setTextColor(Color.BLACK);
//            }
//            return view;
//        }
//
//
//            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
//            String selectedItemText = (String) parent.getItemAtPosition(position);
//            // If user change the default selection
//            // First item is disable and it is used for hint
//            if (position > 0) {
//                // Notify the selected item text
//                Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        }


    }
}
