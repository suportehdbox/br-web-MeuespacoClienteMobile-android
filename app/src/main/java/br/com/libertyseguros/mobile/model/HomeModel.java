package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.datami.smi.SmiSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.jetbrains.annotations.NotNull;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.NotificationBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.DeviceVerificaionListener;
import br.com.libertyseguros.mobile.libray.DocumentsImageManager;
import br.com.libertyseguros.mobile.libray.DownloadImageHome;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.libray.Security;
import br.com.libertyseguros.mobile.libray.SecurityListener;
import br.com.libertyseguros.mobile.receiver.RegistrationIntentService;
import br.com.libertyseguros.mobile.view.Login;
import br.com.libertyseguros.mobile.view.Main;
import br.com.libertyseguros.mobile.view.Register;
import br.com.libertyseguros.mobile.view.Support;


public class HomeModel extends BaseModel {

    private Activity activity;

    private DownloadImageHome dih;

    private String nameImage;

    private Dialog dialogFreeNavigation;

    public HomeModel(Activity activity, boolean token) {

        this.activity = activity;


        DocumentsImageManager documentsImageManager = new DocumentsImageManager(activity);
        documentsImageManager.dataExpiration();

        String md5 = getMD5(activity);

        dih = new DownloadImageHome(activity);

        nameImage = dih.getNameImage();

        dih.startDownload();


        Security sec = new Security();
        final  Activity act = activity;
        sec.isDeviceCompliance(activity, new SecurityListener() {
            @Override
            public void onSecurityCheckComplete(boolean isCompliant) {
                if(!isCompliant){
                    Toast.makeText(act, "Dispositivo não compatível ou com acesso não permitido ao root", Toast.LENGTH_LONG).show();
                    act.finish();
                    return;
                }
            }
        });


        if (BuildConfig.brandMarketing == 3) {
            if (shouldShowPopUpAgain() && !Config.alreadyShownDialog) {
                //showDialogNavigation();
                //wait for datami response
            } else {
                if (!token) {
                    doLoginStuff();
                }
            }
        } else {
            if (!token) {
                doLoginStuff();
            }
        }


    }

    private void doLoginStuff() {
        String token = loadFile.loadPref(Config.TAG, activity, Config.TAGTOKEN);
        if (token != null && token.equals("1")) {
            openLogin();
        } else {
            InfoUser infoUser = new InfoUser();
            infoUser.saveUserInfo("", activity);
        }
    }

    /**
     * Open Screen Main Class - LoginOff
     */
    public void openSkipLogOff() {

        infoUser.saveUserInfo("", activity);
        Intent it = new Intent(activity, Main.class);
        it.putExtra(Config.TAGLOGINON, "0");
        activity.startActivity(it);
        activity.finish();
    }

    /**
     * Open Screen Login Class
     */
    public void openLogin() {
        Intent it = new Intent(activity, Login.class);
        activity.startActivity(it);
        activity.finish();
    }

    /**
     * Open Register Screeb
     */
    public void openRegister() {
        Intent it = new Intent(activity, Register.class);
        activity.startActivity(it);
        Register.activityBefore_2 = activity;

    }

    public boolean shownPopUp() {
        if (shouldShowPopUpAgain() && !Config.alreadyShownDialog) {

            showDialogNavigation();
            return true;
        }
        return false;
    }

    /**
     * Get Name file image Home
     *
     * @return
     */
    public String getNameImage() {
        return nameImage;
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

    private void showDialogNavigation() {
        Config.alreadyShownDialog = true;

        dialogFreeNavigation = new Dialog(activity, R.style.AppThemeDialog);
        dialogFreeNavigation.setCancelable(false);

        dialogFreeNavigation.setContentView(R.layout.dialog_free_navigation);

        Button im_OK = (Button) dialogFreeNavigation.findViewById(R.id.bt_ok);
        final CheckBox check = (CheckBox) dialogFreeNavigation.findViewById(R.id.checkbox_navigation);

        im_OK.setClickable(true);
        im_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowPopUpAgain(!check.isChecked());
                dialogFreeNavigation.dismiss();
                doLoginStuff();
            }
        });

        if(activity.isFinishing()){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(activity.isDestroyed()){
                return;
            }
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogFreeNavigation.show();
            }
        });

    }

    private void setShowPopUpAgain(boolean bool) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("ShowAgain", bool);
        edit.apply();
    }

    private boolean shouldShowPopUpAgain() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getBoolean("ShowAgain", true);
    }

    public boolean showingDialog() {
        return dialogFreeNavigation.isShowing();
    }
}
