package br.com.libertyseguros.mobile.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import br.com.libertyseguros.mobile.beans.DocumentsBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.model.DocumentsPictureModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class DocumentPicturesController {

    private DocumentsPictureModel documentsPictureModel;

    public DocumentPicturesController(OnConnectionResult onConnectionResult, Context context, int imageX, int imageY) {
        documentsPictureModel = new DocumentsPictureModel(onConnectionResult, context, imageX, imageY);
    }

    /**
     * Get message error
     *
     * @return
     */
    public MessageBeans getMessage() {
        return documentsPictureModel.getMessage();
    }

    /**
     * Get type error connection
     *
     * @return
     */
    public int getTypeError() {
        return documentsPictureModel.getTypeError();
    }

    /**
     * Open Support Screen
     */
    public void openSupport(Context context) {
        documentsPictureModel.openSupport(context);
    }


    /**
     * Open make photo intent
     *
     * @param title
     * @param activity
     * @return
     */
    public Intent makePhotoIntent(String title, Activity activity) {
        return documentsPictureModel.makePhotoIntent(title, activity);
    }

    /**
     * Open Make Photo Intent
     *
     * @param title
     * @param activity
     */
    public void openMakePhotoIntent(String title, Activity activity) {
        documentsPictureModel.openMakePhotoIntent(title, activity);
    }

    public String getRealPathFromURI(Uri contentURI, Context context) {
        return documentsPictureModel.getRealPathFromURI(contentURI, context);
    }

    /**
     * Get Text Size Photos
     *
     * @param context
     * @return
     */
    public String getTextSizePhots(Context context) {
        return documentsPictureModel.getTextSizePhots(context);
    }


    /**
     * Get TypeConnection
     *
     * @return
     */
    public int getTypeConnection() {
        return documentsPictureModel.getTypeConnection();
    }

    /**
     * Check file size
     *
     * @param context
     * @param path
     * @return
     */
    public boolean checkFileSize(Context context, String path) {
        return documentsPictureModel.checkFileSize(context, path);
    }


    /**
     * Get Index Remove
     *
     * @return
     */
    public long getIndexRemove() {
        return documentsPictureModel.getIndexRemove();
    }

    /**
     * Image rotate
     *
     * @param bitmap
     * @param path
     * @return
     */
    public Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        return documentsPictureModel.imageOreintationValidator(bitmap, path);
    }

    /**
     * get Documents
     *
     * @param ctx
     */
    public void getDocuments(Context ctx) {
        documentsPictureModel.getDocuments(ctx);
    }

    /**
     * Delete Documents
     */
    public void deleteImages() {
        documentsPictureModel.deleteImages();
    }

    /**
     * Get Documents
     *
     * @return
     */
    public DocumentsBeans getDocuments() {
        return documentsPictureModel.getDocuments();
    }

    /**
     * Set Documents
     *
     * @param documents
     */
    public void setDocuments(DocumentsBeans documents) {
        documentsPictureModel.setDocuments(documents);
    }

    /**
     * Get bitamp image
     *
     * @param name
     * @return
     */
    public Bitmap getImageBitmap(String name) {
        return documentsPictureModel.getImageBitmap(name);
    }

    /**
     * Get Extension
     *
     * @return
     */
    public String getExtension() {
        return documentsPictureModel.getExtension();
    }

    /**
     * Create Upload Files
     *
     * @param activity
     */
    public void createUploadFiles(Activity activity) {
        documentsPictureModel.createUploadFiles(activity);
    }

    /**
     * Get URI in Bitmap
     *
     * @param inContext
     * @param inImage
     * @return
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        return documentsPictureModel.getImageUri(inContext, inImage);
    }

    /**
     * Open Activity DocumentView
     *
     * @param context
     * @param uriImage
     */
    public void openDocumentView(Context context, String uriImage, boolean isPath) {
        documentsPictureModel.openDocumentView(context, uriImage,  isPath);
    }


    /**
     * Create Thumb Documents image saved
     *
     * @param position
     * @return
     */
    public Bitmap createThumbImageSaved(int position) {
        return documentsPictureModel.createThumbImageSaved(position);
    }

    /**
     * Create Thumb Documents
     *
     * @param path
     * @return
     */
    public Bitmap createBitmapThumb(String path) {
        return documentsPictureModel.createBitmapThumb(path);
    }

    public Uri getOutPutfileUri() {
        return documentsPictureModel.getOutPutfileUri();
    }

    public void setOutPutfileUri(Uri outPutfileUri) {
        documentsPictureModel.setOutPutfileUri(outPutfileUri);
    }

    public DocumentsBeans.DocumentData addNewPhoto(String path) {
        return documentsPictureModel.addNewPhoto(path);
    }
}
