package com.example.LegendsOfAndor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatScreen extends AppCompatActivity {
    //channel ID and room to connect to.
    private String channelID = "Vs10DZ4cK13y3m6K";
    private String roomName = "observable-room";

    private EditText editText;
    private ImageButton sendButton;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //display chat screen page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);

        username = MyPlayer.getInstance().getPlayer().getUsername();
        password = MyPlayer.getInstance().getPlayer().getPassword();

        //the text you are editing before sending
        editText = findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        sendButton = findViewById(R.id.sendButton);

        try {
           AsyncTask<String, Void, ArrayList<Message>> asyncTask;
           OldMessagesRetriever oldMessagesRetriever = new OldMessagesRetriever();
           asyncTask = oldMessagesRetriever.execute();

           if (asyncTask.get() != null) {
               for (Message m : asyncTask.get()) {
                   if (m.getPlayer().getUsername().equals(username)) {
                       m.setBelongsToCurrentUser(true);
                   } else {
                       m.setBelongsToCurrentUser(false);
                   }

                   messageAdapter.add(m);
                   messagesView.setSelection(messagesView.getCount() - 1);
               }
           }
        } catch (Exception e) {
           e.printStackTrace();
        }

        //create a new user with the username and generate a random color
        final Player player = MyPlayer.getInstance().getPlayer();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();

                if (message.length() > 0) {
                    Message newMessage = new Message(player, message, true);

                    editText.getText().clear(); // clear editText

                    messageAdapter.add(newMessage);
                    messagesView.setSelection(messagesView.getCount() - 1);

                    try {
                        MessageSender messageSender = new MessageSender();
                        messageSender.execute(new Gson().toJson(newMessage), username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread t = new Thread(new Runnable() { // this runs forever checking for new messages
            @Override
            public void run() {
                while(true) {
                    try {
                        HttpResponse<String> response = Unirest.get("http://192.168.0.151:8080/game1/"+username+"/getMsg") // here game1 is a test, the gameName goes here
                                .asString();

                        if (response.getCode() == 200) {
                            MessageDatabase messageDatabase = new Gson().fromJson(response.getBody(), MessageDatabase.class);
                            final Message incomingMsg = messageDatabase.getMessages().get(messageDatabase.getMessages().size() - 1);

                            if (!incomingMsg.getPlayer().getUsername().equals(username)) {
                                incomingMsg.setBelongsToCurrentUser(false);
                                runOnUiThread(new Runnable() { // cannot run this part on seperate thread, so this forces the following to run on UiThread
                                    @Override
                                    public void run() {
                                        messageAdapter.add(incomingMsg);
                                        messagesView.setSelection(messagesView.getCount() - 1);
                                    }
                                });
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    private static class OldMessagesRetriever extends AsyncTask<String, Void, ArrayList<Message>> {
        @Override
        protected ArrayList<Message> doInBackground(String... strings) {
            HttpResponse<String> response;

            try {
                response = Unirest.get("http://192.168.0.151:8080/game1/getAllMsgs") // here game1 is a test, the gameName goes here
                        .asString();

                String resultAsJsonString = response.getBody();
                return new Gson().fromJson(resultAsJsonString, new TypeToken<ArrayList<Message>>() {}.getType());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private static class MessageSender extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpResponse<String> response;

            try {
                response = Unirest.post("http://192.168.0.151:8080/game1/" + strings[1] + "/sendMsg") // here game1 is a test, the gameName goes here
                        .header("Content-Type", "application/json")
                        .body(strings[0])
                        .asString();

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}

