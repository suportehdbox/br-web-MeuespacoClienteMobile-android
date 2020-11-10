package br.com.libertyseguros.mobile.model;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.ListVision360Beans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.Assistance24WebView;
import br.com.libertyseguros.mobile.view.VehicleAccidentStep1;

public class ListVision360Model {

    private InfoUser infoUser;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private Context context;

    private MessageBeans message;

    private ListVision360Beans listVision360Beans;


    public ListVision360Model(OnConnectionResult OnConnectionResult){
        this.onConnectionResult = OnConnectionResult;
        infoUser = new InfoUser();
    }


    public void checkContentEvent(Context ctx, String policy){
        context = ctx;

        conn = new Connection(context);

        createConnection();

        infoUser.getUserInfo(ctx);

        String param = "";

        param = "?Policy=" + policy +  "&onlyCheck=true";

        conn.startConnection("Segurado/Seguro/Extrato", param, 4, false);
    }

    /**
     * @param ctx
     */
    public void getList(Context ctx, String policy){
        context = ctx;

        conn = new Connection(context);

        createConnection();

        infoUser.getUserInfo(ctx);

        String param = "";

        param = "?Policy=" + policy;
        conn.startConnection("Segurado/Seguro/Extrato", param, 4, false);
    }


    /**
     * Create
     */
    private void createConnection() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try{
                    typeError = 1;
                    onConnectionResult.onError();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "ListVehicleAccidentModel: " + result);

                try {

                    if(result.contains("eventos")){
                        Gson gson = new Gson();

                        listVision360Beans = gson.fromJson(result, ListVision360Beans.class);

                        if(!listVision360Beans.isSuccess()){
                            onConnectionResult.onError();
                        } else {
                            onConnectionResult.onSucess();
                        }
                    } else {
                        Gson gson = new Gson();
                        message = gson.fromJson(result, MessageBeans.class);

                        typeError = 2;
                        onConnectionResult.onError();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Gson gson = new Gson();
                    message = gson.fromJson(result, MessageBeans.class);

                    typeError = 2;
                    onConnectionResult.onError();
                }
            }
        });
    }


    /**
     * Get Type Error
     * @return
     */
    public int getTypeError() {
        return typeError;
    }

    /**
     * Set Type Error
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.typeError = typeError;
    }

    /**
     * Get Message
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Set Message
     * @param message
     */
    public void setMessage(MessageBeans message) {
        this.message = message;
    }

    public ListVision360Beans getListVision360Beans() {
        return listVision360Beans;
    }

    public void setListVision360Beans(ListVision360Beans listVision360Beans) {
        this.listVision360Beans = listVision360Beans;
    }
}
