package br.com.libertyseguros.mobile.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import br.com.libertyseguros.mobile.util.ValidPassword;
import br.com.libertyseguros.mobile.view.Main;
import br.com.libertyseguros.mobile.view.PrivacyPolicy;
import br.com.libertyseguros.mobile.view.Register;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePasswordModel extends BaseModel {

    private Context context;

    private Connection conn;

    private OnConnectionResult onConnectionResult;

    private ValidCPF validCPF;

    private ValidEmail validEmail;

    private ValidCNPJ validCNPJ;

    private ValidPassword validPassword;

    private int typeError;

    private MessageBeans message;

    private LoginBeans loginBeans;

    public ChangePasswordModel(Context context, OnConnectionResult onConnectionResult){
        this.onConnectionResult = onConnectionResult;

        validCPF = new ValidCPF();
        validCNPJ = new ValidCNPJ();
        validEmail = new ValidEmail();
        validPassword = new ValidPassword();

        loginBeans = infoUser.getUserInfo(context);
    }

    /**
     * Open Main Screen
     *
     * @param context
     */
    public void openMain(Context context) {
        LoginModel.setCloseScreen(true);
        infoUser.saveUserInfo("", context);
        Intent it = new Intent(context, Main.class);
        context.startActivity(it);
        ((Activity) context).finish();
    }


    /**
     * Open Screen Register
     * @param context
     */
    public void openRegister(Activity context){
        Intent it = new Intent(context, Register.class);

        Register.activityBefore = context;

        context.startActivity(it);
    }

    /**
     * Method login facebook
     * @param ctx
     * @param cpf
     * @param email
     */
    public void forgotPassword(Context ctx, String email, String cpf) {
        context = ctx;

        conn = new Connection(context);

        createConnection();

        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        cpf = cpf.replace("/", "");


        String param = "";
        try {
            param = "CpfCnpj=" + cpf + "&Email=" +URLEncoder.encode(email, "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing;
            conn.startConnection("Acesso/NovaSenha", param, 1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Forgot password login on
     * @param ctx
     * @param passwordNew
     */
    public void forgotPasswordLoginOn(Context ctx, String password, String passwordNew, boolean header){


        String param = "";
        try{
            context = ctx;

            conn = new Connection(context);

            createConnection();

            param = "pwd="+ URLEncoder.encode(passwordNew, "UTF-8") + "&OldPwd="+ URLEncoder.encode(password, "UTF-8") + "&CpfCnpj=" + URLEncoder.encode(loginBeans.getCpfCnpj() , "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing;

            conn.startConnection("Acesso/Senha", param, 1, header);
        } catch (Exception ex){
            ex.printStackTrace();
        }
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
                //Log.i(Config.TAG, "ChangePassword: " + result);

                try {

                    if (result.equals("")) {
                        onConnectionResult.onSucess();
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
     * Valied Field
     * @param email
     * @param cpf
     * @return
     */
    public String[] validField(Context context, String email, String cpf){

        String msg[] = new String[2];


        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        cpf  = cpf.replace("/", "");

        for(int ind = 0; ind < msg.length; ind++){
            msg[ind] = "";
        }

        if(cpf.equals("")){
            msg[0] = context.getString(R.string.message_empty_cpf_cnpj);
        } else if(!validCNPJ.isCNPJ(cpf) && !validCPF.isCPF(cpf)){
            msg[0] = context.getString(R.string.message_error_cpf_cnpj);
        }

        if(email.equals("")){
            msg[1] = context.getString(R.string.message_empty_email);
        }else if(!validEmail.checkemail(email)){
            msg[1] = context.getString(R.string.message_error_email);
        }


        return msg;
    }



    /**
     * Valied Field
     * @param password
     * @param passwordNew
     * @param cpf
     * @return
     */
    public String[] validFieldLoginOn(Context context, String password, String passwordNew, String confirmPassword, String cpf){

        String msg[] = new String[4];


        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        cpf  = cpf.replace("/", "");

        for(int ind = 0; ind < msg.length; ind++){
            msg[ind] = "";
        }

        if(cpf.equals("")){
         //   msg[0] = context.getString(R.string.message_empty_cpf_cnpj);
        } else if(!validCNPJ.isCNPJ(cpf) && !validCPF.isCPF(cpf)){
          //  msg[0] = context.getString(R.string.message_error_cpf_cnpj);
        }

        if(password.equals("")){
            msg[1] = context.getString(R.string.message_empty_password);
        }else if(!validPassword.checkPassword(password)){
            //msg[1] = context.getString(R.string.message_error_password);
        }

        if(passwordNew.equals("")){
            msg[2] = context.getString(R.string.message_empty_password);
        }else if(!validPassword.checkPassword(passwordNew)){
            msg[2] = context.getString(R.string.message_error_password);
        }

        if(!passwordNew.equals(confirmPassword)){
            msg[3] = context.getString(R.string.message_error_password_confirm);
        }


        return msg;
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
     * Get InfoUser
     * @return
     */
    public LoginBeans getInfoUser(){
        return loginBeans;
    }
    /**
     *
     * Get Image User
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser){
        return infoUser.getImageUser(context, ivImageUser);
    }

    /**
     * Open Screen Privacy Policy
     * @param context
     */
    public void openPrivacy(Context context){
        Intent it = new Intent(context, PrivacyPolicy.class);
        context.startActivity(it);
    }

    public void changePasswordExpired(Context context){
        loginBeans = infoUser.getUserInfo(context);
        loginBeans.setForceResetPassword(false);
        String json = gson.toJson(loginBeans);
        infoUser.saveUserInfo(json, context);
        Config.aleradyChangedPassword = true;
    }

}

