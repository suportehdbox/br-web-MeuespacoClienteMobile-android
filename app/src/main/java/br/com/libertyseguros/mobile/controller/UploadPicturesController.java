package br.com.libertyseguros.mobile.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import java.util.ArrayList;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.libray.DocumentsImageManager;
import br.com.libertyseguros.mobile.model.DocumentsPictureModel;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.ViewerDocuments;

public class UploadPicturesController {

    private VehicleAccidentModel vehicleAccidentModel;

    public UploadPicturesController(OnConnectionResult onConnectionResult) {
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
     * Open Support Screen
     */
    public void openSupport(Context context) {
        vehicleAccidentModel.openSupport(context);
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
     *
     * @return
     */
    public void setArraryOutPutfileUri(ArrayList<String> array) {
        vehicleAccidentModel.setArraryOutPutfileUri(array);
    }


    public void setNumberClaim(String numberClaim) {
        vehicleAccidentModel.setNumberClaim(numberClaim);
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


    /* Delete Image
    * @param index
    */
    public void removePhoto(String index) {
        vehicleAccidentModel.removePhoto(index);
    }


    public String getRealPathFromURI(Uri contentURI, Context context) {
        return vehicleAccidentModel.getRealPathFromURI(contentURI, context);
    }

    /**
     * Get Text Size Photos
     *
     * @param context
     * @return
     */
    public String getTextSizePhots(Context context) {
        return vehicleAccidentModel.getTextSizePhots(context);
    }

    public void createUploadFiles(Activity activity) {
        vehicleAccidentModel.createUploadFiles(activity);
    }


    /**
     * Get TypeConnection
     *
     * @return
     */
    public int getTypeConnection() {
        return vehicleAccidentModel.getTypeConnection();
    }

    /**
     * Check file size
     *
     * @param context
     * @param uri
     * @return
     */
    public boolean checkFileSize(Context context, Uri uri) {
        return vehicleAccidentModel.checkFileSize(context, uri);
    }

    /**
     * Get Uri Image
     *
     * @return
     */
    public Uri getUri() {
        return vehicleAccidentModel.getUri();
    }


    /**
     * Get Index Remove
     *
     * @return
     */
    public long getIndexRemove() {
        return vehicleAccidentModel.getIndexRemove();
    }

    /**
     * Image rotate
     *
     * @param bitmap
     * @param path
     * @return
     */
    public Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        return vehicleAccidentModel.imageOreintationValidator(bitmap, path);
    }

    /**
     * Set uri
     *
     * @param uri
     */
    public void setUri(Uri uri) {
        vehicleAccidentModel.setUri(uri);
    }

    /**
     * Get Login Beans
     *
     * @param context
     * @return
     */
    public LoginBeans getLoginBeans(Context context) {
        return vehicleAccidentModel.getLoginBeans(context);
    }

    /**
     * Documents Image Manager
     *
     * @param context
     * @return
     */
    public DocumentsImageManager getDim(Context context) {
        return vehicleAccidentModel.getDim(context);
    }

    /**
     * Open ViewerDocument
     *
     * @param context
     */
    public void openDocuments(Activity context) {
        vehicleAccidentModel.openDocuments(context);
    }

    /**
     * Open ViewerDocument
     *
     * @param context
     */
    public void openDocuments(Activity context, String numberPolicy) {
        vehicleAccidentModel.openDocuments(context, numberPolicy);
    }
}
