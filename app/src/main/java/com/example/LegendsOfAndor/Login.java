package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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

    private EditText usernameInput;
    private EditText passwordInput;

    private Button loginButton;
    private Player p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //display login page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // set EditText objects
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        //when login button is pressed get username and password
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String, Void, LoginResponses> asyncTask;
                LoginResponses loginResponse;

                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();

                p = new Player(username, password, GlobalStaticMethods.getRandomColor(), false);
                usernameInput.setText("");
                passwordInput.setText("");

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
                        //put username in a bundle and sent to chat screen class
                        Intent myIntent = new Intent(v.getContext(), ChatScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username",username);
                        bundle.putString("password", password);
                        myIntent.putExtras(bundle);
                        startActivity(myIntent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        });
    }

    private static class LoginSender extends AsyncTask<String, Void, LoginResponses> {
        @Override
        protected LoginResponses doInBackground(String... strings) {
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://192.168.0.151:8080/login")
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