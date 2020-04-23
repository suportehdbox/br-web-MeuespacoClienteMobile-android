package br.com.libertyseguros.mobile.controller;


import android.content.Context;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.QuestionBeans;
import br.com.libertyseguros.mobile.model.ChangeEmailOffModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;

public class ChangeEmailOffController {

    private ChangeEmailOffModel changeEmailModel;

    public ChangeEmailOffController(Context context, OnConnectionResult onConnectionResult) {
        changeEmailModel = new ChangeEmailOffModel(context, onConnectionResult);
    }


    /**
     * Open Change Email answer
     * @param context
     */
    public void openPartTwo(Context context, int index){
        changeEmailModel.openPartTwo(context, index);
    }

    /**
     * Forgot password login on
     * @param ctx
     * @param cpfCnpj
     */
    public void forgotEmailSendCPF(Context ctx, String cpfCnpj){
        changeEmailModel.forgotEmailSendCPF(ctx, cpfCnpj);
    }

    /**
     * Valid Fields
     * @param context
     * @param cpfCnpj
     * @return
     */
    public String validFieldStepOne(Context context, String cpfCnpj){
        return changeEmailModel.validFieldStepOne(context, cpfCnpj);
    }

    /**
     * Valid Fields
     * @param context
     * @param email
     * @param confirmEmail
     * @return
     */
    public String validFieldStepThree(Context context, String email, String confirmEmail){
        return changeEmailModel.validFieldStepThree(context, email, confirmEmail);
    }


    /**
     * Get Type Error
     * @return
     */
    public int getTypeError() {
        return changeEmailModel.getTypeError();
    }

    /**
     * Set Type Error
     * @param typeError
     */
    public void setTypeError(int typeError) {
        changeEmailModel.setTypeError(typeError);
    }

    /**
     * Get Message
     * @return
     */
    public MessageBeans getMessage() {
        return changeEmailModel.getMessage();
    }

    /**
     * Set Message
     * @param message
     */
    public void setMessage(MessageBeans message) {
        changeEmailModel.setMessage(message);
    }

    /**
     * Get Type Connection
     * @return
     */
    public int getTypeConnection() {
        return changeEmailModel.getTypeConnection();
    }

    /**
     * Set Type Connection
     * @param typeConnection
     */
    public void setTypeConnection(int typeConnection) {
        changeEmailModel.setTypeConnection(typeConnection);
    }

    /**
     * Get Question
     * @return
     */
    public static QuestionBeans getQuestionBeans(){
        return ChangeEmailOffModel.getQuestionBeans();
    }


    /**
     * Forgot email send Answers
     * @param ctx
     */
    public void forgotEmailSendAnswers(Context ctx, String email){
        changeEmailModel.forgotEmailSendAnswers(ctx, email);
    }

    /**
     * Add EditText
     * @param etAdd
     */
    public static void addEditText(EditTextCustom etAdd, int index){
        ChangeEmailOffModel.addEditText(etAdd, index);
    }

    /**
     * Get exit boolean Activity
     * @return
     */
    public static boolean isExit() {
        return ChangeEmailOffModel.isExit();
    }

    /**
     * Set exit boolean
     * @param exit
     */
    public static void setExit(boolean exit) {
        ChangeEmailOffModel.setExit(exit);
    }

    /**
     * Open Screen Change email step three
     * @param context
     */
    public void openChangeEmailStepThree(Context context){
        changeEmailModel.openChangeEmailStepThree(context);
    }
}