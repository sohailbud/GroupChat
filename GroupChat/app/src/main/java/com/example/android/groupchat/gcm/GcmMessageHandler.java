package com.example.android.groupchat.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.groupchat.R;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Sohail on 1/20/16.
 *
 * This class processes the message received from GCM
 */
public class GcmMessageHandler extends GcmListenerService {

    public static final String TAG = "GcmMessageHandler";
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    static{

        Log.d(TAG, "GCM - onMessageReceived");
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "GCM - onMessageReceived");
        createNotification(from, data.getString("message"));
    }

    /**
     * creates a notification based on title and body received
     */
    private void createNotification(String title, String body) {
        Context context = getBaseContext();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(MESSAGE_NOTIFICATION_ID, builder.build());
    }


}
