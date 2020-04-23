package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.model.AssistanceModel;
import br.com.libertyseguros.mobile.view.GlassAssistanceWebView;
import br.com.libertyseguros.mobile.view.ListVehicleAccidentStatus;
import br.com.libertyseguros.mobile.view.Support;

public class AssistanceController {

    private AssistanceModel assistanceModel;


    public AssistanceController(Activity activity){
        assistanceModel = new AssistanceModel(activity);
    }

    public void openAssitance(Activity context, boolean newVA){
        assistanceModel.openAssistance(context, newVA);
    }

    /**
     * Open Support
     * @param context
     */
    public void openSupport(Context context){
        assistanceModel.openSupport(context);
    }

    /**
     * Open Vehicle Accident Screen
     * @param context
     */
    public void openVehicleAccident(Context context){
        assistanceModel.openVehicleAccident(context);
    }

   /**
    * Open Vehicle Accident Screen
    * @param context
    */
    public void openVehicleAccidentStatus(Context context){
        assistanceModel.openVehicleAccidentStatus(context);
    }

    public void openGlassAssistance(Context context){
//        Intent it = new Intent(context, GlassAssistanceWebView.class);
//        context.startActivity(it);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.url_glass_assistance)));
        context.startActivity(browserIntent);
    }

    /**
     * is Login On
     * @return
     */
    public boolean isloginOn() {
        return assistanceModel.isloginOn();
    }
}
