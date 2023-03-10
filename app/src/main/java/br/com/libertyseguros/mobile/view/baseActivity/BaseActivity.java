package br.com.libertyseguros.mobile.view.baseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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

    public void onCreate(Bundle bundle, Bundle savedInstanceState) {
        super.onCreate(bundle);
        super.onCreate(savedInstanceState);

        View v = findViewById(android.R.id.content);
        v.setFilterTouchesWhenObscured(true);

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
