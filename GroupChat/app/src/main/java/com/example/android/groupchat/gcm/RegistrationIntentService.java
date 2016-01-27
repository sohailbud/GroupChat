package com.example.android.groupchat.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.groupchat.util.Config;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sohail on 1/20/16.
 * <p/>
 * Request an instance ID from Google that will be a way to uniquely identify the device and app.
 * Assuming this request is successful,
 * a token that can be used to send notifications to the app should be generated too.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String GCM_TOKEN = "gcmToken";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Started");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // Make a call to Instance API
            InstanceID instanceID = InstanceID.getInstance(this);

            String token = instanceID.getToken(
                    Config.GOOGLE_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.d(TAG, "Token Received: " + token);

            // save token
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(GCM_TOKEN, token);

            // pass along this data
            sendRegistrationToServer(token);

        } catch (IOException e) {
            Log.d(TAG, "Failed to complete token refresh", e);

            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            PreferenceManager.getDefaultSharedPreferences(this).
                    edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    /**
     * Persist registration to third-party servers.
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     */
    private void sendRegistrationToServer(String token) {

        try {

            URL url = new URL(Config.URL_GCM_SERVLET);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //add request header
            connection.setRequestMethod("POST");

            //send post request
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(token);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Sending 'POST' request to URL : " + url);
            Log.d(TAG, "Post parameters : " + token);
            Log.d(TAG, "Response Code : " + responseCode);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null)
                response.append(inputLine);

            bufferedReader.close();

            //print result
            System.out.println(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
//        PreferenceManager.getDefaultSharedPreferences(this).
//                edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
    }


}
