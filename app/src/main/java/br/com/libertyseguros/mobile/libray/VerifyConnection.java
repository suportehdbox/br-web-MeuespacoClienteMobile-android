package br.com.libertyseguros.mobile.libray;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class VerifyConnection {

    /**
     * Wifi Connection Verify
     * @param context
     * @return
     */
    public boolean typeConnection(Context context){
        boolean wifi = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if( ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI)
        {
            wifi = true;
        }

        return wifi;
    }
}
