package br.com.libertyseguros.mobile.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.model.ProfileModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileController {

    private ProfileModel profileModel;

    public ProfileController(Context context){
        profileModel = new ProfileModel(context);
    }

    /**
     * Open Screen
     * @param context
     * @param index
     */
    public void openScreen(Context context, int index){
        profileModel.openScreen(context, index);
    }

    /**
     * Logout
     * @param activity
     */
    public void logout(Activity activity) {
        profileModel.logout(activity);
    }

    /**
     * Open Support
     * @param context
     */
    public void openSupport(Context context){
        profileModel.openSupport(context);
    }

    public void openLgpd(Context context){
        profileModel.openLgpd(context);
    }

    /**
     * Get InfoUser
     * @return
     */
    public LoginBeans getInfoUser(){
        return profileModel.getInfoUser();
    }

    /**
     * Get Image User
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser){
        return profileModel.getImageUser(context, ivImageUser);
    }


    /**
     * Save id Facebook
     * @param idFacebook
     * @param context
     */
    public void saveIdFacebook(String idFacebook, boolean isFacebook, Context context){
        profileModel.saveIdFacebook(idFacebook, isFacebook, context);
    }

    /**
     * Re create Info user
     * @param context
     */
    public void reCreateInfoUser(Context context){
        profileModel.reCreateInfoUser(context);
    }

    /**
     * SingIn Google Plus
     *
     * @param context
     * @param mGoogleSignInClient
     * @param RC_SIGN_IN
     */
    public void signInGoogle(Activity context, GoogleSignInClient mGoogleSignInClient, int RC_SIGN_IN) {
        profileModel.signInGoogle(context, mGoogleSignInClient, RC_SIGN_IN);
    }
}
