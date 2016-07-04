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

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.activity.MenuPrincipalActivity;
import br.com.libertyseguros.mobile.activity.NotificacaoConsultaActivity;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    private ProgressBar mRegistrationProgressBar;
//    private TextView mInformationTextView;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // << EPO
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            if(null != getIntent().getExtras() && getIntent().getExtras().getBoolean(Constants.LM_EXTRA_PUSH)){

                // -- Caso venha de acesso pela notificação:

                // -- e caso já não esteja na tela de notificação
                ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                String atual =  taskInfo.get(0).topActivity.getClassName();
                if (!atual.contains("NotificacaoConsultaActivity")) {
//                    callActivity(NotificacaoConsultaActivity.class);
                    gotoMenuPrincipal(true);
                }
            }
            else {

                // -- Caso venha de inicialização normal:

                //Codigo para utilizar este activity como tela de splash
                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {

                        gotoMenuPrincipal(false);
                    }
                }, 6000);
            }

            // EPO >>

//        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
//                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences.getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
//                    mInformationTextView.setText(getString(R.string.gcm_send_message));
                    } else {
//                    mInformationTextView.setText(getString(R.string.token_error_message));
                    }
                }
            };
//        mInformationTextView = (TextView) findViewById(R.id.informationTextView);

            // Registering BroadcastReceiver
            registerReceiver();

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }

        } catch (Exception e) {
            Util.showException(this, e);
        }
    }

    /**
     * Chama a activity Principal
     * @param b, caso informa parametro nescessário para comportamento da activity.
     */
    private void gotoMenuPrincipal(boolean b) {
        Intent intent = new Intent();
        Bundle parms = new Bundle();
        parms.putBoolean(Constants.LM_EXTRA_PUSH, b);
        intent.putExtras(parms);
        intent.setClass(this, MenuPrincipalActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();

        CustomApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;

        CustomApplication.activityPaused();
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Constants.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
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
                Exception error = new Exception("Dispositivo não suporta PlayService");
                Util.showException(null, error);
                finish();
            }
            return false;
        }
        return true;
    }

}
