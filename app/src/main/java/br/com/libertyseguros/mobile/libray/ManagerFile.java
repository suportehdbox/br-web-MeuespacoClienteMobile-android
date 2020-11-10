package br.com.libertyseguros.mobile.libray;


import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import br.com.libertyseguros.mobile.util.OnDownloadFile;
public class ManagerFile {

    private InputStream ip;

    private int idDownload;

    private OnDownloadFile onDownloadFile;

    private LoadFile lf;

    private Context context;

    private String path;

    private ThinDownloadManager downloadManager;

    private int type;

    /**
     * Constructor
     */
    public ManagerFile(int value, Context context) {
        this.context = context;

        ip = null;

        lf = new LoadFile();

        setType(value);

        downloadManager = new ThinDownloadManager();

    }


    /**
     * Checks for file
     * @param context
     * @param name
     * @return
     */
    public boolean isFile(Context context, String name) {
        boolean value = false;

        AssetManager assetManager = context.getAssets();
        ip = null;

        OutputStream out = null;

        try {
            ip = assetManager.open(name);
        } catch (Exception e) {
            //Log.e(Config.TAG, e.getMessage());
        }

        if (ip != null) {
            value = true;
        } else {
            File file = new File(path + "/" + name);
            if (file.canRead()) {
                value = true;
            }
        }

        return value;
    }

    /**
     * @param name
     * @param url
     * @param onDownload
     * @param context
     */
    public int download(String name, String url, OnDownloadFile onDownload, Context context) {

        this.context = context;
        this.onDownloadFile = onDownload;


        Uri downloadUri = Uri.parse(url);
        Uri destinationUri = Uri.parse(path + "/" + name);

        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadContext(context)//Optional
                .setStatusListener(new DownloadStatusListenerV1() {
                    @Override
                    public void onDownloadComplete(DownloadRequest downloadRequest) {
                        //Log.i(Config.TAG, "Download OK : " + downloadRequest.getDownloadId());
                        onDownloadFile.onFinish(downloadRequest.getDownloadId(), true, getNameFile(downloadRequest.getDestinationURI().toString()));
                    }

                    @Override
                    public void onDownloadFailed(DownloadRequest downloadRequest, int i, String s) {
                        //Log.i(Config.TAG, "Download Error : " + downloadRequest.getDownloadId() + " - ");
                        onDownloadFile.onFinish(downloadRequest.getDownloadId(), false,  getNameFile(downloadRequest.getDestinationURI().toString()));
                        downloadRequest.getDestinationURI();
                    }

                    @Override
                    public void onProgress(DownloadRequest downloadRequest, long l, long l1, int i) {
                    }
                });

        idDownload  = downloadManager.add(downloadRequest);

        //Log.i(Config.TAG, "Id download: " + idDownload + " name file: " + name + " " + " url: " + url);

        return idDownload;
    }

    /**
     * Cancel download
     */
    public void cancelDownload(){
        try{
            downloadManager.cancelAll();
        }catch (Exception ex){

        }
    }

    /**
     * Verify folder default
     * @param path
     * @return
     */
    public boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                //Log.i(Config.TAG, "Problem creating Image folder: " + path);
                ret = false;
            }
        }
        return ret;
    }

    /**
     * Set value type download file
     * 0 - Image
     * @param value
     */
    public void setType(int value){
        type = value;

        if(type == 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                path = context.getFilesDir().getAbsolutePath();
            }else {
                path = context.getFilesDir().getPath();
            }
        }
    }

    public String getNameFile(String pathFile){
        String name = "";

        try{
            String nameSplit[] = pathFile.split("/");

            name = nameSplit[nameSplit.length - 1];

        } catch(Exception ex){
            //Log.i(Config.TAG, "Erro ao montar nome do pdf");
        }

        return name;
    }
}