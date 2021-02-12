package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.ListVehicleAccidentBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

import br.com.libertyseguros.mobile.view.Assistance24WebView;
import br.com.libertyseguros.mobile.view.AutoClaimWebView;
import br.com.libertyseguros.mobile.view.VehicleAccidentStep1;

public class ListVehicleAccidentModel {

    private InfoUser infoUser;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private Context context;

    private MessageBeans message;

    private ListVehicleAccidentBeans listVehicleAccidentBeans;


    public ListVehicleAccidentModel(OnConnectionResult OnConnectionResult){
        this.onConnectionResult = OnConnectionResult;
        infoUser = new InfoUser();
    }

    /**
     * Get List Vehicle Accident
     * @param ctx
     */
    public void getList(Context ctx){
        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        infoUser.getUserInfo(ctx);

        String param = "";

        if(VehicleAccidentModel.numberPolicy.equals("")){
            param = "?CpfCnpj=" + infoUser.getCpfCnpj(ctx) +  "&brandMarketing=" + BuildConfig.brandMarketing;
            param += "&AppVersion="+BuildConfig.VERSION_NAME;

            conn.startConnection("Sinistro/Itens", param, 2, true);

        } else {

            char c = VehicleAccidentModel.numberPolicy.charAt(0);

            if (Character.isDigit(c)) {
                param = "?CpfCnpj=" + VehicleAccidentModel.cpfCnpj + "&Policy=" + VehicleAccidentModel.numberPolicy +  "&brandMarketing=" + BuildConfig.brandMarketing;

            } else if (Character.isLetter(c)) {
                param = "?CpfCnpj=" + VehicleAccidentModel.cpfCnpj + "&LicensePlate=" + VehicleAccidentModel.numberPolicy +  "&brandMarketing=" + BuildConfig.brandMarketing;
            }

            param += "&AppVersion="+BuildConfig.VERSION_NAME;

            conn.startConnection("Sinistro/Itens", param, 4, false);

        }


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

                    if(result.contains("insurances")){
                        Gson gson = new Gson();

                        listVehicleAccidentBeans = gson.fromJson(result, ListVehicleAccidentBeans.class);

                        ArrayList<VehicleAccidentSendBeans> listVehicleAccidentBeansTemp = new ArrayList<VehicleAccidentSendBeans>();

                        for(int ind = 0; ind <  listVehicleAccidentBeans.getInsurances().length; ind++){
                            if(listVehicleAccidentBeans.getInsurances()[ind].getBranch().equals(Config.auto)){
                                listVehicleAccidentBeansTemp.add(listVehicleAccidentBeans.getInsurances()[ind]);
                            }
                        }

                        if(listVehicleAccidentBeansTemp.size() > 0){
                            listVehicleAccidentBeans.setInsurances(new VehicleAccidentSendBeans[listVehicleAccidentBeansTemp.size()]);
                            for(int indj = 0; indj < listVehicleAccidentBeansTemp.size(); indj++){
                                listVehicleAccidentBeans.getInsurances()[indj] = listVehicleAccidentBeansTemp.get(indj);
                            }
                        } else {
                            listVehicleAccidentBeans.setInsurances(new VehicleAccidentSendBeans[0]);

                        }


                        if(listVehicleAccidentBeans.getInsurances().length < 1){

                            message = new MessageBeans();

                            message.setMessage(context.getResources().getString(R.string.listvaerror));
                            typeError = 2;
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


    /**
     * Get Vehicle Accident Send Beans
     * @return
     */
    public VehicleAccidentSendBeans[] getVehicleAccidentSendBeans() {
        return listVehicleAccidentBeans.getInsurances();
    }




    /**
     * Open Detail Policy
     * @param ctx
     * @param index
     */
    public void openVehicleAccident(Context ctx, int index, int type){
        if(type == 0){
            VehicleAccidentModel.vasb = listVehicleAccidentBeans.getInsurances()[index];
            Intent it;
            if(Config.ClaimWebView){
                it = new Intent(ctx, AutoClaimWebView.class);
            }else{
                it = new Intent(ctx, VehicleAccidentStep1.class);
            }

            ctx.startActivity(it);
        } else {

            String bt;

            bt = "btEmergencia";

             Intent intent = new Intent(ctx, Assistance24WebView.class);
            intent.putExtra("chassi", "");
            intent.putExtra("cpf", infoUser.getCpfCnpj(ctx));
            intent.putExtra("plate", listVehicleAccidentBeans.getInsurances()[index].getInsuranceStatus().getLicensePlate());
            intent.putExtra("callButton", bt);

            //Log.i(Config.TAG, "New 24 hours: " + "CPF " + infoUser.getCpfCnpj(ctx) + " - Plate " + listVehicleAccidentBeans.getInsurances()[index].getInsuranceStatus().getLicensePlate());


            ctx.startActivity(intent);

        }

    }



}
