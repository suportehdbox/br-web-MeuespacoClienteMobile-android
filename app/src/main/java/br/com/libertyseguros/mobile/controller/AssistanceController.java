package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.model.AssistanceModel;

public class AssistanceController {

    private AssistanceModel assistanceModel;
    private Dialog dialogMessage;
    public AssistanceController(Activity activity){
        assistanceModel = new AssistanceModel(activity);
    }



    public void openAssitance(){
        assistanceModel.openAssistance();
    }

    public boolean homeAssistanceAllowed() {
        return assistanceModel.homeAssistanceAllowed();
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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.url_glass_assistance)));
        context.startActivity(browserIntent);
    }

    /**
     * Open Home Assistance WebView
     */
    public void openHomeAssistWebView(){
        assistanceModel.openHomeAssistance();
    }

    /**
     * Pass values from activity onRequestPermissionsResult
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @return
     */
    public boolean checkPermissionsGranted(int requestCode,
                                           String permissions[], int[] grantResults) {
        return this.checkPermissionsGranted(requestCode,permissions,grantResults, true);
    }

    /**
     * Pass values from activity onRequestPermissionsResult
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param startAssist
     * @return
     */
    public boolean checkPermissionsGranted(int requestCode,
                                           String permissions[], int[] grantResults, boolean startAssist) {
        return assistanceModel.checkPermissionsGranted(requestCode, permissions, grantResults, startAssist);
    }



    public boolean checkHomeAssistancePermissions(){
        return  assistanceModel.checkHomeAssistancePermissions();
    }



    public String getUserToken(){
        return assistanceModel.getAccessToken();
    }

    /**
     * is Login On
     * @return
     */
    public boolean isloginOn() {
        return assistanceModel.isloginOn();
    }

}
