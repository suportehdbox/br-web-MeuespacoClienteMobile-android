package br.com.libertyseguros.mobile.libray;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.libertyseguros.mobile.beans.DocumentsBeans;
import br.com.libertyseguros.mobile.beans.NotificationBeans;


public class DocumentsImageManager {

    private String extension = ".jpg";

    private DBCustom dbCustom;

    private Context context;


    public DocumentsImageManager(Context context) {
        this.context = context;

        dbCustom = new DBCustom(context);

        dbCustom.createTable("CREATE TABLE IF NOT EXISTS cached_image (id INTEGER PRIMARY KEY AUTOINCREMENT, limitDate STRING (255), fileName STRING (255))");

    }

    /**
     * Save Image
     *
     * @param b
     * @param name
     */
    public void saveImage(Bitmap b, String name) {
        FileOutputStream out;

        try {
            out = context.openFileOutput(name, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save Image
     *
     * @param name
     */
    private void deleteImage(String name) {
        try {
            context.deleteFile(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get isImageCache
     *
     * @param name
     * @return
     */
    public boolean isImageCache(String name) {
        boolean exists = false;

        try {
            FileInputStream fis = context.openFileInput(name);
            exists = true;
        } catch (Exception e) {
            exists = false;
        }

        return exists;
    }

    /**
     * Get Bitmap
     *
     * @param name
     * @return
     */
    public Bitmap getImageBitmap(String name) {
        try {
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        } catch (Exception e) {
        }
        return null;
    }

    public void deleteImageDocuments(ArrayList<String> ids, String extension) {
        for (int ind = 0; ind < ids.size(); ind++) {
            deleteImage(ids.get(ind) + extension);
            dbCustom.deleteBD("DELETE FROM cached_image WHERE fileName ='" + ids.get(ind) + extension + "'");
        }
    }

    public void saveImageDocuments(String base64, String name, String date) {
        saveImage(base64ToBitmap(base64), name);

        dbCustom.insertBD("INSERT INTO cached_image (limitDate, fileName) VALUES ('" + date + "','" + name + "')");
    }

    private Bitmap base64ToBitmap(String base64) {
        byte[] imageAsBytes = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


    public ArrayList<Bitmap> getArrayBitmapCache() {

        ArrayList<Bitmap> bm = new ArrayList<>();

        Cursor mCursor = dbCustom.selectBD("Select * From cached_image");

        if (mCursor.getCount() > 0) {

            int count = 0;
            while (mCursor.moveToNext()) {
                bm.add(getImageBitmap(mCursor.getString(mCursor.getColumnIndex("fileName"))));
            }

        }

        mCursor.close();

        dbCustom.close();

        return bm;
    }

    public void dataExpiration() {
        Cursor mCursor = dbCustom.selectBD("Select * From cached_image");

        if (mCursor.getCount() > 0) {
            DocumentsBeans.DocumentData documentsData[] = new DocumentsBeans.DocumentData[mCursor.getCount()];

            int count = 0;
            while (mCursor.moveToNext()) {

                verifyImageCache(mCursor.getString(mCursor.getColumnIndex("fileName")),
                        mCursor.getString(mCursor.getColumnIndex("limitDate")));
            }

        }

        mCursor.close();

        dbCustom.close();
    }

    public boolean verifyImageCache(String name, String dateExperation) {
        boolean remove = false;
        long millis = 0;
        dateExperation = dateExperation.replace("T", " ");
        dateExperation = dateExperation.replace("Z", "");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(dateExperation);
            millis = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (millis != 0) {
            if (System.currentTimeMillis() >= millis) {
                dbCustom.deleteBD("DELETE FROM cached_image WHERE fileName='" + name + "'");
                remove = context.deleteFile(name);
            }
        }
        return remove;
    }

}
