package br.com.libertyseguros.mobile.controller;


import android.content.Context;

import br.com.libertyseguros.mobile.beans.ListVision360Beans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.model.ListVehicleAccidentModel;
import br.com.libertyseguros.mobile.model.ListVision360Model;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class ListVision360Controller {

    private ListVision360Model listVision360Model;

    public ListVision360Controller(OnConnectionResult OnConnectionResult){
        listVision360Model = new ListVision360Model(OnConnectionResult);
    }

    /**
     * Get List Vehicle Accident
     * @param ctx
     */
    public void getList(Context ctx, String policy){
        listVision360Model.getList(ctx, policy);
    }

    public void checkContentEvent(Context ctx, String policy) {
        listVision360Model.checkContentEvent(ctx, policy);
    }


        /**
         * Get Type Error
         * @return
         */
    public int getTypeError() {
        return listVision360Model.getTypeError();
    }

    /**
     * Set Type Error
     * @param typeError
     */
    public void setTypeError(int typeError) {
        listVision360Model.setTypeError(typeError);
    }

    /**
     * Get Message
     * @return
     */
    public MessageBeans getMessage() {
        return listVision360Model.getMessage();
    }

    /**
     * Set Message
     * @param message
     */
    public void setMessage(MessageBeans message) {
        listVision360Model.setMessage(message);
    }

    public ListVision360Beans getListVision360Beans() {
        return listVision360Model.getListVision360Beans();
    }


}
