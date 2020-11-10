package br.com.libertyseguros.mobile.util;


import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;

import com.datami.smi.SdState;
import com.datami.smi.SdStateChangeListener;
import com.datami.smi.SmiResult;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.LoadFile;


public class AnalyticsApplication extends NavigationApplication implements SdStateChangeListener {
    private FirebaseAnalytics mFirebaseAnalytics;

    private final String channelId = "Notifications";

    synchronized public FirebaseAnalytics getFirebaseAnalitycs() {
        if (mFirebaseAnalytics == null) {

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            //mTracker = analytics.newTracker(R.xml.global_tracker);
            //mTracker.enableExceptionReporting(true);
        }
        return mFirebaseAnalytics;
    }

//    private void initSalesForce() {
//
//
//        String salesforceID = "";
//        String salesforceToken = "";
//
//        if (BuildConfig.prod) {
//            salesforceID = getResources().getString(R.string.sales_force_id_prod);
//            salesforceToken = getResources().getString(R.string.sales_force_token_prod);
//        } else {
//            salesforceID = getResources().getString(R.string.sales_force_id_hom);
//            salesforceToken = getResources().getString(R.string.sales_force_token_hom);
//        }
//
//        MarketingCloudSdk.init(this, MarketingCloudConfig.builder()
//                .setApplicationId(salesforceID)
//                .setAccessToken(salesforceToken)
//                .setSenderId(getString(R.string.gcm_sender_id))
//                .setNotificationCustomizationOptions(
//                        NotificationCustomizationOptions.create(R.drawable.icon_menu_notification)
//                ).build(this), new MarketingCloudSdk.InitializationListener() {
//                @Override
//                public void complete(InitializationStatus status) {
//                    if (status.isUsable()) {
//                        //Log.i(Config.TAG, "Sales Force: " + status.status().name());
//                        if (status.status() == InitializationStatus.Status.COMPLETED_WITH_DEGRADED_FUNCTIONALITY) {
//                            // While the SDK is usable, something happened during init that you should address.
//                            // This could include:
//                            if (GoogleApiAvailability.getInstance().isUserResolvableError(status.playServicesStatus())) {
//                                //Google play services encountered a recoverable error
//                                GoogleApiAvailability.getInstance().showErrorNotification(AnalyticsApplication.this, status.playServicesStatus());
//                            }
//                        }
//                    } else {
//                        //Something went wrong with init that makes the SDK unusable.
//                    }
//                }
//            });
//
//        MarketingCloudSdk.requestSdk(new MarketingCloudSdk.WhenReadyListener() {
//            @Override
//            public void ready(@NonNull MarketingCloudSdk marketingCloudSdk) {
//
//            }
//        });
//        MarketingCloudSdk.setLogLevel(MCLogListener.VERBOSE);
//        MarketingCloudSdk.setLogListener(new MCLogListener.AndroidLogListener());
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        String deviceGCM = FirebaseInstanceId.getInstance().getToken();

        if (deviceGCM != null) {
            LoadFile lf = new LoadFile();
            lf.savePref(Config.TAGGCM, deviceGCM, Config.TAG, this);
        }


//        initSalesForce();

    }

    @Override
    public void onChange(SmiResult currentSmiResult) {
        try {
            if (listener == null) {
                return;
            }
            SdState sdState = currentSmiResult.getSdState();
            //Log.v((Config.TAG, "sponsored data state : " + sdState);
            if (sdState == SdState.SD_AVAILABLE) {
                // TODO: show a banner or message to user, indicating that the data usage is sponsored and charges do not apply to user data plan
                listener.OnDataPlanChange(true);
            } else if (sdState == SdState.SD_NOT_AVAILABLE) {
                // TODO: show a banner or message to user, indicating that the data usage is NOT sponsored and charges apply to user data plan
                //Log.d(Config.TAG, " - reason: " + currentSmiResult.getSdReason());
                listener.OnDataPlanChange(false);
            } else if (sdState == SdState.WIFI) {
                // device is in wifi
                listener.OnDataPlanChange(false);
            }

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "SD_STATUS_CHANGE");

            getFirebaseAnalitycs().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            // Log the sdState event to Google Analytics
           /* getFirebaseAnalitycs().send(new HitBuilders.EventBuilder()
                    .setCategory("SD_STATUS_CHANGE")
                    .setAction("" + sdState).setNonInteraction(true)
                    .build());*/
        } catch (IllegalStateException e) {
            //nothing to do here

        }
    }


}