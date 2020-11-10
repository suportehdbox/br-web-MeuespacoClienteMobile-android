package br.com.libertyseguros.mobile.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.libray.Security;
import br.com.libertyseguros.mobile.util.NavigationApplication;
import br.com.libertyseguros.mobile.view.baseActivity.BaseNoActionBar;


public class Splash extends BaseNoActionBar {

    private Timer timer;

    private TimerTask timerTask;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

    }

    private void openScreenHome(){
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent it = new Intent(Splash.this, Home.class);
                startActivity(it);
            }
        };

        timer.schedule(timerTask, 2000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
