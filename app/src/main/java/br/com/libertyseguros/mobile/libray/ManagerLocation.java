package br.com.libertyseguros.mobile.libray;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

import br.com.libertyseguros.mobile.R;

public class ManagerLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private LatLng latLng;

    private LocationRequest mLocationRequest;

    private GoogleApiClient mGoogleApiClient;

    private OnLocationController onLocationController;

    private Activity activity;

    private boolean locationStop;

    private Dialog dialogLocation;

    private OnLocationDialog onLocationDialog;

    private int countErroLocation;

    private Timer timer;

    private TimerTask timerTask;

    private Location lastLocation;

    /**
     * Method Construtor
     * @param activity
     */
    public ManagerLocation(Activity activity) {

        this.activity = activity;

        startManagerLocation();
    }

    /**
     * Start Manager Location
     */
    public void startManagerLocation() {
        countErroLocation = 0;

        locationStop = true;

        configDialog();

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        } else {

            if (!isLocationDisable()) {
                locationStop = true;
                dialogLocation.show();
            } else {
                locationStop = false;
            }
        }

        buildGoogleApiClient();
    }

    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        requestLocation();
    }

    public void requestLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        if(lastLocation != null){
            latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            onLocationController.locationChange(latLng.latitude, latLng.longitude);
        } else {
            onLocationController.locationError();
            locationStop = true;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        try {

            if (lastLocation != null) {
                latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                onLocationController.locationChange(latLng.latitude, latLng.longitude);
            } else {
                onLocationController.locationError();
                locationStop = true;
            }
        }catch (Exception ex){
            Log.i(Config.TAG, ex.toString());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if(onLocationController != null && location != null){
            try {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                onLocationController.locationChange(latLng.latitude, latLng.longitude);
            }catch (Exception ex){
                Log.i(Config.TAG, ex.toString());
            }
        }


    }


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public interface OnLocationDialog{
        public void click(boolean yes);

    }

    public void setOnLocationDialog(OnLocationDialog onLocationDialog){
        this.onLocationDialog = onLocationDialog;
    }

    public interface OnLocationController{
        public void locationChange(double latitude, double longitude);
        public void locationError();
        public void locationTimeOut();
        public void location(LatLng latLng);

    }

    public void setOnLocationController(OnLocationController onLocationController){
        this.onLocationController = onLocationController;
    }

    public boolean isLocation(){
        return locationStop;
    }

    public void setLocation(boolean value){
       this. locationStop = value;
    }

    public GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }


    /**
     * Device location enable
     * @return
     */
    public boolean isLocationDisable() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }

    /**
     * Config dialog enable location
     */
    private void configDialog(){
        dialogLocation  = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);

        dialogLocation.setContentView(R.layout.dialog_enable_location);
        dialogLocation.setCancelable(false);

        TextView tvYes = (TextView) dialogLocation.findViewById(R.id.tv_yes);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLocation.dismiss();

                locationStop = false;

                if(onLocationDialog != null){
                    onLocationDialog.click(true);
                }

                Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(it);
            }
        });

        TextView tvNo = (TextView) dialogLocation.findViewById(R.id.tv_no);

        // if button is clicked, close the custom dialog
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLocation.dismiss();

                locationStop = true;

                if(onLocationDialog != null){
                    onLocationDialog.click(false);
                }
            }
        });
    }

    public Dialog getDialogLocation(){
        return  dialogLocation;
    }


    /**
     * Time out location
     */
    public void setTimeOut(){
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
               if(countErroLocation >= Config.COUNTTIMEOUTLOCATION ){


                   try {
                       if (latLng == null) {
                           onLocationController.locationTimeOut();
                       } else {
                           onLocationController.location(latLng);
                       }
                   }catch(Exception ex){
                       ex.printStackTrace();
                   }

                   timer.cancel();

               } else{

                   if(latLng != null){
                       try{
                           onLocationController.location(latLng);
                       }catch (Exception ex){
                           ex.printStackTrace();
                       }

                       timer.cancel();
                   } else {
                       countErroLocation++;
                   }

               }
            }
        };

        timer.schedule(timerTask, Config.TIMEOUTLOCATION, Config.TIMEOUTLOCATION);
    }

    /**
     * Get OnLocationDialog
     * @return
     */
    public OnLocationDialog getOnLocationDialog(){
        return onLocationDialog;
    }
}
