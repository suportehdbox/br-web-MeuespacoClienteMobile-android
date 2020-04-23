package br.com.libertyseguros.mobile.model;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.view.Assistance24WebView;
import br.com.libertyseguros.mobile.view.ListVehicleAccident;
import br.com.libertyseguros.mobile.view.ListVehicleAccidentStatus;
import br.com.libertyseguros.mobile.view.Support;
import br.com.libertyseguros.mobile.view.VehicleAccidentLogOff;

public class AssistanceModel extends BaseModel {

    private InfoUser infoUser;

    private boolean isloginOn;

    private LoginBeans lb;

    public AssistanceModel(Activity activity) {
        infoUser = new InfoUser();
        lb = infoUser.getUserInfo(activity);

        if(lb.getAccess_token() != null) {
            isloginOn = true;
        } else {
            isloginOn = false;
        }
    }

    /**
     * Open Assistance 24 hours
     * @param context
     */
    public void openAssistance(Activity context, boolean newVA){
        //CallAssistance24 callAssistance24 = new CallAssistance24();
        //callAssistance24.call(context);
        Intent it = null;

        if(infoUser.getCpfCnpj(context) != null){
            VehicleAccidentModel.vasb = new VehicleAccidentSendBeans();

            if(newVA){
                it = new Intent(context, ListVehicleAccident.class);
                it.putExtra("vehicleAccident", "1");
            } else {

//                it = new Intent(context, Assistance24WebView.class);
//                it.putExtra("chassi", "");
//                it.putExtra("cpf", infoUser.getCpfCnpj(context));
//                it.putExtra("plate", "");
//                it.putExtra("callButton", "btAcompanharAssistencia");

            }

        } else {
            it = new Intent(context, VehicleAccidentLogOff.class);
            if(newVA){
                it.putExtra("vehicleAccident", "1");
            } 
        }
        context.startActivity(it);
    }

    /**
     * Open Support
     * @param context
     */
    public void openSupport(Context context){
        Intent it = new Intent(context, Support.class);
        context.startActivity(it);
    }

    /**
     * Open Vehicle Accident Screen
     * @param context
     */
    public void openVehicleAccident(Context context){
        Intent it = null;

        if(infoUser.getCpfCnpj(context) != null){
            it = new Intent(context, ListVehicleAccident.class);
            VehicleAccidentModel.vasb = new VehicleAccidentSendBeans();

            it.putExtra("vehicleAccident", "0");
        } else {
            it = new Intent(context, VehicleAccidentLogOff.class);
            it.putExtra("vehicleAccident", "0");

        }
        context.startActivity(it);

    }

    /**
     * Open Vehicle Accident Screen
     * @param context
     */
    public void openVehicleAccidentStatus(Context context){
        Intent it = null;
        it = new Intent(context, ListVehicleAccidentStatus.class);
        context.startActivity(it);

    }

    /**
     * is Login On
     * @return
     */
    public boolean isloginOn() {
        return isloginOn;
    }

}
