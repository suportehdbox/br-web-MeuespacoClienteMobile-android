package br.com.libertyseguros.mobile.controller;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.SalesmanBeans;
import br.com.libertyseguros.mobile.model.SupportModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class SupportController {
    private SupportModel supportModel;

    public SupportController(OnConnectionResult onConnectionResult) {
        supportModel = new SupportModel(onConnectionResult);
    }

    /**
     * Call phone
     *
     * @param ctx
     * @param index
     */
    public void call(Activity ctx, int index) {
        supportModel.call(ctx, index);
    }


    /**
     * Login Checks if user is logged in
     *
     * @param ctx
     * @return
     */
    public boolean isLogin(Context ctx) {
        return supportModel.isLogin(ctx);
    }

    /**
     * Get message error
     *
     * @return
     */
    public MessageBeans getMessage() {
        return supportModel.getMessage();
    }

    /**
     * Get type error connection
     *
     * @return
     */
    public int getTypeError() {
        return supportModel.getTypeError();
    }

    /**
     * Get Salesman
     *
     * @return
     */
    public ArrayList<SalesmanBeans> getSalesman() {
        return supportModel.getSalesman();
    }

    /**
     * Method Array salesman
     *
     * @param ctx
     */
    public void getArray(Context ctx) {
        supportModel.getArray(ctx);
    }


    /**
     * Get Cache
     *
     * @return
     */
    public ArrayList<SalesmanBeans> getCache() {
        return supportModel.getCache();
    }

    /**
     * Call Skype
     * @param ctx
     */
    public void skype(Context ctx) {
        supportModel.skype(ctx);
    }
}