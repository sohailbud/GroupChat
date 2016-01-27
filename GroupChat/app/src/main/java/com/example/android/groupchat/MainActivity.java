package com.example.android.groupchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.groupchat.dao.Message;
import com.example.android.groupchat.gcm.GcmMessageHandler;
import com.example.android.groupchat.gcm.RegistrationIntentService;
import com.example.android.groupchat.util.Config;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    WebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chats_container);
        final ChatsRecyclerViewAdapter chatsRecyclerViewAdapter = new ChatsRecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatsRecyclerViewAdapter);

        final EditText inputMessage = (EditText) findViewById(R.id.inputM_msg);
        final Button sendMessage = (Button) findViewById(R.id.btn_send);

        URI uri = null;
        try {
            uri = new URI(Config.URL_WEBSOCKET);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri != null)
            client = new WebSocketClient(uri, new Draft_17()) {
                @Override
                public void onOpen(ServerHandshake handshakeData) {
                    Log.i("Websocket", "Opened");

                }

                @Override
                public void onMessage(String msg) {
                    final String message = msg;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (message.contains(":"))
                                chatsRecyclerViewAdapter.insertData(convertToMessage(message));
                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.i("Websocket", "Closed " + reason);

                }

                @Override
                public void onError(Exception ex) {

                }
            };

        client.connect();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMsg = inputMessage.getText().toString();
                sendMessageToServer(inputMsg);
                inputMessage.setText("");
//                chatsRecyclerViewAdapter.insertData(new Message(Utils.currentUser, inputMsg));

            }
        });

        if (checkPlayServices())
            startService(new Intent(this, RegistrationIntentService.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    public void sendMessageToServer(String message) {
        client.send(message);
    }

    public Message convertToMessage(String msg) {
        String[] msgs = msg.split(":");
        return new Message(msgs[0], msgs[1]);
    }

    /**
     * Code received from google-services library
     * https://github.com/googlesamples/google-services/blob/master/android/gcm/app/src/main/java/gcm/play/android/samples/com/gcmquickstart/MainActivity.java
     *
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
