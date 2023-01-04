package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;

import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.libray.ManagerLocation;
import br.com.libertyseguros.mobile.model.MainModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainController {

    private MainModel mainModelModel;

    public MainController(Activity activity) {
        mainModelModel = new MainModel(activity);
    }

    public void displayView(int index) {
        mainModelModel.displayView(index);
    }

    public void set(MainModel.ChangeScreen changeScreen) {
        mainModelModel.setOnChangeScreen(changeScreen);
    }

    /**
     * Get GoogleApiClient
     *
     * @return
     */
    public GoogleApiClient getGoogleApiClient() {
        return mainModelModel.getGoogleApiClient();
    }

    /**
     * Get Manager Location
     *
     * @return
     */
    public ManagerLocation getManagerLocation() {
        return mainModelModel.getManagerLocation();
    }

    /**
     * Get Index Screen
     *
     * @return
     */
    public int getIndexScreen() {
        return mainModelModel.getIndexScreen();
    }


    /**
     * Check Google play service
     *
     * @return
     */
    public boolean checkGooglePlayServicesAvailable(Context context) {
        return mainModelModel.checkGooglePlayServicesAvailable(context);
    }

    /**
     * Get login active or disabled
     *
     * @return
     */
    public boolean isIsloginOn() {
        return mainModelModel.isIsloginOn();
    }

    /*
     * Set login active or disabled
     */
    public void setLoginOn(boolean value) {
        mainModelModel.setLoginOn(value);
    }

    /**
     * Open Support
     *
     * @param context
     */
    public void openSupport(Context context) {
        mainModelModel.openSupport(context);
    }

    /**
     * Get Image User
     *
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser) {
        return mainModelModel.getImageUser(context, ivImageUser);
    }

    /*
    * Get InfoUser
    * @return
            */
    public LoginBeans getInfoUser() {
        return mainModelModel.getInfoUser();
    }

    /**
     * Open List Policy Screen
     *
     * @param context
     */
    public void openListPolicy(Context context) {
        mainModelModel.openListPolicy(context);
    }


    /**
     * Get Number new notification
     *
     * @param context
     * @return
     */
    public String getNumberNotification(Context context) {
        return mainModelModel.getNumberNotification(context);
    }

    public void setMenuWorkshop(MenuItem menuItem){
        mainModelModel.setMenuWorkshop(menuItem);
    }

}