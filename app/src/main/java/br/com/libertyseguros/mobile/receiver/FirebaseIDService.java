package br.com.libertyseguros.mobile.receiver;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.LoadFile;

public class FirebaseIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //Log.d(TAG, "Refreshed token: " + refreshedToken);
        LoadFile lf = new LoadFile();
        lf.savePref(Config.TAGGCM, token, Config.TAG, this);
    }
}
