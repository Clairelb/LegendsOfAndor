package com.example.LegendsOfAndor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

enum ActivateHelmResponses {
    ERROR_DOES_NOT_OWN_HELM, ERROR_ARCHER, ERROR_WIZARD, ERROR_BOW_USER, ERROR_NO_DICE_ROLLS, HELM_ACTIVATED
}

enum ActivateShieldFightResponses {
    ERROR_DOES_NOT_OWN_SHIELD, ERROR_SHIELD_ALREADY_ACTIVATED, ERROR_INAPPROPRIATE_SHIELD_ACTIVATION, SHIELD_ACTIVATED
}

enum ActivateWitchesBrewFightResponses {
    ERROR_DOES_NOT_OWN_WITCHES_BREW, ERROR_BV_NOT_SET, ERROR_CANNOT_USE_AFTER_CREATURE_ROLL_DIE, WITCHES_BREW_ACTIVATED
}

enum ActivateMedicinalHerbFightResponses {
    ERROR_DOES_NOT_OWN_MEDICINAL_HERB, ERROR_BV_NOT_SET, ERROR_CANNOT_USE_AFTER_CREATURE_ROLL_DIE, MEDICINAL_HERB_ACTIVATED
}

public class ChooseItemFight extends AppCompatActivity {
    private Button backBTN;
    private Button useHelmBTN;
    private Button useShieldBTN;
    private Button useWitchesBrewBTN;
    private Button useMedicinalHerbBTN;

