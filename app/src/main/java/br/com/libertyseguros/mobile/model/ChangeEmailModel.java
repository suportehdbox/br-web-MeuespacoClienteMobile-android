package br.com.libertyseguros.mobile.model;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URLEncoder;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.util.ValidCNPJ;
import br.com.libertyseguros.mobile.util.ValidCPF;
import br.com.libertyseguros.mobile.util.ValidEmail;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeEmailModel extends BaseModel{

    private ValidEmail validEmail;

    private ValidCPF validCPF;

    private ValidCNPJ validCNPJ;

    private static boolean closeScreen;

    private Context context;

    private Connection conn;

    private OnConnectionResult onConnectionResult;

    private int typeError;

    private MessageBeans message;

    private LoginBeans loginBeans;

    private String newEmail;

    public ChangeEmailModel(Context context, OnConnectionResult onConnectionResult){
        this.onConnectionResult = onConnectionResult;

        validEmail = new ValidEmail();
        validCPF = new ValidCPF();
        validCNPJ = new ValidCNPJ();

        loginBeans = infoUser.getUserInfo(context);
    }


    /**
     * Forgot password login on
     * @param ctx
     * @param email
     */
    public void forgotEmailLoginOm(Context ctx, String email, boolean header){
        String param = "";
        try{
            context = ctx;


            conn = new Connection(context);

            createConnection();

            newEmail = email;

            param = "Email="+ URLEncoder.encode(email, "UTF-8") + "&CpfCnpj=" + URLEncoder.encode( loginBeans.getCpfCnpj(), "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing;

            conn.startConnection("Acesso/Email", param, 1, header);
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

        /**
         * Valid Fields
         * @param context
         * @param email
         * @param confirmEmail
         * @return
         */
    public String validFields(Context context, String email, String confirmEmail){

        String msg = "";

        if(validEmail.checkemail(email)){
            if(!email.equals(confirmEmail)){
                msg = context.getString(R.string.message_error_email_confirm);
            }
        } else {
            msg = context.getString(R.string.message_error_email);
        }

        return msg;
    }

    /**
     * Boolean close Screen
     * @return
     */
    public static boolean isCloseScreen() {
        return closeScreen;
    }

    /**
     * Get boolean Close Screen
     * @param closeScreen
     */
    public static void setCloseScreen(boolean closeScreen) {
        ChangeEmailModel.closeScreen = closeScreen;
    }

    /**
     * Create
     */
    private void createConnection() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try{
                    typeError = 1;
                    onConnectionResult.onError();
                } catch(Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "ChangeEmail: " + result);

                try {

                    if (result.equals("")) {
                        onConnectionResult.onSucess();
                        saveNewEmail(newEmail, context);
                    } else {

                        Gson gson = new Gson();
                        message = gson.fromJson(result, MessageBeans.class);

                        typeError = 2;
                        onConnectionResult.onError();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    onConnectionResult.onError();
                }
            }
        });
    }


    /**
     * Get Type Error
     * @return
     */
    public int getTypeError() {
        return typeError;
    }

    /**
     * Set Type Error
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.typeError = typeError;
    }

    /**
     * Get Message
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Set Message
     * @param message
     */
    public void setMessage(MessageBeans message) {
        this.message = message;
    }

    /**
     * Valied Field
     * @param email
     * @param confirmEmail
     * @return
     */
    public String[] validFieldLoginOn(Context context, String email, String confirmEmail){

        String msg[] = new String[3];


        for(int ind = 0; ind < msg.length; ind++){
            msg[ind] = "";
        }

       /* if(cpf.equals("")){
           // msg[0] = context.getString(R.string.message_empty_cpf_cnpj);
        } else if(!validCNPJ.isCNPJ(cpf) && !validCPF.isCPF(cpf)){
          //  msg[0] = context.getString(R.string.message_error_cpf_cnpj);
        }*/

        if(email.equals("")){
            msg[1] = context.getString(R.string.message_empty_email);
        }else if(!validEmail.checkemail(email)){
            msg[1] = context.getString(R.string.message_error_email);
        }

        if(!confirmEmail.equals(email)){
            msg[2] = context.getString(R.string.message_error_email_confirm);
        }

        return msg;
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
     * Save New Email
     * @param email
     * @param context
     */
    public void saveNewEmail(String email, Context context){

        loginBeans.setEmail(email);

        String json = gson.toJson(loginBeans);
        infoUser.saveUserInfo(json, context);
    }

}
