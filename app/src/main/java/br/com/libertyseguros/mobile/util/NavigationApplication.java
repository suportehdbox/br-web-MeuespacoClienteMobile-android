package br.com.libertyseguros.mobile.util;


import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.datami.smi.SmiSdk;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.libray.InfoUser;

public class NavigationApplication extends Application {

    protected NavigationListener listener;

    public void setNavigationListener(NavigationListener myListener) {
        listener = myListener;
    }

    public NavigationListener getListener(){
        return  listener;
    }

    public void onCreate() {
        super.onCreate();
        // Call the Datami API at the beginning of onCreate, before other initializations.
        // IMPORTANT: If Datami API is not the first API called in the application then any network
        // connection made before Datami SDK initialization will be non-sponsored and will be
        // charged to the user.
        String mySdkKey = "af5c8174aad82237bb8fb90d562de55777a14922"; //Use the SDK API access key given by Datami.
        InfoUser info = new InfoUser();
        String myUserId = "not_loggedin@user.com";
        if (info.getUserInfo(getApplicationContext()).getEmail() != null && !info.getUserInfo(getApplicationContext()).getEmail().equalsIgnoreCase("")) {
            myUserId = info.getUserInfo(getApplicationContext()).getEmail();
        }

        int sdIconId = R.drawable.ic_launcher;
        Boolean sdkUserMessaging = false; //if “false” the application needs to inform the user about the sponsorship in OnChange callback

//        if(BuildConfig.brandMarketing == 1){
            SmiSdk.initSponsoredData(mySdkKey, this, myUserId, sdIconId, sdkUserMessaging);
//        }
        // Application specific initializations
    }


//    @Override
//    public void onChange(SmiResult currentSmiResult) {
//        if(listener == null){
//            return;
//        }
//        SdState sdState = currentSmiResult.getSdState();
//        //Log.v((Config.TAG, "sponsored data state : " + sdState);
//        if(sdState == SdState.SD_AVAILABLE) {
//            // TODO: show a banner or message to user, indicating that the data usage is sponsored and charges do not apply to user data plan
//            listener.OnDataPlanChange(true);
//        } else if(sdState == SdState.SD_NOT_AVAILABLE) {
//            // TODO: show a banner or message to user, indicating that the data usage is NOT sponsored and charges apply to user data plan
//            //Log.d(Config.TAG, " - reason: " + currentSmiResult.getSdReason());
//            listener.OnDataPlanChange(false);
//        } else if(sdState == SdState.WIFI) {
//            // device is in wifi
//            listener.OnDataPlanChange(false);
//        }
//
//        // Log the sdState event to Google Analytics
//        getFirebaseAnalitycs().send(new HitBuilders().EventBuilder()
//                .setCategory("SD_STATUS_CHANGE")
//                .setAction(""+sdState).setNonInteraction(true)
//                .build());
//    }


    public interface NavigationListener {
        void OnDataPlanChange(boolean freeNavigation);

        void OnNotificationReceived(String message);


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            multiDexException.printStackTrace();
        }
    }
}


