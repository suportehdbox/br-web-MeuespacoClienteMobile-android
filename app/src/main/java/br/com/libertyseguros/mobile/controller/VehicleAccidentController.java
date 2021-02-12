package br.com.libertyseguros.mobile.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.NumberWarningVehicleAccidentBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.DocumentsImageManager;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.VehicleAccidentStep4;

public class VehicleAccidentController {

    private VehicleAccidentModel vehicleAccidentModel;

    public VehicleAccidentController(OnConnectionResult onConnectionResult) {
        vehicleAccidentModel = new VehicleAccidentModel(onConnectionResult);
    }

    /**
     * Get message error
     *
     * @return
     */
    public MessageBeans getMessage() {
        return vehicleAccidentModel.getMessage();
    }

    /**
     * Get type error connection
     *
     * @return
     */
    public int getTypeError() {
        return vehicleAccidentModel.getTypeError();
    }


    /**
     * Start recording
     *
     * @throws IOException
     */
    public void startRecording(VehicleAccidentStep4.TimeoutRecordAudio timeoutRecordAudio) throws IOException {
        vehicleAccidentModel.startRecording(timeoutRecordAudio);
    }

    /**
     * Stop recording Audio
     */
    public void stopRecording(Context context) {
        vehicleAccidentModel.stopRecording(context);
    }


    /**
     * Start Play audio
     */
    public void startPlaying(AdjustableImageView ivAudio, Activity activity) {
        vehicleAccidentModel.startPlaying(ivAudio, activity);
    }


    /**
     * Stop Play audio
     */
    public void stopPlaying() {
        vehicleAccidentModel.stopPlaying();
    }

    /**
     * Pause Play audio
     */
    public void pausePlaying() {
        vehicleAccidentModel.pause();
    }

    /**
     * Get Audio play/stop
     *
     * @return
     */
    public boolean isPlay() {
        return vehicleAccidentModel.isPlay();
    }

    /**
     * Set Audio play/stop
     *
     * @return
     */
    public void setPlay(boolean play) {
        vehicleAccidentModel.setPlay(play);
    }

    /**
     * Get Audio Record
     *
     * @return
     */
    public boolean isAudioRecord() {
        return vehicleAccidentModel.isAudioRecord();
    }

    /**
     * Set Audio Record
     *
     * @return
     */
    public void setAudioRecord(boolean audioRecord) {
        vehicleAccidentModel.setAudioRecord(audioRecord);
    }

    /**
     * Delete File Audio
     */
    public void deleteAudio() {
        vehicleAccidentModel.deleteAudio();
    }


    /* Open Vehicle Accident Step 1
    * @param context
    */
    public void openStep1Logoff(Context context){
        vehicleAccidentModel.openStep1Logoff(context);
    }

    /**
     * Open 24 hours
     * @param context
     * @param cpf
     * @param plate
     */
    public void open24hours(Context context, String cpf, String plate){
        vehicleAccidentModel.open24hours(context, cpf, plate);
    }

    /**
     * Open next step screen
     *
     * @param context
     * @param index
     */
    public void openNextStep(Context context, int index) {
        vehicleAccidentModel.openNextStep(context, index);
    }

    /**
     * Open Change Password Screen
     */
    public void openForgotPassword(Context context) {
        vehicleAccidentModel.openForgotPassword(context);
    }

    /**
     * Open Support Screen
     */
    public void openSupport(Context context) {
        vehicleAccidentModel.openSupport(context);
    }

    /**
     * Open Screen Login Class
     */
    public void openLogin(Activity activity) {
        vehicleAccidentModel.openLogin(activity);
    }

    /**
     * Open Register Screen
     */
    public void openRegister(Activity activity) {
        vehicleAccidentModel.openRegister(activity);
    }

    /**
     * Open Change Password Screen
     */
    public void openChangePassword(Activity activity){
        vehicleAccidentModel.openChangePassword(activity);
    }
    /**
     * Open make photo intent
     *
     * @param title
     * @param activity
     * @return
     */
    public Intent makePhotoIntent(String title, Activity activity) {
        return vehicleAccidentModel.makePhotoIntent(title, activity);
    }

    /**
     * Open Make Photo Intent
     *
     * @param title
     * @param activity
     */
    public void openMakePhotoIntent(String title, Activity activity) {
        vehicleAccidentModel.openMakePhotoIntent(title, activity);
    }

    /**
     * Get Array OutPut File Uri
     *
     * @return
     */
    public ArrayList<String> getArrayOutPutfileUri() {
        return vehicleAccidentModel.getArrayOutPutfileUri();
    }

    /**
     * Set Array OutPut File Uri
     * @return
     */
    public void setArraryOutPutfileUri(ArrayList<String> array) {
        vehicleAccidentModel.setArraryOutPutfileUri(array);
    }
    /**
     * Add Array OutPut File Uri
     */
    public void addOutPutfileUri() {
        vehicleAccidentModel.addOutPutfileUri();
    }

    public String getOutPutfileUri() {
        return vehicleAccidentModel.getOutPutfileUri();
    }

