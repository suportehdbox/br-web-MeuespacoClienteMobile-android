package br.com.libertyseguros.mobile.view.baseActivity;


import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.receiver.ServiceFirebaseMessagingService;
import br.com.libertyseguros.mobile.util.NavigationApplication;
import br.com.libertyseguros.mobile.view.custom.NotificationSnackBar;

public class BaseNoActionBar extends AppCompatActivity implements NavigationApplication.NavigationListener {

    @Override
    public void onResume() {
        super.onResume();
        NavigationApplication nav = (NavigationApplication) getApplication();
        nav.setNavigationListener(this);
        ServiceFirebaseMessagingService.setOnNotificationListener(nav.getListener());
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getSupportActionBar().hide();


        //  NavigationApplication nav = (NavigationApplication) getApplication();
        // nav.setNavigationListener(this);
    }

    @Override
    public void OnDataPlanChange(boolean freeNavigation) {
        String msg = getString(R.string.free_navigation);
        if (!freeNavigation) {
            //msg = getString(R.string.paid_navigation);
            return;
        }
        Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT)
                .setDuration(4000).show();
    }

    @Override
    public void OnNotificationReceived(String message) {
        NotificationSnackBar.showNotification(getApplicationContext(), getWindow().getDecorView(), message);
    }

}
