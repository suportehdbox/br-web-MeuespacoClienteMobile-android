package br.com.libertyseguros.mobile.controller;


import android.content.Context;

import br.com.libertyseguros.mobile.beans.InfoPhoneBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.model.ChangeEmailModel;
import br.com.libertyseguros.mobile.model.ChangePhoneModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePhoneController {

    private ChangePhoneModel changePhoneModel;

    public ChangePhoneController(Context context, OnConnectionResult onConnectionResult) {
        changePhoneModel = new ChangePhoneModel(context, onConnectionResult);
    }


    /**
     * Valid Fields
     *
     * @param context
     * @param email
     * @param mobilePhone
     * @return
     */
    public String[] validFields(Context context, String email, String mobilePhone) {
        return changePhoneModel.validFields(context, email, mobilePhone);
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
        return changePhoneModel.getTypeError();
    }

    /**
     * Set Type Error
     *
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.changePhoneModel.setTypeError(typeError);
    }

    /**
     * Get Message
     *
     * @return
     */
    public MessageBeans getMessage() {
        return changePhoneModel.getMessage();
    }

    /**
     * Set Message
     *
     * @param message
     */
    public void setMessage(MessageBeans message) {
        changePhoneModel.setMessage(message);
    }


    /**
     * Send phone update
     * @param ctx
     * @param phone
     * @param ramal
     * @param phoneMobile
     */
    public void sendPhone(Context ctx, String phone, String ramal, String phoneMobile) {
        changePhoneModel.sendPhone(ctx, phone, ramal, phoneMobile);
    }



    /**
     * Get InfoUser
     * @return
     */
    public LoginBeans getInfoUser(){
        return changePhoneModel.getInfoUser();
    }

    /**
     * Get Image User
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser){
        return changePhoneModel.getImageUser(context, ivImageUser);
    }


    /**
     * Get Phone Info
     * @param ctx
     */
    public void getPhoneInfo(Context ctx) {
        changePhoneModel.getPhoneInfo(ctx);
    }

    /**
     * Get type connection
     * @return
     */
    public int getTypeConnection(){
        return changePhoneModel.getTypeConnection();
    }

    public InfoPhoneBeans getInfoPhoneBeans() {
        return changePhoneModel.getInfoPhoneBeans();
    }

    public void setInfoPhoneBeans(InfoPhoneBeans infoPhoneBeans) {
        changePhoneModel.setInfoPhoneBeans(infoPhoneBeans);
    }
}