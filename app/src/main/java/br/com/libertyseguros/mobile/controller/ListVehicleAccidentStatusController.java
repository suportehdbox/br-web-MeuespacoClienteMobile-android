package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentStatusBeans;
import br.com.libertyseguros.mobile.model.ListVehicleAccidentStatusModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.DetailPolicy;

public class ListVehicleAccidentStatusController {

    private ListVehicleAccidentStatusModel listVehicleAccidentModel;

    public ListVehicleAccidentStatusController(OnConnectionResult OnConnectionResult){
        listVehicleAccidentModel = new ListVehicleAccidentStatusModel(OnConnectionResult);
    }

    /**
     * Get List Vehicle Accident
     * @param ctx
     */
    public void getList(Context ctx){
        listVehicleAccidentModel.getList(ctx);
    }


    /**
     * Get Type Error
     * @return
     */
    public int getTypeError() {
        return listVehicleAccidentModel.getTypeError();
    }

    /**
     * Set Type Error
     * @param typeError
     */
    public void setTypeError(int typeError) {
        listVehicleAccidentModel.setTypeError(typeError);
    }

    /**
     * Get Message
     * @return
     */
    public MessageBeans getMessage() {
        return listVehicleAccidentModel.getMessage();
    }

    /**
     * Set Message
     * @param message
     */
    public void setMessage(MessageBeans message) {
        listVehicleAccidentModel.setMessage(message);
    }


    /**
     * Get Vehicle Accident Status Send Beans
     * @return
     */
    public VehicleAccidentStatusBeans.Claims[] getVehicleAccidentStatusBeans() {
        return listVehicleAccidentModel.getVehicleAccidentStatusBeans();
    }


    public void goToUploadPictures(int index){
        VehicleAccidentStatusBeans.Claims beans = listVehicleAccidentModel.getVehicleAccidentStatusBeans()[index];
        listVehicleAccidentModel.gotoUploadPictures(beans.getPolicy(), beans.getNumber());



    }

}
