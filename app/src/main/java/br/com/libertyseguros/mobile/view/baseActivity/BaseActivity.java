package br.com.libertyseguros.mobile.view.baseActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.receiver.ServiceFirebaseMessagingService;
import br.com.libertyseguros.mobile.util.NavigationApplication;
import br.com.libertyseguros.mobile.view.custom.NotificationSnackBar;


public class BaseActivity extends Activity implements NavigationApplication.NavigationListener {


    @Override
    public void onResume() {
        super.onResume();
        NavigationApplication nav = (NavigationApplication) getApplication();
        nav.setNavigationListener(this);
        ServiceFirebaseMessagingService.setOnNotificationListener(nav.getListener());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        // NavigationApplication nav = (NavigationApplication) getApplication();
        //nav.setNavigationListener(this);
    }

    @Override
    public void OnDataPlanChange(boolean freeNavigation) {
        String msg = getString(R.string.free_navigation);
        if (!freeNavigation) {
            //msg = getString(R.string.paid_navigation);
            return;
        }
        CoordinatorLayout Clayout = (CoordinatorLayout) findViewById(R.id.snackbarlocation);

        Snackbar snackbar = Snackbar.make(Clayout, msg, Snackbar.LENGTH_SHORT);
        snackbar.setDuration(4000);
        snackbar.show();
    }

    @Override
    public void OnNotificationReceived(String message) {
        CoordinatorLayout Clayout = (CoordinatorLayout) findViewById(R.id.snackbarlocation);
        NotificationSnackBar.showNotification(getApplicationContext(), Clayout, message);
    }


}
