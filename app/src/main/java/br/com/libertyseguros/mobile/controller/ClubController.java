package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.libray.DownloadImageClub;
import br.com.libertyseguros.mobile.model.BaseModel;
import br.com.libertyseguros.mobile.model.ClubModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class ClubController extends BaseModel{

    private ClubModel clubModel;


    public ClubController(Context context){
        clubModel = new ClubModel(null, null,context);
    }

    public ClubController(OnConnectionResult onConnectionResult, DownloadImageClub.OnClubImageDownloaded onClubImageDownloaded, Context context){
        clubModel = new ClubModel(onConnectionResult,onClubImageDownloaded, context);
    }

    public void checkTermsAlreadyAgreed(Activity activity){
        if(clubModel.getLb().getAccess_token() == null || clubModel.getLb().getAccess_token().isEmpty()){
            return;
        }

        if(!clubModel.getAgreedTerms(activity)){
            return;
        }

        this.openClubWebview(activity);
        activity.finish();
    }

    /**
     * Get Club info
     * @param ctx
     */
    public void getClubAccess(Context ctx){
       clubModel.getClubAccess(ctx);
    }



    /**
     * Get message error
     * @return
     */
    public MessageBeans getMessage() {
        return clubModel.getMessage();
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return clubModel.getTypeError();
    }


    /**
     * Open Login Screen
     * @param context
     */
    public void openLogin(Context context){
       clubModel.openLogin(context);
    }

    /**
     * Open Register Screen
     * @param context
     */
    public void openRegister(Context context){
        clubModel.openRegister(context);
    }

    /**
     * Open Forgot Password
     * @param context
     */
    public void openForgotPassword(Context context){
        clubModel.openForgotPassword(context);
    }

    /**
     * Open Club Webview Screen
     */
    public void openClubWebview(Context context){
        clubModel.openClubWebview(context);
    }


       /**
     * Get Url
     * @return
     */
    public String getUrl(Context context) {
        return clubModel.getUrl(context);
    }



}
