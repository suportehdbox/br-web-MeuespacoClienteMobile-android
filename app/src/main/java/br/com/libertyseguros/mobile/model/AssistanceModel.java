package br.com.libertyseguros.mobile.model;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.view.HomeAssistanceWebView;
import br.com.libertyseguros.mobile.view.ListVehicleAccident;
import br.com.libertyseguros.mobile.view.ListVehicleAccidentStatus;
import br.com.libertyseguros.mobile.view.Support;
import br.com.libertyseguros.mobile.view.VehicleAccidentLogOff;

public class AssistanceModel extends BaseModel {

    private InfoUser infoUser;

    private boolean isloginOn;

    private LoginBeans lb;
    private Activity currentActivity;

    public AssistanceModel(Activity activity) {
        currentActivity = activity;

        infoUser = new InfoUser();

        if (this.getAccessToken() != null) {
            isloginOn = true;
        } else {
            isloginOn = false;
        }
    }

    public String getAccessToken(){
        if(lb == null){
            lb = infoUser.getUserInfo(this.currentActivity);
        }

        return lb.getAccess_token();
    }

    /**
     * return if user has home assistance coverage
     * @return
     */
    public boolean homeAssistanceAllowed() {
        if(BuildConfig.brandMarketing == 2){
            //Aliro nao tem residencia
            return false;
        }
        return Config.hasHomeAssistance;
    }

      /**
     * Open Assistance 24 hours
     */
    public void openAssistance() {

        if (ContextCompat.checkSelfPermission(this.currentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this.currentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this.currentActivity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            this.startAssistance();
        }

    }

    /**
     * Start 24 hours assist after check required permissions
     */
    private void startAssistance() {
        Intent it = null;

        if (infoUser.getCpfCnpj(this.currentActivity) != null) {
            VehicleAccidentModel.vasb = new VehicleAccidentSendBeans();
            it = new Intent(this.currentActivity, ListVehicleAccident.class);
            it.putExtra("vehicleAccident", "1");
        } else {
            it = new Intent(this.currentActivity, VehicleAccidentLogOff.class);
            it.putExtra("vehicleAccident", "1");
        }
        this.currentActivity.startActivity(it);
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
     * Open Vehicle Accident Screen
     *
     * @param context
     */
    public void openVehicleAccident(Context context) {
        Intent it = null;

        if (infoUser.getCpfCnpj(context) != null) {
            it = new Intent(context, ListVehicleAccident.class);
            VehicleAccidentModel.vasb = new VehicleAccidentSendBeans();

            it.putExtra("vehicleAccident", "0");
        } else {
            it = new Intent(context, VehicleAccidentLogOff.class);
            it.putExtra("vehicleAccident", "0");

        }
        context.startActivity(it);

    }

    /**
     * Open Vehicle Accident Screen
     *
     * @param context
     */
    public void openVehicleAccidentStatus(Context context) {
        Intent it = null;
        it = new Intent(context, ListVehicleAccidentStatus.class);
        context.startActivity(it);

    }


    public boolean checkHomeAssistancePermissions(){
        if (ContextCompat.checkSelfPermission(this.currentActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this.currentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.currentActivity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
            return false;
        }

        return true;

    }

    public void openHomeAssistance() {
        if (checkHomeAssistancePermissions()){
            startHomeIntent();
        }
    }


    public boolean checkPermissionsGranted(int requestCode,
                                           String permissions[], int[] grantResults, boolean startAssist) {

        boolean permissionGranted = true;
        for (int ind = 0; ind < permissions.length; ind++) {
            if (grantResults[ind] != PackageManager.PERMISSION_GRANTED) {
                permissionGranted = false;
            }
        }

        if (!permissionGranted) {
            return false;
        }

        if(startAssist){
            switch (requestCode) {
                case 1:
                    this.openAssistance();
                    break;
                case 2:
                    this.startHomeIntent();
                    break;
            }
        }
        return true;
    }

    private void startHomeIntent() {
        Intent it = new Intent(this.currentActivity, HomeAssistanceWebView.class);
        this.currentActivity.startActivity(it);
    }

    /**
     * is Login On
     *
     * @return
     */
    public boolean isloginOn() {
        return isloginOn;
    }

}
