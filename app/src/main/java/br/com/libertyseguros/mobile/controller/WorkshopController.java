package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.beans.WorkshopBeans;
import br.com.libertyseguros.mobile.libray.ManagerLocation;
import br.com.libertyseguros.mobile.model.WorkshopModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class WorkshopController {

    private WorkshopModel workshopModel;

    public WorkshopController(Activity activity, OnConnectionResult onConnectionResult, ManagerLocation managerLocation) {
        workshopModel = new WorkshopModel(activity, onConnectionResult, managerLocation);
    }


    public void getWorkshop(Context ctx, int type, boolean typeChange) {
        workshopModel.getWorkshop(ctx, type, typeChange);
    }


    public void doSearchWorkshops(Context ctx){
        workshopModel.doSearchWorkshops(ctx);
    }
    /**
     * Get Array Workshop
     *
     * @return
     */
    public ArrayList<WorkshopBeans> getArrayWorkshop() {
        return workshopModel.getArrayWorkshop();
    }

    /**
     * Get ManagerLocation
     *
     * @return
     */
    public ManagerLocation getManagerLocation() {
        return workshopModel.getManagerLocation();
    }


    /**
     * Get GoogleApiClient
     * @return
     */
    public GoogleApiClient getGoogleApiClient(){
        return workshopModel.getManagerLocation().getGoogleApiClient();
    }

    /**
     * Get Manager is null
     * @return
     */
    public boolean isManagerNull(){
        return workshopModel.isManagerNull();
    }


    /**
     * Open Screen Workshop off
     * @param context
     * @param extra
     * @param index
     */
    public void OpenWorkshopOff(Context context, boolean extra, int index){
        workshopModel.OpenWorkshopOff(context, extra, index);
    }


    /**
     * set Update Workshop
     * @param value
     */
    public static void setUpdateWorkshop(boolean value){
        WorkshopModel.setUpdateWorkshop(value);
    }

    /**
     * get Update Workshop
     * @return
     */
    public static boolean isUpdateWorkshop(){
        return WorkshopModel.isUpdateWorkshop();
    }

    /**
     * get String address
     * @return
     */
    public static String getAddress() {
        return WorkshopModel.getAddress();
    }

    /**
     * Set String Address
     * @param address
     */
    public static void setAddress(String address) {
        WorkshopModel.setAddress(address);
    }

    /**
     * Get String Cep
     * @return
     */
    public static String getCep() {
        return WorkshopModel.getCep();
    }

    /**
     * Set String Cep
     * @param cep
     */
    public static void setCep(String cep) {
        WorkshopModel.setCep(cep);
    }

    /**
     * Close Workshop
     * @return
     */
    public static boolean isCloseWorkshop() {

        return WorkshopModel.isCloseWorkshop();
    }


    public void changeSearch(){
        workshopModel.changeSearch();
    }

    public boolean isScoreSearch(){
        return workshopModel.isScoreSearch();
    }
    /**
     * Set close Workshop
     * @param value
     */
    public static void setCloseWorkshop(boolean value) {
        WorkshopModel.setCloseWorkshop(value);
    }


}

