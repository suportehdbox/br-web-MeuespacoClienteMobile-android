package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;

import br.com.libertyseguros.mobile.beans.DetailPolicyBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.PolicyBeansV2;
import br.com.libertyseguros.mobile.model.PolicyModelV2;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class PolicyController {
    private PolicyModelV2 policyModel;

    public PolicyController(OnConnectionResult onConnectionResult){
        policyModel = new PolicyModelV2(onConnectionResult);
    }

    /**
     * Get Detail Policy
     * @param ctx
     */
    public void getDetailPolicy(Context ctx){
        policyModel.getDetailPolicy(ctx);
    }

    /**
     * Get List Policy
     * @param ctx
     * @param active
     */
    public void getListPolicy(Context ctx, boolean active){
        policyModel.getListPolicy(ctx, active);
    }

    public void openListVision(Context context) {
        policyModel.openListVision(context);
    }


    /**
     * Get Type Error
     * @return
     */
    public int getTypeError() {
        return policyModel.getTypeError();
    }

    /**
     * Set Type Error
     * @param typeError
     */
    public void setTypeError(int typeError) {
        policyModel.setTypeError(typeError);
    }

    /**
     * Get Message
     * @return
     */
    public MessageBeans getMessage() {
        return policyModel.getMessage();
    }

    /**
     * Set Message
     * @param message
     */
    public void setMessage(MessageBeans message) {
        policyModel.setMessage(message);
    }

    /**
     * Get Type Connection
     * @return
     */
    public int getTypeConnection(){
        return policyModel.getTypeConnection();
    }

    /**
     * Set Policy Beans
     * @return
     */
    public void setPolicyBeans(PolicyBeansV2 policyBeans) {
       policyModel.setPolicyBeans(policyBeans);
    }

    /**
     * Get Policy Beans
     * @return
     */
    public PolicyBeansV2 getPolicyBeans() {
        return policyModel.getPolicyBeans();
    }

    /**
     * Open Detail Policy
     * @param ctx
     * @param index
     */
    public void openDetail(Activity ctx, int index, boolean finish){
        policyModel.openDetail(ctx, index, finish);
    }

    /**
     * Open Vehicle Accident
     * @param ctx
     * @param index
     */
    public void openVehicleAccident(Context ctx, int index){
        policyModel.openVehicleAccident(ctx, index);
    }

    /**
     * Open Documents
     * @param ctx
     * @param index
     */
    public void openDocuments(Context ctx, int index){
        policyModel.openDocuments(ctx, index);
    }
/**
     *
     * @param ctx
     */
    public void openMorePolicy(Context ctx){
        policyModel.openMorePolicy(ctx);
    }

    /**
     * Open Intent call
     */
    public void openCall(Context ctx){
      policyModel.openCall(ctx);
    }


    /**
     * Send Email
     */
    public void sendEmail(Context ctx){
        policyModel.sendEmail(ctx);
    }

    /**
     * Get Detail Policy
     * @return
     */
    public DetailPolicyBeans getDetailPolicyBeans() {
        return policyModel.getDetailPolicyBeans();
    }

    /**
     * Set Detail Policy
     * @param detailPolicyBeans
     */
    public void setDetailPolicyBeans(DetailPolicyBeans detailPolicyBeans) {
        policyModel.setDetailPolicyBeans(detailPolicyBeans);
    }

    /**
     * Get License Plate
     * @return
     */
    public int licensePlate(){
        return policyModel.licensePlate();
    }


    /**
     * Open Parcel
     * @param ctx
     */
    public void openParcels(Context ctx, int indexIssuance){
        policyModel.openParcels(ctx,indexIssuance);
    }

    /**
     * Get is Vehicle Accident
     * @return
     */
    public boolean isVehicleAccident(){
        return policyModel.isVehicleAccident();
    }

    /**
     * Set VehicleAccident
     * @param value
     */
    public void setVehicleAccident(boolean value){
        policyModel.setVehicleAccident(value);
    }

    /**
     * Set Documents
     * @param value
     */
    public void setDocuments(boolean value){
        policyModel.setDocuments(value);
    }

    /**
     * Get is Document
     * @return
     */
    public boolean isDocument(){
        return policyModel.isDocuments();
    }

    /**
    /**
     * Open Extend Screen
     * @param context
     */
    public void openExtend(Context context, int indexPayment, OnBarCode barCode){
        policyModel.openExtend(context, indexPayment, barCode);
    }

    /**
     * Open Insurance Coverage
     * @param context
     */
    public void openInsuranceCoverage(Context context){
        policyModel.openInsuranceCoverage(context);
    }

    /**
     * Get Second Policy
     * @param ctx
     */
    public void getSecondCopyPolicy(Context ctx, boolean shareSecondCopyPolicy){
        policyModel.getSecondCopyPolicy(ctx, shareSecondCopyPolicy);
    }

    /**
     * Start process Second Copy Policy
     * @param ctx
     * @param shareSecondCopyPolicy
     */
    public void startOpenShareSecondCopyPolicy(Context ctx, boolean shareSecondCopyPolicy){
        policyModel.startOpenShareSecondCopyPolicy(ctx, shareSecondCopyPolicy);
    }
}
