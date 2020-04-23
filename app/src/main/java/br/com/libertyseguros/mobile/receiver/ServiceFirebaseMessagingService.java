package br.com.libertyseguros.mobile.receiver;


import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.salesforce.marketingcloud.notifications.NotificationManager;

import br.com.libertyseguros.mobile.libray.CheckForeground;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.util.NavigationApplication;
import br.com.libertyseguros.mobile.view.Notification;


public class ServiceFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static NavigationApplication.NavigationListener onNotificationListener;

    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {


            if(remoteMessage != null && remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null) {
                Log.d(TAG, "From: " + remoteMessage.getFrom());
                Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

                CheckForeground checker = new CheckForeground();
                if (checker.isAppInForeground(getBaseContext().getPackageName(), getBaseContext())) {
                    if (onNotificationListener != null) {
                        onNotificationListener.OnNotificationReceived(remoteMessage.getNotification().getBody());
                    }
                }
            }
        }catch (Exception ex){

        }
    }


    public static void setOnNotificationListener(NavigationApplication.NavigationListener onNotificationListener) {
        ServiceFirebaseMessagingService.onNotificationListener = onNotificationListener;
    }

    public static void removeOnNotificationListener() {
        onNotificationListener = null;
    }
}