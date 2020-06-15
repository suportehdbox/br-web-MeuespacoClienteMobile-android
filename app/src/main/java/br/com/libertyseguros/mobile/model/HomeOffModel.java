package br.com.libertyseguros.mobile.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.beans.WorkshopBeans;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.ManagerLocation;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.view.Assistance;
import br.com.libertyseguros.mobile.view.Login;
import br.com.libertyseguros.mobile.view.NovoClubeLiberty;
import br.com.libertyseguros.mobile.view.Register;
import br.com.libertyseguros.mobile.view.Workshop;

public class HomeOffModel extends BaseModel {

    private ManagerLocation managerLocation;

    private Context context;

    private Connection conn;

    private ArrayList<WorkshopBeans> arrayWorkshop;

    private OnConnectionResult onConnectionResult;

    private Toast toast;

    public HomeOffModel(ManagerLocation managerLocation, OnConnectionResult onConnectionResult) {

        this.managerLocation = managerLocation;

        this.arrayWorkshop = new ArrayList<>();

        this.onConnectionResult = onConnectionResult;
    }

    public void openWorkShop(Context context){
        Intent it = new Intent(context, Workshop.class);
        Workshop.setManagerLocation(managerLocation);
        context.startActivity(it);
    }

    /**
     * Get ManagerLocation
     * @return
     */
    public ManagerLocation getMangagerLocation(){
       return managerLocation;
    }

    /**
     * get list workshop
     */
    public void getWorkshop(Context ctx){
        context = ctx;
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try{
                    onConnectionResult.onError();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess(String result) {
                Gson gson = new Gson();

                try {

                    arrayWorkshop = new ArrayList<WorkshopBeans>();

                    JSONArray ja = new JSONArray(result);

                    for (int ind = 0; ind < ja.length(); ind++) {
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
                String postal = getAddress(managerLocation.getLatLng(), context);

                if(postal == null){
                    postal = "";
                }

                String param = "?UserId=99999&CEP=" + postal.replace("-", "") + "&Radius=30";

                conn.startConnection("Oficina", param, 2);
            }
        }).start();


    }

    /**
     * Get Array Workshop
     * @return
     */
    public ArrayList<WorkshopBeans> getArrayWorkshop(){
        return arrayWorkshop;
    }

    /**
     * Open Intent route
     * @param context
     */
    public void openRoute(Context context){

       try {
           if (arrayWorkshop.size() > 0 && managerLocation.getLatLng() != null) {
               //  Intent navigationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + arrayWorkshop.get(0).getLatitude() +"," + arrayWorkshop.get(0).getLongitude() + "&daddr="+ managerLocation.getLatLng().latitude + "," + managerLocation.getLatLng().longitude));
               // context.startActivity(Intent.createChooser(navigationIntent, "Select an application"));
              // String uri = "geo:" + managerLocation.getLatLng().latitude + "," + managerLocation.getLatLng().longitude + "?q=" + arrayWorkshop.get(0).getLatitude() + "," + arrayWorkshop.get(0).getLongitude();
              // context.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));


               String url = "http://maps.google.com/maps?saddr=" + managerLocation.getLatLng().latitude + "," + managerLocation.getLatLng().longitude + "&daddr=" + arrayWorkshop.get(0).getLatitude() + "," + arrayWorkshop.get(0).getLongitude();

               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
               context.startActivity(intent);
           }
       }catch(Exception ex){
           ex.printStackTrace();

           if(toast != null){
               toast.cancel();
           }

           toast = Toast.makeText(context, context.getResources().getString(R.string.error_route), Toast.LENGTH_LONG);
           toast.show();

       }

    }

    /**
     * Open Intent call
     */
    public void openCall(Context context){

        String number = arrayWorkshop.get(0).getPhone();
        Uri number1 = Uri.parse("tel:" + number);
        Intent dial = new Intent(Intent.ACTION_DIAL);
        dial.setData(number1);
        context.startActivity(dial);
    }

    /**
     * Open Assistence Screen
     */
    public void openAssistance(Context context){
        Intent it = new Intent(context, Assistance.class);
        context.startActivity(it);
    }

    /**
     * Open Screen Login Class
     */
    public void openLogin(Activity activity){
        Intent it = new Intent(activity, Login.class);
        activity.startActivity(it);
        activity.finish();
    }

    /**
     * Open Register Screen
     */
    public void openRegister(Activity activity){
        Intent it = new Intent(activity, Register.class);
        Register.activityBefore = activity;
        activity.startActivity(it);

    }

    /**
     * Open Club Screen
     * @param context
     */
    public void openClub(Context context){
        Intent it = new Intent(context, NovoClubeLiberty.class);
        ClubModel.activityBefore = (Activity) context;
        context.startActivity(it);
    }

    /**
     * Create Operation hours
     * @param index
     * @return
     */
    public String createOperation(int index){
        String operation = "";

        try{
            if(arrayWorkshop.get(index).getWeekStart() != null){
                operation = context.getResources().getString(R.string.operation_part_1) + " " + arrayWorkshop.get(index).getWeekStart().substring(0, arrayWorkshop.get(index).getWeekStart().length() - 3) + " ";

                operation += context.getResources().getString(R.string.operation_part_2) + " " +  arrayWorkshop.get(index).getWeekEnd().substring(0, arrayWorkshop.get(index).getWeekEnd().length() - 3);

                if(arrayWorkshop.get(index).getSaturdayStart() != null){
                    operation +=  " " + context.getResources().getString(R.string.operation_part_3) + " " +  arrayWorkshop.get(index).getSaturdayStart().substring(0, arrayWorkshop.get(index).getSaturdayStart().length() - 3);

                    operation += " " + context.getResources().getString(R.string.operation_part_2) + " " +  arrayWorkshop.get(index).getSaturdayEnd().substring(0, arrayWorkshop.get(index).getSaturdayEnd().length() - 3);
                }
            }

        }catch (Exception ex){
            operation = "";
        }

        return operation;
    }
}
