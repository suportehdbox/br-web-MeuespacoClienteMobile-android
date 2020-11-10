package br.com.libertyseguros.mobile.view;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.MainController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.model.MainModel;
import br.com.libertyseguros.mobile.model.PolicyModelV2;
import br.com.libertyseguros.mobile.receiver.ServiceFirebaseMessagingService;
import de.hdodenhof.circleimageview.CircleImageView;

public class Main extends AppCompatActivity {

    public static Activity activity;

    private TextView textViewVersion;

    private DrawerLayout androidDrawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar toolbar;

    private NavigationView navigationView;

    private MainController mainController;

    private Fragment fragmentDefault;

    private boolean firstOpen;

    private Toast toast;

    protected void onDestroy() {
        super.onDestroy();
        ServiceFirebaseMessagingService.removeOnNotificationListener();
    }

    public void onResume() {
        super.onResume();

        if (navigationView != null) {
            for (int ind = 0; ind < navigationView.getMenu().size(); ind++) {
                navigationView.getMenu().getItem(ind).setChecked(false);
            }
        }

        if (PolicyModelV2.isMorePolicy()) {
            PolicyModelV2.setMorePolicy(false);
            mainController.openListPolicy(this);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InfoUser.bmProfile = null;
        activity = this;
        initInstancesDrawer();

        if (!mainController.checkGooglePlayServicesAvailable(this)) {
            if (toast != null) {
                toast.cancel();
            }

            toast = Toast.makeText(this, getString(R.string.message_play_service), Toast.LENGTH_LONG);
            toast.show();
        }

    }

    protected void onStart() {
        if (mainController != null && !mainController.isIsloginOn()) {
            mainController.getGoogleApiClient().connect();
        }
        super.onStart();
    }

    protected void onStop() {
        if (mainController != null && !mainController.isIsloginOn()) {
            mainController.getGoogleApiClient().disconnect();
        }
        super.onStop();
    }


    private void initInstancesDrawer() {

        String textComp = "";
        if (!BuildConfig.prod) {
            textComp = " " + getString(R.string.legend_ac);
        }

        textViewVersion = (TextView) findViewById(R.id.version_name_text_view);
        textViewVersion.setText(getString(R.string.version_name, BuildConfig.VERSION_NAME + textComp));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.title_action_bar_0));

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);

        navigationView.setItemIconTintList(null);

        View view = navigationView.getHeaderView(0);


        navigationView.getMenu().clear();

        setTitle(getString(R.string.title_action_bar_0));

        mainController = new MainController(this);

        ImageView ivLetter = (ImageView) view.findViewById(R.id.iv_letter);
        CircleImageView ci = (CircleImageView) view.findViewById(R.id.profile_image);

        TextView tvName = (TextView) view.findViewById(R.id.tv_name);

        if (mainController.isIsloginOn()) {
            navigationView.inflateMenu(R.menu.drawer_list_item_login);

            MenuItem menuWorkshop = navigationView.getMenu().getItem(3);
            mainController.setMenuWorkshop(menuWorkshop);

            String name = "";

            if (mainController.getInfoUser().getUserName().contains(" ")) {
                name = mainController.getInfoUser().getUserName().split(" ")[0];
            } else {
                name = mainController.getInfoUser().getUserName();
            }


            TextDrawable drawable1 = TextDrawable.builder()
                    .beginConfig().textColor(getResources().getColor(R.color.text_letter_menu)).endConfig()
                    .buildRound(mainController.getInfoUser().getUserName().substring(0, 1), getResources().getColor(R.color.background_sel_navigation_menu));
            ivLetter.setImageDrawable(drawable1);


            if (mainController.getImageUser(this, ci)) {
                ci.setVisibility(View.VISIBLE);
                ivLetter.setVisibility(View.GONE);
            } else {
                ci.setVisibility(View.GONE);
                ivLetter.setVisibility(View.VISIBLE);
            }

            tvName.setText(getString(R.string.hi) + " " + name);

        } else {
            navigationView.inflateMenu(R.menu.drawer_list_item);
            ci.setImageDrawable(getResources().getDrawable(R.drawable.icon_menu_perfil));
            ivLetter.setVisibility(View.GONE);
        }

        androidDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_design_support_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                Main.this, androidDrawerLayout, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (mainController.isIsloginOn()) {
                    if(Config.hasAutoPolicy ||  Config.hasHomeAssistance ){
                        navigationView.getMenu().getItem(2).setVisible(true);
                    }else{
                        navigationView.getMenu().getItem(2).setVisible(false);
                    }

                    showNotificationNew();
                }
            }
        };


        actionBarDrawerToggle.syncState();

        androidDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() == 5) {
                    LoadFile lf = new LoadFile();
                    lf.savePref(Config.TAGNOTIFICAITONNEW, "0", Config.TAG, Main.this);
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                if (firstOpen) {
                    mainController.displayView(menuItem.getItemId());
                } else {
                    firstOpen = true;
                    mainController.displayView(0);
                }

                return true;
            }
        });

        mainController.set(new MainModel.ChangeScreen() {
            @Override
            public void onChangeScreen(int position, Fragment fragment) {
                androidDrawerLayout.closeDrawer(Gravity.LEFT);

                if (position == 0) {
                    fragmentDefault = fragment;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!isFinishing()) {
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().add(R.id.frame_container, fragmentDefault).commit();
                            }


                        }
                    }, 300);

                }
            }
        });


        MenuItem menuItem = navigationView.getMenu().getItem(0);
        navigationView.getMenu().performIdentifierAction(menuItem.getItemId(), 0);
        navigationView.getMenu().getItem(0).setChecked(false);


        if (mainController.isIsloginOn()) {
            showNotificationNew();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        } else if (item.getItemId() == R.id.action_name) {
            mainController.openSupport(Main.this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Log.i(Config.TAG, "PERMISSION_GRANTED - location");

                    if (!mainController.getManagerLocation().isLocationDisable()) {
                        mainController.getManagerLocation().setLocation(true);
                        mainController.getManagerLocation().getDialogLocation().show();
                    } else {
                        mainController.getManagerLocation().requestLocation();
                        mainController.getManagerLocation().setLocation(false);
                        mainController.getManagerLocation().getOnLocationDialog().click(true);
                    }

                } else {

                    //Log.i(Config.TAG, "PERMISSION_DENIED - location");
                    mainController.getManagerLocation().setLocation(true);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public BitmapDrawable writeOnDrawable(int drawableId, String text) {

        if (text.length() == 1) {
            text = "0" + text;
        }

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, (bm.getWidth() - paint.getTextSize()) / 2, (bm.getHeight() + 15) / 2, paint);

        return new BitmapDrawable(bm);
    }


    public void showNotificationNew() {

        String number = mainController.getNumberNotification(this);

        if (number.equals("0")) {
            navigationView.getMenu().getItem(5).setIcon(getResources().getDrawable(R.drawable.icon_menu_notification));
        } else {
            navigationView.getMenu().getItem(5).setIcon(writeOnDrawable(R.drawable.icon_menu_notification_new, number));
        }

    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {


        return super.onMenuOpened(featureId, menu);
    }
}



