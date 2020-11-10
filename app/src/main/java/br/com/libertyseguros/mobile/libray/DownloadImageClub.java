package br.com.libertyseguros.mobile.libray;


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import br.com.libertyseguros.mobile.util.OnDownloadFile;

public class DownloadImageClub {

    private int ind;

    private Context context;

    private ManagerFile managerFile;

    private String  typeImage;

    private LoadFile lf;

    private OnDownloadFile onDownloadFile;

    private VerifyConnection vc;

    private String url = "http://www.libertyseguros.com.br/MobileApp/segurado/android/";

    private String namemImage = "club.png";

    private long defaultTime = 604800000;

    private OnClubImageDownloaded listenerDownloadComplete;

    /**
     * Constructor
     * @param ctx
     */
    public DownloadImageClub(Context ctx, OnClubImageDownloaded listener){
        this.context = ctx;
        listenerDownloadComplete = listener;
        lf = new LoadFile();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        float density= context.getResources().getDisplayMetrics().density;

        if(density == 1.0){
            typeImage = "mdpi/" ;
        } else if(density == 1.5){
            typeImage = "hdpi/";
        } else if(density == 2.0){
            typeImage = "xhdpi/";
        }else {
            typeImage = "xxhdpi/";
        }

    }

    /**
     * Start download image
     */
    public void startDownload(){
        OnDownloadFile onDownloadFile = new OnDownloadFile() {
            @Override
            public void onFinish(float id, boolean success, String nameFile) {
                if(success){
                    lf.savePref(Config.TAGTIMEIMAGE, System.currentTimeMillis() + "", Config.TAG, context);
                    if(listenerDownloadComplete != null){
                        listenerDownloadComplete.OnDownloadComplete(nameFile);
                    }
                }
            }
        };

        vc = new VerifyConnection();

        managerFile = new ManagerFile(0, context);


        if(vc.typeConnection(context)){
            //Log.i(Config.TAG, "start download image WIFI");
            managerFile.download(namemImage, url + typeImage +  namemImage, onDownloadFile, context);
        } else {
            if(download3G()){
                managerFile.download(namemImage, url + typeImage + namemImage, onDownloadFile, context);
            }
        }
    }

    /**
     * Checks download time
     * @return
     */
    public boolean download3G(){

        String time = lf.loadPref(Config.TAG, context, Config.TAGTIMEIMAGE);

        if(time == null){
            //Log.i(Config.TAG, "start download image 3G, time == null");

            return true;
        } else {
            long timeLong = Long.parseLong(time);

            if((System.currentTimeMillis() - timeLong) < defaultTime){
                //Log.i(Config.TAG, "Not start download image 3G, time < 1 Week");
                return false;
            } else{
                //Log.i(Config.TAG, "start download image 3G, time > 1 Week");

                return true;
            }
        }


    }

    private String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    /**
     * Get name image
     * @return
     */
    public String getNameImage(){
        return namemImage;
    }


    public interface OnClubImageDownloaded{
        void OnDownloadComplete(String fileName);
    }
}
