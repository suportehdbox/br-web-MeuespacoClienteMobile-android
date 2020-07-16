package br.com.libertyseguros.mobile.view.baseActivity;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.libertyseguros.mobile.R;

import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.receiver.ServiceFirebaseMessagingService;
import br.com.libertyseguros.mobile.util.AnalyticsApplication;
import br.com.libertyseguros.mobile.util.NavigationApplication;
import br.com.libertyseguros.mobile.view.Support;
import br.com.libertyseguros.mobile.view.custom.NotificationSnackBar;

public class BaseActionBar extends AppCompatActivity implements NavigationApplication.NavigationListener {

    public boolean support;

    private LoadFile lf;

    public AnalyticsApplication application = (AnalyticsApplication) getApplication();

    public FirebaseAnalytics mFirebaseAnalytics;

    public boolean connection;


    @Override
    public void onResume() {
        super.onResume();
        NavigationApplication nav = (NavigationApplication) getApplication();
        nav.setNavigationListener(this);
        ServiceFirebaseMessagingService.setOnNotificationListener(nav.getListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (!connection) {
            if (item.getItemId() == android.R.id.home) {
                super.onBackPressed();
            } else if (item.getItemId() == R.id.action_name) {
                Intent it = new Intent(BaseActionBar.this, Support.class);
                startActivity(it);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!support) {
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        return true;
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mFirebaseAnalytics = application.getFirebaseAnalitycs();


        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.background_action_bar)));
        actionBar.setDisplayShowTitleEnabled(true);
        lf = new LoadFile();


        //NavigationApplication nav = (NavigationApplication) getApplication();
        //nav.setNavigationListener(this);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
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
