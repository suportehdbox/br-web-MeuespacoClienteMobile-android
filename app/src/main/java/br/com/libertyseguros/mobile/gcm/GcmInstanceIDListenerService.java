//package br.com.libertyseguros.mobile.gcm;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.util.Log;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.iid.InstanceID;
//
//import br.com.libertyseguros.mobile.common.LibertyException;
//import br.com.libertyseguros.mobile.common.LibertyExceptionEnum;
//
///**
// * Created by evandro on 9/30/15.
// */
//public class GcmInstanceIDListenerService extends IntentService {
//
//    /**
//     * Sender ID here. This is the project number you got from the API Console.
//     */
//    private final static String SENDER_ID 	= "578571555423"; //"libertysegurosbrasil2014"
//
//
//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to name the worker thread, important only for debugging.
//     */
//    public GcmInstanceIDListenerService(String name) {
//        super(name);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        try {
//
//            // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
//            if (checkPlayServices()) {
//
//                // [START register_for_gcm]
//                // Initially this call goes out to the network to retrieve the token, subsequent calls
//                // are local.
//                // [START get_token]
//                InstanceID instanceID = InstanceID.getInstance(this);
////                String tokenNotificacao = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//                String tokenNotificacao = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//                // [END get_token]
//
//                // TODO: Implement this method to send any registration to your app's servers.
//                sendRegistrationToServer(tokenNotificacao);
//
//                Log.i("GCM", "Device registered, registration ID="+ tokenNotificacao);
//            } else {
//                Log.i("GCM", "No valid Google Play Services APK found.");
//            }
//
//        } catch (Exception e) {
//            LibertyException lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_REDE_DADOS);
//            Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());
//        }
//
//    }
//
//    /**
//     * Check the device to make sure it has the Google Play Services APK. If
//     * it doesn't, display a dialog that allows users to download the APK from
//     * the Google Play Store or enable it in the device's system settings.
//     */
//    private boolean checkPlayServices() {
//
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getApplicationContext());
//
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (!GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
////	            GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
////	        } else {
//                Log.i("CheckPlayServices", "This device is not supported.");
//            }
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Persist registration to third-party servers.
//     *
//     * Modify this method to associate the user's GCM registration token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private void sendRegistrationToServer(String token) {
//        // Add custom implementation, as needed.
//    }
//
//}