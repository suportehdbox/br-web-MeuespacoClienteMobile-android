package br.com.libertyseguros.mobile.receiver;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.net.URLEncoder;

import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.controller.MainController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.view.Login;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    private Connection conn;

    private LoadFile loadFile;

    private InfoUser infoUser;

    private Context context;
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Refreshed token: " + refreshedToken);

        String deviceGCM = FirebaseInstanceId.getInstance().getToken();

        if(deviceGCM != null){
            LoadFile lf = new LoadFile();
            lf.savePref(Config.TAGGCM, deviceGCM, Config.TAG, this);
        }

    }
}
