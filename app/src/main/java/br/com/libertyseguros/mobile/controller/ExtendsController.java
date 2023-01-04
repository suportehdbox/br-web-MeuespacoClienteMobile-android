package br.com.libertyseguros.mobile.controller;


import android.content.Context;

import br.com.libertyseguros.mobile.beans.ExtendsBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.TicketBeans;
import br.com.libertyseguros.mobile.model.ExtendsModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class ExtendsController {

    private ExtendsModel extendsModel;

    public ExtendsController(OnConnectionResult onConnectionResult, String contract, String issuance, String installment, String ciaCode, String cliCode, boolean payment){
        extendsModel = new ExtendsModel(onConnectionResult, contract, issuance, installment, ciaCode, cliCode, payment);
    }


    /**
     * Simulate Extend
     * @param ctx
     */
    public void getSimulateExtend(Context ctx){
        extendsModel.getSimulateExtend(ctx);
    }

    /**
     * Get message error
     * @return
     */
    public MessageBeans getMessage() {
        return extendsModel.getMessage();
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return extendsModel.getTypeError();
    }

    /**
     * Get Type connection
     * @return
     */
    public int getTypeConnection(){
        return extendsModel.getTypeConnection();
    }

    /**
     * Get Extend
     * @return
     */
    public ExtendsBeans getExtendsBeans(){
        return extendsModel.getExtendsBeans();
    }

    /**
     *
     * @param date
     * @return
     */
    public String getDate(String date, Context context){
        return extendsModel.getDate(date, context);
    }


    /**
     * Get Money
     * @param money
     * @param context
     * @return
     */
    public String getMoney(String money, Context context){
        return extendsModel.getMoney(money, context);
    }



    /**
     * Open PDF
     * @param url
     * @param context
     */
    public void openPDF(String url, Context context) {
        extendsModel.openPDF(url, context);
    }


    /**
     * Get TicketBeans
     * @return
     */
    public TicketBeans getTicketBeans(){
        return extendsModel.getTicketBeans();
    }

    /**
     * Copy Text Clipboard
     * @param context
     */
    public void copyText(Context context){
        extendsModel.copyText(context);
    }
}
