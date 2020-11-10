package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.Salesman;
import br.com.libertyseguros.mobile.beans.SalesmanBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class SupportModel extends BaseModel{
    private Activity context;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private LoginBeans loginBeans;

    private MessageBeans message;

    private ArrayList<SalesmanBeans> salesman;

    private Gson gson;

    private InfoUser infoUser;

    public SupportModel(OnConnectionResult onConnectionResult){
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        infoUser = new InfoUser();
    }

    /**
     * Login Checks if user is logged in
     * @param ctx
     * @return
     */
    public boolean isLogin(Context ctx){
        context = (Activity) ctx;
        loginBeans = infoUser.getUserInfo(context);

        boolean value = false;

        if(loginBeans.getAccess_token() != null){
            value = true;
        }

        return value;
    }


    /**
     * Call phone
     * @param ctx
     * @param index
     */
    public void call(Activity ctx, int index){
        this.context = ctx;

        String phone = "";
        switch(index){
            case 1:
                phone = ctx.getString(R.string.phone_1);
                break;
            case 2:
                phone = ctx.getString(R.string.phone_2);
                break;
            case 3:
                phone = ctx.getString(R.string.phone_3);
                break;
            case 4:
                phone = ctx.getString(R.string.phone_4);
                break;
            case 5:
                phone = ctx.getString(R.string.phone_5);
                break;
        }

        try{
            String number = phone;
            Uri number1 = Uri.parse("tel:" + phone);
            Intent dial = new Intent(Intent.ACTION_DIAL);
            dial.setData(number1);
            context.startActivity(dial);
        }catch (SecurityException ex){
            ex.printStackTrace();
        }

    }


    /**
     *
     * @param ctx
     */
    public void sendEmail(Activity ctx){
        this.context = ctx;

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ ctx.getString(R.string.sub_title_phone_5)});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");


        //emailIntent.setType("message/rfc822");

        try {
            context.startActivity(Intent.createChooser(emailIntent,
                    context.getString(R.string.send_email_title)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Method Array salesman
     * @param ctx
     */
    public void getArray(Context ctx){
        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        String param = "";

        conn.startConnectionV2("Segurado/Seguro/Corretora", param, 2, true);
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
                    //Log.i(Config.TAG, "SupportModel: " + result);

                    try {

                        if (result.contains("brokers")) {

                            convertBeans(result);

                            loadFile.savePref(Config.TAGASALESMAN, result, Config.TAG, context);

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
     * Get Salesman
     * @return
     */
    public  ArrayList<SalesmanBeans> getSalesman(){
        return salesman;
    }

    /**
     * Get Cache
     * @return
     */
    public ArrayList<SalesmanBeans> getCache(){
        String json = loadFile.loadPref(Config.TAG, context, Config.TAGASALESMAN);
        if(json != null){
            convertBeans(json);
        } else {
            salesman = new  ArrayList<SalesmanBeans>();
        }

        return salesman;
    }

    private void convertBeans(String json){
        Salesman sb;

        sb = gson.fromJson(json, Salesman.class);

        salesman = new  ArrayList<SalesmanBeans>();


        for(int ind = 0; ind < sb.getSalesmen().length; ind++){
            salesman.add(sb.getSalesmen()[ind]);
        }
    }

    /**
     * Call Skype
     * @param ctx
     */
    public void skype(Context ctx) {


        boolean isAppInstalled = appInstalledOrNot("com.skype.raider", ctx);

        if(isAppInstalled) {
            try {
                Intent sky = new Intent("android.intent.action.VIEW");
                sky.setData(Uri.parse("skype:" + "libertyseguros_central"));
                ctx.startActivity(sky);
            } catch (ActivityNotFoundException e) {
                //Log.e("SKYPE CALL", "Skype failed", e);
            }
        } else {
            final String appPackageName = context.getPackageName();
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.skype.raider")));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.skype.raider")));
            }

        }

    }

    private boolean appInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


}
