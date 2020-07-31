package br.com.libertyseguros.mobile.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.List;

import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.view.ChangeEmailLoginOn;
import br.com.libertyseguros.mobile.view.ChangePasswordLoginOn;
import br.com.libertyseguros.mobile.view.ChangePhoneLoginOn;
import br.com.libertyseguros.mobile.view.Lgpd;
import br.com.libertyseguros.mobile.view.ListPolicy;
import br.com.libertyseguros.mobile.view.Login;
import br.com.libertyseguros.mobile.view.Support;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileModel extends BaseModel {

    private LoginBeans loginBeans;


    public ProfileModel(Context context){
        loginBeans = infoUser.getUserInfo(context);
    }

    /**
     * Open Screen
     * @param context
     * @param index
     */
    public void openScreen(Context context, int index){
        Intent it = null;

        switch(index){
            case 1:
                it = new Intent(context, ChangePasswordLoginOn.class);
                context.startActivity(it);
                break;
            case 2:
                it = new Intent(context, ChangeEmailLoginOn.class);
                context.startActivity(it);
                break;
            case 3:
                it = new Intent(context, ListPolicy.class);
                it.putExtra("documents", "1");
                context.startActivity(it);
                break;
            case 4:
                it = new Intent(context, ChangePhoneLoginOn.class);
                context.startActivity(it);

                break;

        }
    }

    /**
     * Logout
     * @param activity
     */
    public void logout(Activity activity){
        activity.finish();
        MainModel.activity.finish();
        loadFile.savePref(Config.TAGHOMEON, "", Config.TAG, activity);
        loadFile.savePref(Config.TAGTOKEN, "0", Config.TAG, activity);
        infoUser.saveUserInfo("", activity);
        Intent it = new Intent(activity, Login.class);
        activity.startActivity(it);
    }

    /**
     * Open Support
     * @param context
     */
    public void openSupport(Context context){
        Intent it = new Intent(context, Support.class);
        context.startActivity(it);
    }

    public void openLgpd(Context context){
        Intent it = new Intent(context, Lgpd.class);
        context.startActivity(it);
    }
    /**
     * Get InfoUser
     * @return
     */
    public LoginBeans getInfoUser(){
        return loginBeans;
    }


    /**
     * Get Image User
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser){
        return infoUser.getImageUser(context, ivImageUser);
    }

    /**
     * Save id Facebook
     * @param idFacebook
     * @param context
     */
    public void saveIdFacebook(String idFacebook, boolean isFacebook, Context context){
        loginBeans.setIdFacebook(idFacebook);

        if(isFacebook){
            loginBeans.setHasFacebook(true);
        } else {
        }

        String json = gson.toJson(loginBeans);
        infoUser.saveUserInfo(json, context);
    }


    /**
     * Re create Info user
     * @param context
     */
    public void reCreateInfoUser(Context context){
        loginBeans = infoUser.getUserInfo(context);
    }

    /**
     * SingIn Google Plus
     *
     * @param context
     * @param mGoogleSignInClient
     * @param RC_SIGN_IN
     */
    public void signInGoogle(Activity context, GoogleSignInClient mGoogleSignInClient, int RC_SIGN_IN) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        context.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
