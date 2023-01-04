package br.com.libertyseguros.mobile.controller;

import android.app.Activity;
import android.content.Context;

import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.model.ChangePasswordModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePasswordController{

    private ChangePasswordModel changePasswordModel;

    public ChangePasswordController(Context context, OnConnectionResult onConnectionResult){
        changePasswordModel = new ChangePasswordModel(context, onConnectionResult);
    }

    /**
     * Open Main Screen
     * @param context
     */
    public void openMain(Context context){
        changePasswordModel.openMain(context);
    }

    /**
     * Valied Field
     * @param email
     * @param cpf
     * @return
     */
    public String[] validField(Context context, String email, String cpf){
       return changePasswordModel.validField(context, email, cpf);
    }

    /**
     * Valied Field
     * @param password
     * @param passwordNew
     * @param cpf
     * @return
     */
    public String[] validFieldLoginOn(Context context, String password, String passwordNew, String confirmPassword, String cpf) {
        return changePasswordModel.validFieldLoginOn(context, password, passwordNew, confirmPassword, cpf);
    }

    /**
     * Forgot password login on
     * @param ctx
     * @param passwordNew
     */
    public void forgotPasswordLoginOn(Context ctx, String password, String passwordNew, boolean header) {
        changePasswordModel.forgotPasswordLoginOn(ctx, password, passwordNew, header);
    }
        /**
         * Open Screen Register
         * @param context
         */
    public void openRegister(Activity context){
        changePasswordModel.openRegister(context);
    }
    /**
     * Method Forgot Password
     * @param ctx
     * @param cpf
     * @param email
     */
    public void forgotPassword(Context ctx, String cpf, String email){
        changePasswordModel.forgotPassword(ctx, cpf, email);
    }

    /**
     * Get Type Error
     * @return
     */
    public int getTypeError() {
        return changePasswordModel.getTypeError();
    }

    /**
     * Set Type Error
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.changePasswordModel.setTypeError(typeError);
    }

    /**
     * Get Message
     * @return
     */
    public MessageBeans getMessage() {
        return changePasswordModel.getMessage();
    }

    /**
     * Set Message
     * @param message
     */
    public void setMessage(MessageBeans message) {
        changePasswordModel.setMessage(message);
    }

    /**
     * Get InfoUser
     * @return
     */
    public LoginBeans getInfoUser(){
        return changePasswordModel.getInfoUser();
    }

    /**
     * Get Image User
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser){
        return changePasswordModel.getImageUser(context, ivImageUser);
    }

    /**
     * Open Screen Privacy Policy
     * @param context
     */
    public void openPrivacy(Context context){
        changePasswordModel.openPrivacy(context);
    }

    public void changePasswordExpired(Context context){
        changePasswordModel.changePasswordExpired(context);
    }
}
