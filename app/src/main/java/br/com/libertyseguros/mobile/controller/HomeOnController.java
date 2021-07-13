package br.com.libertyseguros.mobile.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.HomeBeansV2;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.model.HomeOnModel;
import br.com.libertyseguros.mobile.model.PolicyModelV2;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.ChangePasswordLoginOn;
import br.com.libertyseguros.mobile.view.Parcels;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeOnController {
    private HomeOnModel homeOnModel;
    private Dialog dialogPasswordExpired;

    public HomeOnController(OnConnectionResult onConnectionResult, Context context) {
        homeOnModel = new HomeOnModel(onConnectionResult, context);
    }

    /**
     * Get message error
     *
     * @return
     */
    public MessageBeans getMessage() {
        return homeOnModel.getMessage();
    }

    /**
     * Get type error connection
     *
     * @return
     */
    public int getTypeError() {
        return homeOnModel.getTypeError();
    }


    /**
     * Method Get home
     *
     * @param ctx
     */
    public void getHome(Context ctx) {
        homeOnModel.getHome(ctx);
        homeOnModel.validateDevice();
    }


    /**
     * Get InfoUser
     *
     * @return
     */
    public LoginBeans getInfoUser() {
        return homeOnModel.getInfoUser();
    }

    /**
     * Get Image User
     *
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser) {
        return homeOnModel.getImageUser(context, ivImageUser);
    }

    public void checkPasswordExpired(Context context){
        if (getInfoUser().isForceResetPassword() && !Config.aleradyChangedPassword ){

            if (dialogPasswordExpired == null) {
                dialogPasswordExpired = new Dialog(context, R.style.AppThemeDialog);
                dialogPasswordExpired.setCancelable(false);
                dialogPasswordExpired.setContentView(R.layout.dialog_password_expired);

                ImageView ivType = (ImageView) dialogPasswordExpired.findViewById(R.id.iv_ok);
                ivType.setOnClickListener(new View.OnClickListener() {
                    //    @Override
                    public void onClick(View v) {
                        dialogPasswordExpired.dismiss();
                        Intent it = new Intent(context, ChangePasswordLoginOn.class);
                        context.startActivity(it);
                    }
                });
            }
            dialogPasswordExpired.show();
        }
    }

    /**
     * Open Assistence Screen
     */
    public void openAssistance(Context context) {
        homeOnModel.openAssistance(context);
    }

    /**
     * Open Workshop Screen
     *
     * @param context
     */
    public void openWorkShop(Context context) {
        homeOnModel.openWorkShop(context);
    }

    /**
     * Open List Policy
     *
     * @param context
     */
    public void openListPolicy(Context context) {
        homeOnModel.openListPolicy(context);
    }

    public void openListVision(Context context) {
        homeOnModel.openListVision(context);
    }

    /**
     * Get Policy Beans
     *
     * @return
     */
    public HomeBeansV2 getPolicyBeans() {
        return homeOnModel.getPolicyBeans();
    }

    /**
     * Get Size List Policy
     *
     * @return
     */
    public int getSizeListPolicy() {
        return homeOnModel.getSizeListPolicy();
    }

    /**
     * Set List  Policy size
     *
     * @param sizeListPolicy
     */
    public void setSizeListPolicy(int sizeListPolicy) {
        homeOnModel.setSizeListPolicy(sizeListPolicy);
    }

    /**
     * Get Connection Type
     *
     * @return
     */
    public int getTypeConnection() {
        return homeOnModel.getTypeConnection();
    }

    /**
     * Set Connection Type
     *
     * @param typeConnection
     */
    public void setTypeConnection(int typeConnection) {
        homeOnModel.setTypeConnection(typeConnection);
    }

    /**
     * Method Get List Policy
     *
     * @param ctx
     */
    public void getListPolicy(Context ctx, boolean active) {
        homeOnModel.getListPolicy(ctx, active);
    }

    /**
     * Get License Plate
     *
     * @return
     */
    public int licensePlate() {
        return homeOnModel.licensePlate();
    }

    /*
    * Open Detail Screen
    * @param Context
    */
    public void openDetailList(Context context) {
        homeOnModel.openDetailList(context);
    }

    /**
     * Open Extend Screen
     *
     * @param context
     */
    public void openExtend(Context context, int indexPayment, OnBarCode barCode) {
        homeOnModel.openExtend(context, indexPayment, barCode);
    }


    /**
     * Open Club Screen
     *
     * @param context
     */
    public void openClub(Context context) {
        homeOnModel.openClub(context);
    }


    /**
     * Open Vehicle Accident Screen
     *
     * @param context
     */
    public void openVehicleAccidentStatus(Context context) {
        homeOnModel.openVehicleAccidentStatus(context);
    }


    public void getPaymentInfo(Context context){
        homeOnModel.getPaymentsInfo(context);
    }

    /**
     * Show Popup Fingertipes
     *
     * @param context
     * @return
     */
    public boolean showFingerTips(Context context) {
        return homeOnModel.showFingerprints(context);
    }

    /**
     * Enable Fingerprints enable
     *
     * @param context
     */
    public void enableFingerprints(Context context) {
        homeOnModel.enableFingerprints(context);
    }

//    public void setSalesForceSDK(final String policy, final String CPF, final String Ramo) {
//        homeOnModel.setSalesForceSDK(policy, CPF, Ramo);
//    }


    public void openParcels(Context context, int indexPayment){
        PolicyModelV2.setInsurancesSel(getPolicyBeans().getInsurance());
        Intent it = new Intent(context, Parcels.class);
        it.putExtra("policyNumber", getPolicyBeans().getInsurance().getPolicy());
        it.putExtra("ciaCode", getPolicyBeans().getInsurance().getCiaCode() +  "");
        it.putExtra("contract", getPolicyBeans().getInsurance().getContract() + "");
        it.putExtra("issuance", getPolicyBeans().getInsurance().getIssuances()[indexPayment] + "");

        context.startActivity(it);
    }
}