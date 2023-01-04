package br.com.libertyseguros.mobile.controller;


import android.content.Context;

import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.model.ChangeEmailModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeEmailController {

    private ChangeEmailModel changeEmailModel;

    public ChangeEmailController(Context context, OnConnectionResult onConnectionResult) {
        changeEmailModel = new ChangeEmailModel(context, onConnectionResult);
    }


    /**
     * Valid Fields
     *
     * @param context
     * @param email
     * @param confirmEmail
     * @return
     */
    public String validFields(Context context, String email, String confirmEmail) {
        return changeEmailModel.validFields(context, email, confirmEmail);
    }

    /**
     * Boolean close Screen
     *
     * @return
     */
    public static boolean isCloseScreen() {
        return ChangeEmailModel.isCloseScreen();
    }

    /**
     * Get boolean Close Screen
     *
     * @param closeScreen
     */
    public static void setCloseScreen(boolean closeScreen) {
        ChangeEmailModel.setCloseScreen(closeScreen);
    }

    /**
     * Get Type Error
     *
     * @return
     */
    public int getTypeError() {
        return changeEmailModel.getTypeError();
    }

    /**
     * Set Type Error
     *
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.changeEmailModel.setTypeError(typeError);
    }

    /**
     * Get Message
     *
     * @return
     */
    public MessageBeans getMessage() {
        return changeEmailModel.getMessage();
    }

    /**
     * Set Message
     *
     * @param message
     */
    public void setMessage(MessageBeans message) {
        changeEmailModel.setMessage(message);
    }


    /**
     * Forgot password login on
     *
     * @param ctx
     * @param email
     */
    public void forgotEmailLoginOn(Context ctx, String email, boolean header) {
        changeEmailModel.forgotEmailLoginOm(ctx, email, header);
    }

    public String[] validFieldLoginOn(Context context, String email, String confirmEmail) {
        return changeEmailModel.validFieldLoginOn(context, email, confirmEmail);
    }

    /**
     * Get InfoUser
     * @return
     */
    public LoginBeans getInfoUser(){
        return changeEmailModel.getInfoUser();
    }

    /**
     * Get Image User
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser){
        return changeEmailModel.getImageUser(context, ivImageUser);
    }

}