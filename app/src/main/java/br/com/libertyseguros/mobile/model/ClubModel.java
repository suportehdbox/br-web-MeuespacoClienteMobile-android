package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import br.com.libertyseguros.mobile.beans.ClubBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.DownloadImageClub;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.ChangePassword;
import br.com.libertyseguros.mobile.view.ClubWebView;
import br.com.libertyseguros.mobile.view.Login;
import br.com.libertyseguros.mobile.view.PrivacyPolicy;
import br.com.libertyseguros.mobile.view.Register;

public class ClubModel extends BaseModel{

    public static Activity activityBefore;

    private Activity context;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private LoginBeans lb;

    private MessageBeans message;

    private Gson gson;

    private InfoUser infoUser;

    private ClubBeans clubBeans;


    /**
     * Get Post
     Data
     * @return
     */
    public String getToken() {
        return clubBeans.getToken();
    }


    /**
     * Get Url
     * @return
     */
    public String getUrl() {
        return clubBeans.getUrl();

    }



    public ClubModel(OnConnectionResult onConnectionResult, DownloadImageClub.OnClubImageDownloaded listener, Context context){
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        infoUser = new InfoUser();

        lb = infoUser.getUserInfo(context);
    }

    public LoginBeans getLb(){
        return lb;
    }

    public void saveAgreedTerms(Context context){
        infoUser.saveClubTerms(true, lb.getCpfCnpj(), context);
    }

    public Boolean getAgreedTerms(Context context){
        return infoUser.getClubTerms(lb.getCpfCnpj(), context);
    }

    /**
     * Get Club info
     * @param ctx
     */
    public void getClubAccess(Context ctx){
        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        lb = new LoginBeans();
        lb = infoUser.getUserInfo(context);


        conn.startConnectionV3("Segurado/Clube/", "", 2, true);
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
                }catch(Exception ex){
                    ex.printStackTrace();
                }


            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "ClubModel: " + result);

                try {
                    clubBeans = gson.fromJson(result, ClubBeans.class);
                    if(clubBeans.getSuccess()){
                        onConnectionResult.onSucess();
                    } else {
                        typeError = 2;
                        onConnectionResult.onError();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    typeError = 2;
                    onConnectionResult.onError();
                }
            }
        });
    }


    /**
     * Get message error
     * @return
     */
    public ClubBeans getMessage() {
        return clubBeans;
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return typeError;
    }


    /**
     * Open Login Screen
     * @param context
     */
    public void openLogin(Context context){
        Intent it = new Intent(context, Login.class);
        context.startActivity(it);
        ((Activity) context).finish();
        activityBefore.finish();
    }

    /**
     * Open Register Screen
     * @param context
     */
    public void openRegister(Context context){
        Register.activityBefore = (Activity) context;
        Register.activityBefore_2 = activityBefore;

        Intent it = new Intent(context, Register.class);
        context.startActivity(it);

    }

    /**
     * Open Forgot Password
     * @param context
     */
    public void openForgotPassword(Context context){

        Intent it = new Intent(context, ChangePassword.class);
        context.startActivity(it);
    }

    /**
     * Open Club Webview Screen
     * @param context
     */
    public void openClubWebview(Context context){
        Intent it = new Intent(context, ClubWebView.class);
        context.startActivity(it);
    }

    public void openTerms(Context context){
        Intent it = new Intent(context, PrivacyPolicy.class);
        context.startActivity(it);
    }
}
