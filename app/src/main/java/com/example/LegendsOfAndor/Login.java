package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

enum LoginResponses {
    LOGIN_SUCCESS, NEW_LOGIN_CREATED, LOGIN_ERROR_INCORRECT_PASSWORD, LOGIN_ERROR_ALREADY_LOGGED_IN
}

public class Login extends AppCompatActivity {
    private String username;
    private String password;
    private String serverIP;

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText ipInput;

    private Button loginButton;
    private Player p;
    private MyPlayer myPlayer = MyPlayer.getInstance();

    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //display login page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        // set EditText objects
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        ipInput = findViewById(R.id.ipInput);

        //when login button is pressed get username and password
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, LoginResponses> asyncTask;
                LoginResponses loginResponse;

                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                serverIP = ipInput.getText().toString();
                if (username == null || password == null || serverIP == null || username.length() == 0 || password.length() == 0 || serverIP.length() == 0) {
                    Toast.makeText(Login.this, "Please fill out all required fields", Toast.LENGTH_LONG).show();
                } else {
                    p = new Player(username, password, GlobalStaticMethods.getRandomColor());
                    usernameInput.setText("");
                    passwordInput.setText("");
                    ipInput.setText("");

                    myPlayer.setPlayer(p);
                    myPlayer.setServerIP(serverIP);

                    try {
                        LoginSender loginSender = new LoginSender();
                        asyncTask = loginSender.execute(new Gson().toJson(p));
                        loginResponse = asyncTask.get();
                        if (loginResponse == null) {
                            Toast.makeText(Login.this, "Login error. No response from server.", Toast.LENGTH_LONG).show();
                        } else if (loginResponse == LoginResponses.LOGIN_ERROR_INCORRECT_PASSWORD) {
                            Toast.makeText(Login.this, "Login error. Incorrect password", Toast.LENGTH_LONG).show();
                        } else if (loginResponse == LoginResponses.LOGIN_ERROR_ALREADY_LOGGED_IN) {
                            Toast.makeText(Login.this, "Login error. Player is already logged in", Toast.LENGTH_LONG).show();
                        } else {
                            if (loginResponse == LoginResponses.LOGIN_SUCCESS) {
                                Toast.makeText(Login.this, "Login success. Welcome back.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Login.this, "Login success. New account created.", Toast.LENGTH_LONG).show();
                            }
                            //LOGIN INFO IS CORRECT
                            //PUT INFO INTO MYPLAYER
                            MyPlayer myPlayer = MyPlayer.getInstance();
                            myPlayer.setPlayer(p);
                            //START LOBBY PAGE
                            Intent myIntent = new Intent(v.getContext(), CreateGame.class);
                            startActivity(myIntent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static class LoginSender extends AsyncTask<String, Void, LoginResponses> {
        @Override
        protected LoginResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse <String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() +":8080/login")
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, LoginResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}