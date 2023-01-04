package br.com.libertyseguros.mobile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import br.com.libertyseguros.mobile.libray.LoadFile;

/**
 * Assync load image
 */
public class AsyncLoadImage extends AsyncTask<Object, Object, Object> {
    private final AsyncLoadFileListener listener;
    private Bitmap bitmapLoaded;
    public AsyncLoadImage(AsyncLoadFileListener listener1){
        listener = listener1;
    }

    protected void onProgressUpdate(Object... progress) {
        //On progress!!
    }

    @Override
    protected Object doInBackground(Object... params) {
        LoadFile loadFile = new LoadFile();
        //Load Image from Uri
        try {
            bitmapLoaded = loadFile.loadImage((Uri) params[0], (Context) params[1], (int) params[2], (int) params[3]);
        }catch (Exception e) {
            e.printStackTrace();
            listener.onComplete(null);
        }

        return null;
    }

    protected void onPostExecute(Object result) {
        listener.onComplete(bitmapLoaded);
    }


    public interface AsyncLoadFileListener{
        void onComplete(Bitmap bmp);
    }
}
