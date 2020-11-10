/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.libertyseguros.mobile.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Date;
import java.util.logging.Handler;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.libray.CheckForeground;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.util.NavigationApplication;
import br.com.libertyseguros.mobile.view.TransparentActivity;


public class MyGcmListenerService extends GcmListenerService {

    private int productId;
    private static NavigationApplication.NavigationListener onNotificationListener;
    private static final String TAG = "LibertySeguros";

    private Handler handler;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {


        for (String key : data.keySet()) {
            //Log.v((TAG, key + " = \"" + data.get(key) + "\"");
        }
        String message = "";
        if (data.getBundle("notification") != null) {
            message = data.getBundle("notification").getString("body");
        } else {
            message = data.getString("message");
        }

        String textProductId = null;

        try {
            textProductId = data.getString("productId");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Log.d(TAG, "From: " + from);
        //Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

        boolean bProduct;

        if (textProductId != null) {
            productId = Integer.parseInt(textProductId);
            bProduct = true;
        } else {
            productId = -1;
            bProduct = false;
        }

        sendNotification(message, bProduct, productId);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, boolean value, int id) {

        CheckForeground checker = new CheckForeground();

        LoadFile lf = new LoadFile();

        String number = lf.loadPref(Config.TAG, this, Config.TAGNOTIFICAITONNEW);

        if (number == null) {
            number = "0";
        }

        int valueNotification = Integer.parseInt(number);

        valueNotification++;

        lf.savePref(Config.TAGNOTIFICAITONNEW, valueNotification + "", Config.TAG, this);

        if (checker.isAppInForeground(getBaseContext().getPackageName(), getBaseContext())) {
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
//                    .setSmallIcon(R.drawable.notificacao)
//                    .setContentTitle(this.getString(R.string.app_name))
//                    .setContentText(message)
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            long time = new Date().getTime();
//            String tmpStr = String.valueOf(time);
//            String last4Str = tmpStr.substring(tmpStr.length() - 5);
//            int notificationId = Integer.valueOf(last4Str);
//        notificationManager.notify(notificationId, notificationBuilder.build());


//
//        }else {
            Intent intent = new Intent(this, TransparentActivity.class);
            intent.putExtra("notification", message);


            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                    .setSmallIcon(R.drawable.notificacao)
                    .setContentTitle(this.getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setColor(getColor(R.color.background_navigation_menu))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            long time = new Date().getTime();
            String tmpStr = String.valueOf(time);
            String last4Str = tmpStr.substring(tmpStr.length() - 5);
            int notificationId = Integer.valueOf(last4Str);
            notificationManager.notify(notificationId, notificationBuilder.build());

        }
    }

    /**
     * Set onNotificationListener
     *
     * @param listener - OnConectionChangeListener object
     */
    public static void setOnNotificationListener(
            NavigationApplication.NavigationListener listener) {
        onNotificationListener = listener;
    }

    public static void removeOnNotificationListener() {
        onNotificationListener = null;
    }
}