    /**
     * @param context
     * @param name
     * @param email
     * @param phone
     * @param yes
     * @param nameDriver
     * @param birthday
     * @param phoneDriver
     * @return
     */
    public String[] validStep2(Context context, String name, String email, String phone, boolean yes, String nameDriver, String birthday, String phoneDriver) {
        return vehicleAccidentModel.validStep2(context, name, email, phone, yes, nameDriver, birthday, phoneDriver);
    }

    /**
     * Valid step 3
     *
     * @param context
     * @param location
     * @param ref
     * @param city
     * @param district
     * @return
     */
    public String[] validStep3(Context context, String location, String ref, String city, String district, String number) {
        return vehicleAccidentModel.validStep3(context, location, ref, city, district, number);
    }

    /* Delete Image
    * @param index
    */
    public void removePhoto(String index){
        vehicleAccidentModel.removePhoto(index);
    }

    /**
     *
     * Send Vehicle Accident
     * @param ctx
     */
    public void sendVehicleAccident(Context ctx){
        vehicleAccidentModel.sendVehicleAccident(ctx);
    }



    public String getRealPathFromURI(Uri contentURI, Context context) {
        return vehicleAccidentModel.getRealPathFromURI(contentURI, context);
    }

    /**
     * Valido Step Login off
     * @param context
     * @param number
     * @param cpfCnpj
     * @return
     */
    public String[] validStepOff(Context context, String number, String cpfCnpj, int type){
        return vehicleAccidentModel.validStepOff(context, number, cpfCnpj, type);
    }

    /**
     * Get Text Size Photos
     * @param context
     * @return
     */
    public String getTextSizePhots(Context context){
        return vehicleAccidentModel.getTextSizePhots(context);
    }

    public void createUploadFiles(Activity activity){
        vehicleAccidentModel.createUploadFiles(activity);
    }

    /**
     * Get Audio File
     * @return
     */
    public File getAudioFile(){
        return vehicleAccidentModel.getAudioFile();
    }


    /**
     * Get NumberWarningVehicleAccidentBeans
     * @return
     */
    public NumberWarningVehicleAccidentBeans getNwvhbeans() {
        return vehicleAccidentModel.getNwvhbeans();
    }

    /**
     * Get TypeConnection
     * @return
     */
    public int getTypeConnection(){
        return  vehicleAccidentModel.getTypeConnection();
    }

    /**
     *
     Check file size
     * @param context
     * @param uri
     * @return
     */
    public boolean checkFileSize(Context context, Uri uri){
        return vehicleAccidentModel.checkFileSize(context, uri);
    }

    /**
     * Get Uri Image
     * @return
     */
    public Uri getUri(){
        return vehicleAccidentModel.getUri();
    }


    /**
     * Get Index Remove
     * @return
     */
    public long getIndexRemove(){
        return vehicleAccidentModel.getIndexRemove();
    }

    /**
     * Image rotate
     * @param bitmap
     * @param path
     * @return
     */
    public Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        return vehicleAccidentModel.imageOreintationValidator(bitmap, path);
    }
    /**
     * Set uri
     * @param uri
     */
    public void setUri(Uri uri){
        vehicleAccidentModel.setUri(uri);
    }

    /**
     * Get Login Beans
     * @param context
     * @return
     */
    public LoginBeans getLoginBeans(Context context){
        return vehicleAccidentModel.getLoginBeans(context);
    }

    /**
     * Get State
     * @return
     */
    public ArrayList<String> getState(){
        return vehicleAccidentModel.getState();
    }


    public void openCities(Context context, int index){
        vehicleAccidentModel.openCities(context,index);

    }

    /**
     * Open ViewerDocument
     * @param context
     */
    public void openDocuments(Activity context){
        vehicleAccidentModel.openDocuments(context);
    }

    /**
     * Documents Image Manager
     * @param context
     * @return
     */
    public DocumentsImageManager getDim(Context context){
        return vehicleAccidentModel.getDim(context);
    }



    /**
     * Crop Image
     * @param uri
     * @param INT_CROP
     * @param activity
     */
    public void cropImage(Uri uri, int INT_CROP, Activity activity){

    }




    public String getWebViewUrl(Context context){
        String url = context.getString(R.string.auto_claim_prod);

        if (!BuildConfig.prod) {
            url = context.getString(R.string.auto_claim_act);
        }
        String plate = VehicleAccidentModel.vasb.getLicensePlate();
        if(plate == null || plate.isEmpty() ){
            plate = VehicleAccidentModel.vasb.getInsuranceStatus().getLicensePlate();
        }


        String cpf = getLoginBeans(context).getCpfCnpj();
        if(cpf == null || cpf.isEmpty()){
            cpf = VehicleAccidentModel.cpfCnpj;
        }

        url += "?plate=" + plate + "&document=" + cpf +
                "&issuingAgency=" + VehicleAccidentModel.vasb.getIssuingAgency() +
                "&itemCode=" + VehicleAccidentModel.vasb.getItemCode() +
                "&brandMarketing=" + BuildConfig.brandMarketing;

        return url;
    }
}