/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.libertyseguros.mobile.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.activity.NotificacaoConsultaActivity;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

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
        String msg = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + msg);

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
        //-- Salva a notificacao
        saveNotification(msg);

        //-- Caso aplicacao executando exibe alert
        showNotification(msg);

        //-- Exibe notificação na barra:
        sendNotification(msg);

        Log.i(TAG, "Received: " + msg);

        // [END_EXCLUDE]
    }
    // [END receive_message]

    // verifica se recebeu novo push - param que veio do GcmIntentService
    private void saveNotification(String push){

        if (null != push && !push.equals("")) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = sdf.format(new Date());

            Map<String, Object> notificacao = new HashMap<String, Object>();
            notificacao.put("date", date);
            notificacao.put("alert", push);

            // Inicializar o array com os conteúdos do json salvo
            List<Map<String, Object>> arrayFields = Util.getNotificationList(getApplicationContext());

            if(null == arrayFields){
                arrayFields = new ArrayList<Map<String,Object>>();
            }

            arrayFields.add(notificacao);

            if(arrayFields.size() > 1) {
                Collections.sort(arrayFields, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date d1 = null;
                        Date d2 = null;
                        try {
                            d1 = sdf.parse((String)lhs.get("date"));
                            d2 = sdf.parse((String)rhs.get("date"));
                        } catch (ParseException e) {
                            Util.showException(null, e);
                        }
                        return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
                    }
                });
            }

            // atualiza json
            Util.saveNotificationList(getApplicationContext(), arrayFields);
        }
    }

    private void showNotification(String msg) {

        if(CustomApplication.isActivityVisible()){
            //Launch a activity
            Bundle parms = new Bundle();
            parms.putString(Constants.LM_EXTRA_PUSH, msg);
            Intent i = new Intent();
            i.putExtras(parms);
            i.setClass(getApplicationContext(), NotificacaoAlertActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        }
    }


    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {

        Bundle parms = new Bundle();
        parms.putBoolean(Constants.LM_EXTRA_PUSH, true);
//        Intent intent = new Intent(this, NotificacaoConsultaActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(parms);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Liberty Seguros")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 150, 300, 150, 600 })
//                .setOngoing(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Util.randInt(), notificationBuilder.build());

     }
}