    private boolean usedHelm = false;
    private boolean usedWitchesBrew = false;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item_fight);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        backBTN = findViewById(R.id.choose_item_fight_back);
        useHelmBTN = findViewById(R.id.use_helm);
        useShieldBTN = findViewById(R.id.use_shield);
        useWitchesBrewBTN = findViewById(R.id.use_witches_brew);
        useMedicinalHerbBTN = findViewById(R.id.use_medicinal_herb);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ChooseItemFight.this, MonsterFight.class);
                startActivity(myIntent);
            }
        });

        useHelmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usedWitchesBrew) {
                    try {
                        AsyncTask<String, Void, ActivateHelmResponses> asyncTask;
                        ActivateHelmSender activateHelmSender = new ActivateHelmSender();
                        asyncTask = activateHelmSender.execute("");

                        if (asyncTask.get() == ActivateHelmResponses.ERROR_DOES_NOT_OWN_HELM) {
                            Toast.makeText(ChooseItemFight.this, "Error. You do not own a helm.", Toast.LENGTH_LONG).show();
                        } else if (asyncTask.get() == ActivateHelmResponses.ERROR_ARCHER) {
                            Toast.makeText(ChooseItemFight.this, "Error. You are an archer.", Toast.LENGTH_LONG).show();
                        } else if (asyncTask.get() == ActivateHelmResponses.ERROR_WIZARD) {
                            Toast.makeText(ChooseItemFight.this, "Error. You are a wizard.", Toast.LENGTH_LONG).show();
                        } else if (asyncTask.get() == ActivateHelmResponses.ERROR_BOW_USER) {
                            Toast.makeText(ChooseItemFight.this, "Error. You are using a bow", Toast.LENGTH_LONG).show();
                        } else if (asyncTask.get() == ActivateHelmResponses.ERROR_NO_DICE_ROLLS) {
                            Toast.makeText(ChooseItemFight.this, "Error. You must roll your dice before activating helm.", Toast.LENGTH_LONG).show();
                        } else { // HELM_ACTIVATED
                            usedHelm = true;
                            Toast.makeText(ChooseItemFight.this, "Helm activated.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ChooseItemFight.this, "Error. Cannot use helm after using witch's brew.", Toast.LENGTH_LONG).show();
                }
            }
        });

        useShieldBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AsyncTask<String, Void, ActivateShieldFightResponses> asyncTask;
                    ActivateShieldSender activateShieldSender = new ActivateShieldSender();
                    asyncTask = activateShieldSender.execute("");

                    if (asyncTask.get() == ActivateShieldFightResponses.ERROR_DOES_NOT_OWN_SHIELD) {
                        Toast.makeText(ChooseItemFight.this, "Error. You do not own a shield.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == ActivateShieldFightResponses.ERROR_SHIELD_ALREADY_ACTIVATED) {
                        Toast.makeText(ChooseItemFight.this, "Error. You already activated your shield", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == ActivateShieldFightResponses.ERROR_INAPPROPRIATE_SHIELD_ACTIVATION) {
                        Toast.makeText(ChooseItemFight.this, "Error. This is an inappropriate time to activate your shield.", Toast.LENGTH_LONG).show();
                    } else { // SHIELD_ACTIVATED
                        Toast.makeText(ChooseItemFight.this, "Shield activated.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        useWitchesBrewBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usedHelm) {
                    Toast.makeText(ChooseItemFight.this, "Error. Cannot use witch's brew after using helm.", Toast.LENGTH_LONG).show();
                } else if (usedWitchesBrew) {
                    Toast.makeText(ChooseItemFight.this, "Error. Can only use the witch's brew once per battle round.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        AsyncTask<String, Void, ActivateWitchesBrewFightResponses> asyncTask;
                        ActivateWitchesBrewSender activateWitchesBrewSender = new ActivateWitchesBrewSender();
                        asyncTask = activateWitchesBrewSender.execute("");

                        if (asyncTask.get() == ActivateWitchesBrewFightResponses.ERROR_DOES_NOT_OWN_WITCHES_BREW) {
                            Toast.makeText(ChooseItemFight.this, "Error. You do not own a witch's brew.", Toast.LENGTH_LONG).show();
                        } else if (asyncTask.get() == ActivateWitchesBrewFightResponses.ERROR_BV_NOT_SET) {
                            Toast.makeText(ChooseItemFight.this, "Error. You must calculate your battle value before using witch's brew.", Toast.LENGTH_LONG).show();
                        } else if (asyncTask.get() == ActivateWitchesBrewFightResponses.ERROR_CANNOT_USE_AFTER_CREATURE_ROLL_DIE) {
                            Toast.makeText(ChooseItemFight.this, "Error. You cannot use witch's brew after the creature rolls their dice.", Toast.LENGTH_LONG).show();
                        } else { // WITCHES_BREW_ACTIVATED
                            usedWitchesBrew = true;
                            Toast.makeText(ChooseItemFight.this, "Witch's brew activated.", Toast.LENGTH_LONG).show();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        useMedicinalHerbBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AsyncTask<String, Void, ActivateMedicinalHerbFightResponses> asyncTask;
                    ActivateMedicinalHerbSender activateMedicinalHerbSender = new ActivateMedicinalHerbSender();
                    asyncTask = activateMedicinalHerbSender.execute("");

                    if (asyncTask.get() == ActivateMedicinalHerbFightResponses.ERROR_DOES_NOT_OWN_MEDICINAL_HERB) {
                        Toast.makeText(ChooseItemFight.this, "Error. You do not own a medicinal herb.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == ActivateMedicinalHerbFightResponses.ERROR_BV_NOT_SET) {
                        Toast.makeText(ChooseItemFight.this, "Error. You must calculate your battle value before using medicinal herb.", Toast.LENGTH_LONG).show();
                    } else if (asyncTask.get() == ActivateMedicinalHerbFightResponses.ERROR_CANNOT_USE_AFTER_CREATURE_ROLL_DIE) {
                        Toast.makeText(ChooseItemFight.this, "Error. You cannot use medicinal herb after the creature rolls their dice.", Toast.LENGTH_LONG).show();
                    } else { // MEDICINAL_HERB_ACTIVATED
                        Toast.makeText(ChooseItemFight.this, "Medicinal herb activated.", Toast.LENGTH_LONG).show();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {}

    private static class ActivateHelmSender extends AsyncTask<String, Void, ActivateHelmResponses> {
        @Override
        protected ActivateHelmResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/activateHelm")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, ActivateHelmResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class ActivateShieldSender extends AsyncTask<String, Void, ActivateShieldFightResponses> {
        @Override
        protected ActivateShieldFightResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/activateShieldFight")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, ActivateShieldFightResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class ActivateWitchesBrewSender extends AsyncTask<String, Void, ActivateWitchesBrewFightResponses> {
        @Override
        protected ActivateWitchesBrewFightResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/activateWitchesBrewFight")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, ActivateWitchesBrewFightResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class ActivateMedicinalHerbSender extends AsyncTask<String, Void, ActivateMedicinalHerbFightResponses> {
        @Override
        protected ActivateMedicinalHerbFightResponses doInBackground(String... strings) {
            MyPlayer myPlayer = MyPlayer.getInstance();
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://" + myPlayer.getServerIP() + ":8080/" + myPlayer.getGame().getGameName() + "/" + myPlayer.getPlayer().getUsername() + "/activateMedicinalHerbFight")
                        .asString();
                String resultAsJsonString = response.getBody();

                return new Gson().fromJson(resultAsJsonString, ActivateMedicinalHerbFightResponses.class);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
