package br.com.libertyseguros.mobile.libray;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.libertyseguros.mobile.beans.FacebookBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoUser {
    private LoadFile loadFile;

    private Gson gson;

    private LoginBeans lb;

    public static Bitmap bmProfile;

    public InfoUser(){
        loadFile = new LoadFile();

        gson = new Gson();
    }

    /**
     * Save loading user
     * @param context
     * @return
     */
    public LoginBeans getUserInfo(Context context){
         lb = new LoginBeans();

        String userInfo = loadFile.loadPref(Config.TAG, context,  Config.TAGUSERINFO);

        if(userInfo != null){
            try{
                lb = gson.fromJson(userInfo, LoginBeans.class);
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }

        if(lb == null){
            lb = new LoginBeans();
        }
        return lb;
    }

    /**
     * Save user Info
     * @param result
     * @param context
     */
    public void saveUserInfo(String result, Context context){
        loadFile.savePref(Config.TAGUSERINFO, result, Config.TAG, context);
    }

    /**
     * Save user Info
     * @param result
     * @param context
     */
    public void saveClubTerms(Boolean agreed, String CPF, Context context){
        loadFile.savePref("Key_Agreed_"+CPF, agreed, Config.TAG, context);
    }

    public boolean getClubTerms(String CPF, Context context){
        return loadFile.loadBoolPref(Config.TAG, context, "Key_Agreed_"+CPF);
    }


    public String getCpfCnpj(Context context){
        return lb.getCpfCnpj();
    }

    /**
     * Get Image User
     * @param context
     * @param ci
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ci) {
        boolean isDrawable = false;

        LoadFile lf = new LoadFile();

        String name = getUserInfo(context).getUserName().replace(" ", "");
       /* name = name.replace(".","") + ".jpg";

        Drawable dr = lf.getImage(context, name);

        if(dr != null){
            ci.setImageDrawable(dr);
            isDrawable = true;
        }*/

        if (getUserInfo(context).getPhoto() == null) {
            getUserInfo(context).setPhoto("");
        }

        if (!getUserInfo(context).getPhoto().equals("")) {
            if(bmProfile == null) {

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    URL url;

                    if (getUserInfo(context).getPhoto().contains("graph.facebook.com")) {
                        url = new URL(getUserInfo(context).getPhoto().replace("=", ":") + "?redirect=false");

                        HttpURLConnection urlConnection = null;

                        try {
                            urlConnection = (HttpURLConnection) url.openConnection();

                            int responseCode = urlConnection.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                String json = readStream(urlConnection.getInputStream());

                                FacebookBeans facebookBeans = new Gson().fromJson(json, FacebookBeans.class);
                                url = new URL(facebookBeans.getData().getUrl());

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            url = new URL("error");
                        }
                    } else {
                        url = new URL(getUserInfo(context).getPhoto());
                    }

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream input = connection.getInputStream();
                    bmProfile = BitmapFactory.decodeStream(input);


                    ci.setImageBitmap(bmProfile);
                    isDrawable = true;


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                ci.setImageBitmap(bmProfile);
                isDrawable = true;
            }

        }

        return isDrawable;
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

}
