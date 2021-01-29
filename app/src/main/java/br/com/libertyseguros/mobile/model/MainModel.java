package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.net.URLEncoder;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.ManagerLocation;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.view.Assistance;
import br.com.libertyseguros.mobile.view.HomeAssistanceWebView;
import br.com.libertyseguros.mobile.view.Login;
import br.com.libertyseguros.mobile.view.ListPolicy;
import br.com.libertyseguros.mobile.view.Notification;
import br.com.libertyseguros.mobile.view.NovoClubeLiberty;
import br.com.libertyseguros.mobile.view.Register;
import br.com.libertyseguros.mobile.view.Support;
import br.com.libertyseguros.mobile.view.Workshop;
import br.com.libertyseguros.mobile.view.fragment.HomeOff;
import br.com.libertyseguros.mobile.view.fragment.HomeOn;
import br.com.libertyseguros.mobile.view.Profile;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainModel extends BaseModel {

    public static Activity activity;

    private int indexScreen;

    private Fragment fragment;

    private ChangeScreen changeScreen;

    private ManagerLocation managerLocation;

    private boolean isloginOn;

    private LoginBeans lb;

    private Context context;

    private Connection conn;

    private MenuItem menuWorkshop;

    private LogoutModel logoutModel;
    public MainModel(Activity activity) {
        this.activity = activity;

        isloginOn = false;

        lb = infoUser.getUserInfo(activity);


        if (lb.getAccess_token() != null) {
            isloginOn = true;

//            String gcmSend = loadFile.loadPref(Config.TAG, activity, Config.TAGGCMSEND);

            // if(!Config.alreadySentGCM){
            //    Config.alreadySentGCM = true;
            sendToken(activity);
            //}
        } else {
            managerLocation = new ManagerLocation(activity);
            managerLocation.setOnLocationController(new ManagerLocation.OnLocationController() {
                @Override
                public void locationChange(double latitude, double longitude) {

                }

                @Override
                public void locationError() {

                }

                @Override
                public void locationTimeOut() {

                }

                @Override
                public void location(LatLng latLng) {

                }
            });
        }


    }

    public void displayView(int position) {

        indexScreen = position;

        fragment = null;

        Intent it = null;


        if (isloginOn) {
            switch (position) {
                case 0:
                    fragment = new HomeOn().newInstance(menuWorkshop);
                    break;
                case R.id.navigation_drawer_item_1:
                    it = new Intent(activity, Profile.class);
                    Profile.activityBefore = activity;
                    activity.startActivity(it);
                    break;
                case R.id.navigation_drawer_item_2:
                    it = new Intent(activity, ListPolicy.class);
                    Profile.activityBefore = activity;
                    activity.startActivity(it);
                    break;
                case R.id.navigation_drawer_item_3:
                    if(Config.hasHomeAssistance && !Config.hasAutoPolicy ){
                        it = new Intent(context, HomeAssistanceWebView.class);
                    }else{
                        it = new Intent(context, Assistance.class);
                    }
                    context.startActivity(it);
                    break;
                case R.id.navigation_drawer_item_4:
                    Workshop.setManagerLocation(managerLocation);
                    it = new Intent(activity, Workshop.class);
                    activity.startActivity(it);
                    break;
                case R.id.navigation_drawer_item_5:
                    it = new Intent(activity, NovoClubeLiberty.class);
                    activity.startActivity(it);
                    break;
                case R.id.navigation_drawer_item_6:
                    it = new Intent(activity, Notification.class);
                    activity.startActivity(it);
                    break;
                case R.id.navigation_drawer_item_7:
                    openSupport(activity);
                    break;
                case R.id.navigation_drawer_item_8:
                    logoutModel = new LogoutModel(activity.getApplicationContext());
                    logoutModel.logout(activity);
                    break;
                default:
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    fragment = new HomeOff().newInstance(managerLocation);
                    break;
                case R.id.navigation_drawer_item_1:
                    it = new Intent(activity, Login.class);
                    activity.startActivity(it);
                    activity.finish();
                    break;
                case R.id.navigation_drawer_item_2:
                    it = new Intent(activity, Register.class);
                    activity.startActivity(it);
                    Register.activityBefore = activity;
                    break;
                case R.id.navigation_drawer_item_3:
                    Workshop.setManagerLocation(managerLocation);
                    it = new Intent(activity, Assistance.class);
                    activity.startActivity(it);
                    break;
                case R.id.navigation_drawer_item_4:
                    Workshop.setManagerLocation(managerLocation);
                    it = new Intent(activity, Workshop.class);
                    activity.startActivity(it);
                    break;

                case R.id.navigation_drawer_item_5:
                    ClubModel.activityBefore = activity;
                    it = new Intent(activity, NovoClubeLiberty.class);
                    activity.startActivity(it);
                    break;
                case R.id.navigation_drawer_item_6:
                    openSupport(activity);
                    break;

                default:
                    break;
            }
        }


        if (fragment != null) {
            changeScreen.onChangeScreen(position, fragment);
        } else {
            changeScreen.onChangeScreen(position, null);
        }
    }

    public void exit() {
        activity.finish();


    }


    public void setOnChangeScreen(ChangeScreen changeScreen) {
        this.changeScreen = changeScreen;
    }

    public interface ChangeScreen {
        public void onChangeScreen(int position, Fragment fragment);
    }

    /**
     * Get Index Screen
     *
     * @return
     */
    public int getIndexScreen() {
        return indexScreen;
    }

    /**
     * Get GoogleApiClient
     *
     * @return
     */
    public GoogleApiClient getGoogleApiClient() {
        return managerLocation.getGoogleApiClient();
    }

    /**
     * Get Manager Location
     *
     * @return
     */
    public ManagerLocation getManagerLocation() {
        return managerLocation;
    }

    /**
     * Check Google play service
     *
     * @return
     */
    public boolean checkGooglePlayServicesAvailable(Context context) {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get login active or disabled
     *
     * @return
     */
    public boolean isIsloginOn() {
        return isloginOn;
    }

    /*
     * Set login active or disabled
     */
    public void setLoginOn(boolean value) {
        isloginOn = value;
    }

    /**
     * Open Support
     *
     * @param context
     */
    public void openSupport(Context context) {
        Intent it = new Intent(context, Support.class);
        context.startActivity(it);
    }

    /**
     * Open List Policy Screen
     *
     * @param context
     */
    public void openListPolicy(Context context) {
        Intent it = new Intent(context, ListPolicy.class);
        it.putExtra("morePolicy", "1");
        context.startActivity(it);
    }

    /**
     * Get Image User
     *
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser) {
        return infoUser.getImageUser(context, ivImageUser);
    }

    /*
    * Get InfoUser
    * @return
            */
    public LoginBeans getInfoUser() {
        return lb;
    }

    /**
     * Method SendGCM
     *
     * @param ctx
     */
    public void sendToken(Context ctx) {
        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        String param = "";

        String GCM = loadFile.loadPref(Config.TAG, context, Config.TAGGCM);

        if (GCM == null) {
            GCM = "";
        }

        //TODO Remover Log
//        //Log.i(Config.TAG, "TOKEN NOT: " + GCM);

        try {
            //Não remover o teste=1
            param = "?Teste=1&DeviceId=" + Config.getDeviceUID(context) + "&Token=" + URLEncoder.encode(GCM, "UTF-8") + "&System=2";
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Log.i(Config.TAG, "Connection PARAM: " + param);

        if (!GCM.equals("")) {
            conn.startConnection("Notificacao/Token", param, 1, true);
        } else {
            Config.alreadySentGCM = false;
        }
    }


    private void createConnection() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "SendToken: " + result);

                if (result.equals("")) {
                    loadFile.savePref(Config.TAGGCMSEND, "1", Config.TAG, context);
                }
            }
        });
    }

    /**
     * Get Number new notification
     *
     * @param context
     * @return
     */
    public String getNumberNotification(Context context) {
        String number = loadFile.loadPref(Config.TAG, context, Config.TAGNOTIFICAITONNEW);

        if (number == null) {
            number = "0";
        }

        return number;
    }

    public MenuItem getMenuWorkshop() {
        return menuWorkshop;
    }

    public void setMenuWorkshop(MenuItem menuWorkshop) {
        this.menuWorkshop = menuWorkshop;
    }
}
