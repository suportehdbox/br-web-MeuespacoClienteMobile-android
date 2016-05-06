package br.com.libertyseguros.mobile.gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.activity.LibertyMobile;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

@SuppressLint("SimpleDateFormat")
public class GcmIntentService extends IntentService {

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";

    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

            	String msg = extras.getString("alert");

            	//-- Salva a notificacao
            	saveNotification(msg);

            	//-- Caso aplicacao executando exibe alert
            	showNotification(msg);

            	//-- Exibe notificação na barra:
            	sendNotification(msg);

                Log.i(TAG, "Received: " + msg);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

	// verifica se recebeu novo push - param que veio do GcmIntentService
    private void saveNotification(String push){

		if (null != push && !push.equals("")) {

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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

////    	runOnUiThread(new Runnable() { public void run() {
//				AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
//				dialog.setIcon(android.R.drawable.ic_dialog_alert);
//				dialog.setTitle(R.string.Atention);
//				dialog.setMessage(msg);
////				dialog.setNeutralButton("OK");
//				dialog.show();
////			}});
    	}
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with a GCM message.
    // Ao clicar na notificção deve abrir a activity de consulta de notificações
    private void sendNotification(String msg) {

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Bundle parms = new Bundle();
        parms.putBoolean(Constants.LM_EXTRA_PUSH, true);
		Intent it = new Intent(this, LibertyMobile.class);
		it.putExtras(parms);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
        															.setSmallIcon(R.mipmap.ic_launcher)
															        .setContentTitle("Liberty Seguros")
															        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
															        .setContentText(msg)
															        .setVibrate(new long[] { 150, 300, 150, 600 })
															        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setOngoing(true);
        mNotificationManager.notify(Util.randInt(), mBuilder.build());

        /* // API 22
        Bundle parms = new Bundle();
        parms.putBoolean(Constants.LM_EXTRA_PUSH, true);

        Intent notificationIntent = new Intent(getApplicationContext(), LibertyMobile.class);
        notificationIntent.putExtras(parms);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long when = System.currentTimeMillis();
        String title = getApplicationContext().getString(R.string.app_name);

        Notification notification = new Notification(R.mipmap.ic_launcher, msg, when);
        notification.setLatestEventInfo(getApplicationContext(), title, msg, intent);

        //setstyle .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Util.randInt(), notification);
*/

//        Notification.Builder builder = new Notification.Builder(this);
//
//        builder.setAutoCancel(false);
//        builder.setTicker("this is ticker text");
//        builder.setContentTitle(title);
//        builder.setContentText(msg);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentIntent(intent);
//        builder.setOngoing(true);
////		builder.setSubText("This is subtext...");   //API level 16
////		builder.setNumber(100);
//        builder.build();
//
//        Notification notification = builder.getNotification();
    }
}