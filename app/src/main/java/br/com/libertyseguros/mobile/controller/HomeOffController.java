package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.beans.WorkshopBeans;
import br.com.libertyseguros.mobile.libray.ManagerLocation;
import br.com.libertyseguros.mobile.model.HomeOffModel;

public class HomeOffController {

    private HomeOffModel homeOffModel;

    public HomeOffController(ManagerLocation managerLocation, OnConnectionResult onConnectionResult) {
        homeOffModel = new HomeOffModel(managerLocation, onConnectionResult);
    }

    public void openWorkshop(Context ctx) {
        homeOffModel.openWorkShop(ctx);
    }

    public void getWorkshop(Context context) {
        homeOffModel.getWorkshop(context);
    }

    /**
     * Get Array Workshop
     *
     * @return
     */
    public ArrayList<WorkshopBeans> getArrayWorkshop() {
        return homeOffModel.getArrayWorkshop();
    }

    /**
     * Get ManagerLocation
     *
     * @return
     */
    public ManagerLocation getMangagerLocation() {
        return homeOffModel.getMangagerLocation();
    }

    /**
     * Open Intent route
     */
    public void openRoute(Context context) {
        homeOffModel.openRoute(context);
    }

    /**
     * Open Intent call
     */
    public void openCall(Context context) {
        homeOffModel.openCall(context);
    }

    /**
     * Open Assistence Screen
     */
    public void openAssistance(Context context) {
        homeOffModel.openAssistance(context);
    }

    /**
     * Open Screen Login Class
     */
    public void openLogin(Activity activity) {
        homeOffModel.openLogin(activity);
    }

    /**
     * Open Register Screeb
     */
    public void openRegister(Activity activity) {
        homeOffModel.openRegister(activity);
    }

    /*
    * Open Detail Screen
    * @param Context
    */
    public void openDetailList(Context Context) {
        this.openDetailList(Context);
    }

    /**
     * Open Club Screen
     *
     * @param context
     */
    public void openClub(Context context) {
        homeOffModel.openClub(context);
    }

    /**
     * Create Operation hours
     * @param index
     * @return
     */
    public String createOperation(int index) {
        return homeOffModel.createOperation(index);
    }
}