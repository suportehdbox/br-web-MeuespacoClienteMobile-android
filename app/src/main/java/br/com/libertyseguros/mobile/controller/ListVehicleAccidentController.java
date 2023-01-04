package br.com.libertyseguros.mobile.controller;


import android.content.Context;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.model.ListVehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class ListVehicleAccidentController {

    private ListVehicleAccidentModel listVehicleAccidentModel;

    public ListVehicleAccidentController(OnConnectionResult OnConnectionResult){
        listVehicleAccidentModel = new ListVehicleAccidentModel(OnConnectionResult);
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
     * Get Vehicle Accident Send Beans
     * @return
     */
    public VehicleAccidentSendBeans[] getVehicleAccidentSendBeans() {
        return listVehicleAccidentModel.getVehicleAccidentSendBeans();
    }




    /**
     * Open Vehicle Accident Screen
     * @param ctx
     * @param index
     */
    public void openVehicleAccident(Context ctx, int index, int type){
        listVehicleAccidentModel.openVehicleAccident(ctx, index, type);
    }
}
