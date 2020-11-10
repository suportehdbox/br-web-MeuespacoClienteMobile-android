package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentStatusBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.DetailPolicy;
import br.com.libertyseguros.mobile.view.Main;
import br.com.libertyseguros.mobile.view.UploadPictures;

public class ListVehicleAccidentStatusModel {

    private InfoUser infoUser;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private Context context;

    private MessageBeans message;

    private VehicleAccidentStatusBeans vehicleAccidentStatusBeans;

    public ListVehicleAccidentStatusModel(OnConnectionResult OnConnectionResult){
        this.onConnectionResult = OnConnectionResult;
        infoUser = new InfoUser();
    }

    /**
     * Get List Vehicle Accident status
     * @param ctx
     */
    public void getList(Context ctx){
        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        infoUser.getUserInfo(ctx);

        String param = "";

        conn.startConnection("Sinistro/StatusSinistro/", param, 2, true);
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
                }catch(Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "ListVehicleAccidentStatusModel: " + result);


                //result = "{\"claims\":[{\"date\":\"2016-01-01T02:00:00Z\",\"number\":1619414,\"policy\":\"3116516480\",\"status\":\"100\",\"type\":10},{\"date\":\"2016-01-01T02:00:00Z\",\"number\":1621478,\"policy\":\"3116516487\",\"status\":\"20\",\"type\":10},{\"date\":\"2016-05-30T03:00:00Z\",\"number\":1622564,\"policy\":\"3116517374\",\"status\":\"20\",\"type\":10},{\"date\":\"2016-07-30T03:00:00Z\",\"number\":1624087,\"policy\":\"3116519898\",\"status\":\"10\",\"type\":10}],\"sucesso\":true,\"message\":\"\",\"rowsAffected\":0}";

                try {

                    if(result.contains("claims")){
                        Gson gson = new Gson();

                        vehicleAccidentStatusBeans = gson.fromJson(result, VehicleAccidentStatusBeans.class);

                        onConnectionResult.onSucess();
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

    public void gotoUploadPictures(String policy, String sinistro){
        Intent it = new Intent(context, UploadPictures.class);
        it.putExtra("policy", policy);
        it.putExtra("sinistro", sinistro);
        context.startActivity(it);
    }

    /**
     * Get Vehicle Accident Status Send Beans
     * @return
     */
    public VehicleAccidentStatusBeans.Claims[] getVehicleAccidentStatusBeans() {
        return vehicleAccidentStatusBeans.getClaims();
    }

}
