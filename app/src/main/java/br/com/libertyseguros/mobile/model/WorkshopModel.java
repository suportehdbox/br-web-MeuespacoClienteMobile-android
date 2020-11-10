package br.com.libertyseguros.mobile.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;

import br.com.libertyseguros.mobile.beans.WorkshopBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.ManagerLocation;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.WorkshopOff;

public class WorkshopModel extends BaseModel {

    private ManagerLocation managerLocation;

    private Context context;

    private Connection conn;

    private ArrayList<WorkshopBeans> arrayWorkshop;

    private OnConnectionResult onConnectionResult;

    private boolean managerNull;

    private static boolean updateWorkshop;

    private static String address;

    private static String cep;

    private int typeFilter;

    private static boolean closeWorkshop;

    private boolean scoreSearch = true;

    public WorkshopModel(Activity activity, OnConnectionResult onConnectionResult, ManagerLocation managerLocation) {

        this.arrayWorkshop = new ArrayList<>();

        if(managerLocation == null){
            managerNull = false;
            this.managerLocation = new ManagerLocation(activity);
        } else {
            managerNull = true;
            this.managerLocation = managerLocation;
        }

        this.onConnectionResult = onConnectionResult;
    }

    /**
     * Get ManagerLocation
     * @return
     */
    public ManagerLocation getManagerLocation(){
       return managerLocation;
    }

    /**
     * get list workshop
     */
    public void getWorkshop(Context ctx, int type, boolean typeChange){

        if(typeChange){
            this.typeFilter = type;
        }

        doSearchWorkshops(ctx);
    }


    public void doSearchWorkshops(Context ctx){
        context = ctx;
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try{
                    onConnectionResult.onError();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "WorkshopModel: " + result);

                Gson gson = new Gson();

                try {

                    arrayWorkshop = new ArrayList<WorkshopBeans>();

                    JSONArray ja = new JSONArray(result);


                    for (int ind = 0; ind < ja.length(); ind++) {
                        if(ind == 0) {
                            arrayWorkshop = new ArrayList<WorkshopBeans>();
                        }

                        WorkshopBeans wb = new WorkshopBeans();
                        wb = gson.fromJson(ja.get(ind).toString(), WorkshopBeans.class);
                        arrayWorkshop.add(wb);
                    }

                    onConnectionResult.onSucess();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    onConnectionResult.onError();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String postal = "";

                if(typeFilter == 0){
                    postal  = getAddress(managerLocation.getLatLng(), context);
                } else if(typeFilter == 1){
                    postal  = cep;
                } else if(typeFilter == 2){
                    postal  = getAddress(address, context);
                }

                String param = "";

                //postal = "01311000";

                String radius = "30";

                String search = "Score";
                if(!scoreSearch){
                    search = "Distancia";
                }

                try{
                    param = "?UserId=99999&CEP=" + URLEncoder.encode(postal.replace("-", ""), "UTF-8") + "&Radius=" + radius+"&TipoPesquisa="+search;
                } catch (Exception ex){
                    ex.printStackTrace();
                }


                conn.startConnection("Oficina", param, 2);
            }
        }).start();


    }







    /**
     * Get Array Workshop
     * @return
     */
    public void OpenWorkshopOff(Context context, boolean extra, int index){
        Intent it = new Intent(context, WorkshopOff.class);
        if(extra){
            it.putExtra("changemessage", "" + index);
        }
        context.startActivity(it);
    }

    public void changeSearch(){
        scoreSearch = !scoreSearch;
    }

    public boolean isScoreSearch(){
        return scoreSearch;
    }

    /**
     * Get Array Workshop
     * @return
     */
    public ArrayList<WorkshopBeans> getArrayWorkshop(){
        return arrayWorkshop;
    }


    /**
     * Get Manager is null
     * @return
     */
    public boolean isManagerNull(){
        return managerNull;
    }

    /**
     * set Update Workshop
     * @param value
     */
    public static void setUpdateWorkshop(boolean value){
        updateWorkshop = value;
    }

    /**
     * get Update Workshop
     * @return
     */
    public static boolean isUpdateWorkshop(){
        return updateWorkshop;
    }

    /**
     * get String address
     * @return
     */
    public static String getAddress() {
        return address;
    }

    /**
     * Set String Address
     * @param address
     */
    public static void setAddress(String address) {
        WorkshopModel.address = address;
    }

    /**
     * Get String Cep
     * @return
     */
    public static String getCep() {
        return cep;
    }

    /**
     * Set String Cep
     * @param cep
     */
    public static void setCep(String cep) {
        WorkshopModel.cep = cep;
    }

    /**
     * Close Workshop
     * @return
     */
    public static boolean isCloseWorkshop() {
        return closeWorkshop;
    }

    /**
     * Set close Workshop
     * @param value
     */
    public static void setCloseWorkshop(boolean value) {
        closeWorkshop = value;
    }
}