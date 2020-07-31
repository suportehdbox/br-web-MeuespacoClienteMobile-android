package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.RegisterActivationBeans;
import br.com.libertyseguros.mobile.beans.RegisterBeans;
import br.com.libertyseguros.mobile.model.RegisterModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class RegisterController {

    private RegisterModel registerModel;


    public RegisterController(OnConnectionResult onConnectionResult, String idFacebook, boolean isFacebook){
        registerModel = new RegisterModel(onConnectionResult, idFacebook, isFacebook);
    }

    public RegisterController(OnConnectionResult onConnectionResult){
        registerModel = new RegisterModel(onConnectionResult);
    }

    /**
     * Connection Register
     * @param context
     * @param password
     * @param cpf
     * @param email
     */
    public void registerSTEP2(Context context, String password, String cpf, String email, String photoGoogle){
        registerModel.registerSTEP2(context,cpf,  email, password, photoGoogle);
    }


    /**
     * Connection Register
     * @param context
     * @param password
     * @param cpf
     * @param email
     */
    public void register(Context context, String policy, String password, String cpf, String email){
        registerModel.register(context, policy, password ,cpf,  email);
    }

    /**
     * Validate register information
     * @param context
     * @param typePolice
     * @param policy
     * @param cpf
     */
    public void validRegister(Context context, int typePolice, String policy, String cpf){
        registerModel.registerSTEP1(context, typePolice, policy, cpf);
    }

    public void sendActivtaionLink(Context context, String cpf){
        registerModel.getLinkRegister(context, cpf);
    }

    /**
     * Get Activation Register
     * @param context
     */
    public void getRegisterActivation(Context context, String token) {
        registerModel.getRegisterActivation(context, token);
    }

        /**
         * Valid Field
         * @param context
         * @param name
         * @param policy
         * @param cpf
         * @param email
         * @param confirmEmail
         * @param password
         * @param confirmPassword
         * @return
         */
    public String[] validField(Context context, String name, String policy, String cpf, String email, String confirmEmail, String password, String confirmPassword, boolean check) {
        return registerModel.validField(context, name, policy, cpf, email, confirmEmail, password, confirmPassword, check);
    }

    /**
     * Set item error
     * @param warning
     */
    public void setWarning(String[] warning) {
        registerModel.setWarning(warning);
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return registerModel.getTypeError();
    }

    /**
     * Set type error connection
     * @param typeError
     */
    public void setTypeError(int typeError) {
        registerModel.setTypeError(typeError);
    }

    /**
     * Get type connection
     * @return
     */
    public int getTypeConnection() {
        return registerModel.getTypeConnection();
    }

    /**
     * Set type Connection
     * @param typeConnection
     */
    public void setTypeConnection(int typeConnection) {
        registerModel.setTypeConnection(typeConnection);
    }

    /**
     * Get Message Error
     * @return
     */
    public MessageBeans getMessage(){
        return registerModel.getMessage();
    }

    /**
     * Open Terms
     */
    public void openLinkTerms(Context context){
        registerModel.openLinkTerms(context);
    }

    public void openCanalReport(Context context){
        registerModel.openCanalReport(context);
    }

    /**
     * Get Register Error Messagea
     */
    public RegisterBeans getRegisterMessage() { return registerModel.getRegisterMessage(); }

    public void goBackLogin(Activity activity){
        activity.finish();
    }

    public boolean isFacebook() {
        return registerModel.isFacebook();
    }

    public void setFacebook(boolean facebook) {
        registerModel.setFacebook(facebook);
    }

    public RegisterActivationBeans getRegisterActivationBeans() {
        return registerModel.getRegisterActivationBeans();
    }

    public void setRegisterActivationBeans(RegisterActivationBeans registerActivationBeans) {
        registerModel.setRegisterActivationBeans(registerActivationBeans);
    }
}
