package br.com.libertyseguros.mobile.libray;


import android.content.Context;
import android.util.Log;

import br.com.libertyseguros.mobile.util.OnDownloadFile;

public class DownloadImageUser {

    private ManagerFile managerFile;

    private OnFinishDownload onFinishDownload;

    public DownloadImageUser(OnFinishDownload onFinishDownload){
        this.onFinishDownload  = onFinishDownload;
    }

    /**
     * Start download image
     */
    public void startDownload(Context context, String namemImage, String url){
        OnDownloadFile onDownloadFile = new OnDownloadFile() {
            @Override
            public void onFinish(float id, boolean success, String nameFile) {
                onFinishDownload.onFinish();

                if (success){
                    //Log.i(Config.TAG, "Download Image user");
                } else {
                    //Log.i(Config.TAG, "Error Download Image user");

                }

            }
        };

        managerFile = new ManagerFile(0, context);

        if(!managerFile.isFile(context, namemImage)){

            managerFile.download(namemImage, url, onDownloadFile, context);
        } else {

            //Log.i(Config.TAG, "Image User found");
            onFinishDownload.onFinish();
        }

    }

    public interface OnFinishDownload{
        public void onFinish();
    }

    public void setOnFinishDownload(OnFinishDownload onFinishDownload){
        this.onFinishDownload = onFinishDownload;
    }
}
