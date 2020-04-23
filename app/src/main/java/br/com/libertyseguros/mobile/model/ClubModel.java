package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URLEncoder;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.ClubBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.controller.ClubController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.DownloadImageClub;
import br.com.libertyseguros.mobile.libray.DownloadImageHome;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.ChangePassword;
import br.com.libertyseguros.mobile.view.ClubWebView;
import br.com.libertyseguros.mobile.view.Login;
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

    private boolean isloginOn;

    private ClubBeans clubBeans;

    //private String url = "http://libertyseguros.homolog.clubeben.proxy.media/auth/libertyseguros";
   // private String url = "http://libertyseguros.clubeben.com.br/auth/libertyseguros";

    private String url;
    private String postData;


    private DownloadImageClub dih;
    private String nameImage;



    /**
     * Get Post
     Data
     * @return
     */
    public String getPostData() {
        return postData;
    }


    /**
     * Get Url
     * @return
     */
    public String getUrl(Context context) {


        if(BuildConfig.prod){
            url = context.getString(R.string.url_club_prod);
        } else {
            url = context.getString(R.string.url_club_act);
        }

        return url;
    }



    public ClubModel(OnConnectionResult onConnectionResult, DownloadImageClub.OnClubImageDownloaded listener, Context context){
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        infoUser = new InfoUser();

        lb = infoUser.getUserInfo(context);


        if(lb.getAccess_token() != null) {
            isloginOn = true;
        } else {
            isloginOn = false;
        }


        dih = new DownloadImageClub(context,listener );

        nameImage = dih.getNameImage();

        dih.startDownload();
    }

    /**
     * Get Name file image Home
     * @return
     */
    public String getNameImage(){
        return nameImage;
    }

    /**
     * Get Club info
     * @param ctx
     */
    public void getClubAccess(Context ctx){
        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        String param = "";

        lb = new LoginBeans();
        lb = infoUser.getUserInfo(context);

        try{
            param = "varToken=" + URLEncoder.encode(lb.getAuthToken(), "UTF-8");
        } catch(Exception ex){
            ex.printStackTrace();
        }


        conn.startConnectionV2("Segurado/Clube/", param, 1, true);
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
                Log.i(Config.TAG, "ClubModel: " + result);

                try {
                    if(result.contains("sessionId")){
                        clubBeans = gson.fromJson(result, ClubBeans.class);
                        postData = clubBeans.getSessionId();
                        onConnectionResult.onSucess();
                    } else {
                        message = gson.fromJson(result, MessageBeans.class);
                        typeError = 2;
                        onConnectionResult.onError();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    message = gson.fromJson(result, MessageBeans.class);
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
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return typeError;
    }


    /**
     * get Login on
     * @return
     */
    public boolean isloginOn() {
        return isloginOn;
    }

    /**
     * Set Login on/off
     * @param isloginOn
     */
    public void setIsloginOn(boolean isloginOn) {
        this.isloginOn = isloginOn;
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
}
