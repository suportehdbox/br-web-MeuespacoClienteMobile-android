package br.com.libertyseguros.mobile.controller;


import android.content.Context;

import br.com.libertyseguros.mobile.beans.BarCodeBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.ParcelsBeans;
import br.com.libertyseguros.mobile.model.ParcelsModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class ParcelsController{

    private ParcelsModel parcelsModel;



    public ParcelsController(OnConnectionResult onConnectionResult, String numberPolicy, String contract, String ciaCode, String issuance){
        parcelsModel = new ParcelsModel(onConnectionResult, numberPolicy, contract, ciaCode,  issuance);
    }

    /**
     * Get Parcels Beans
     * @return
     */
    public ParcelsBeans getInstallments(){
        return parcelsModel.getInstallments();
    }
    /**
     * Get Parcels
     * @param ctx
     */
    public void getParcels(Context ctx){
        parcelsModel.getParcels(ctx);
    }

    /**
     * Get message error
     * @return
     */
    public MessageBeans getMessage() {
        return parcelsModel.getMessage();
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return parcelsModel.getTypeError();
    }


    /*
     * Get Cod
     */
    public void getCod(Context context, int number){
        parcelsModel.getCod(context, number);
    }

    /*
   * Get Cod
   */
    public void getNoListCod(Context context, int index){
        parcelsModel.getCodNoListParcel(context, index);
    }


    /**
     * get Type Connection
     * @return
     */
    public int getTypeConnection() {
        return parcelsModel.getTypeConnection();
    }

    /**
     * Set Type Connection
     * @param typeConnection
     */
    public void setTypeConnection(int typeConnection) {
        parcelsModel.setTypeConnection(typeConnection);
    }


    public BarCodeBeans getBarCodeBeans() {
        return parcelsModel.getBarCodeBeans();
    }


}
