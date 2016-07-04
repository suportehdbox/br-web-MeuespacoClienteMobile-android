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

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(br.com.libertyseguros.mobile.R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

//            sendRegistrationToServer(token);
//            sendRegistrationIdToBackend(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, true).apply();

            sharedPreferences.edit().putString(Constants.TOKEN_GCM, token).apply();

            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        try {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            for (String topic : TOPICS) {
                pubSub.subscribe(token, "/topics/" + topic, null);
            }
        } catch (Exception e) {
            Util.showException(this, e);
        }
    }
    // [END subscribe_topics]


//    /**
//     * Persist registration to third-party servers.
//     *
//     * Modify this method to associate the user's GCM registration token with any server-side account
//     * maintained by your application.
//     *
//     * @param tokenNotificacao The new token.
//     */
//
//    /**
//     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
//     * or CCS to send messages to your app. Not needed for this demo since the
//     * device sends upstream messages to a server that echoes back the message
//     * using the 'from' address in the message.
//     */
//    private void sendRegistrationIdToBackend(String tokenNotificacao) {
//
//        try {
//
//            WebserviceInterface enviarTokenWsInterface = new WebserviceInterface(this) {
//                @Override
//                public void callBackWebService(String response) {
//                    Log.i("callEnviarToken", String.valueOf(CallWebServices.retEnviarToken(response)));
//                }
//            };
//
//            // Chama o ws
//            CallWebServices.callEnviarToken(this, getDadosLoginSegurado().getCpf(), tokenNotificacao, enviarTokenWsInterface);
//
//        } catch (Exception e) {
//            Util.showException(this, e);
//        }
//    }

}
