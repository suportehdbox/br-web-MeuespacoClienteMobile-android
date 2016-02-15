/*
 * Copyright (c) 2011, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.libertyseguros.mobile.model.EventPhoto;

/**
 * @author N0053575 (Heidi Sturm)
 */
public final class ImageUtils
{
    /**
     * The logging tag
     */
    private static final String TAG = ImageUtils.class.getName();

    /**
     * @param photo
     * @return
     */
    public static Bitmap createThumbnailImage(EventPhoto photo, int size, float scale, Context context)
    {
        String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + photo.getPhotoPath();

        int desiredWidth;

        int outWidth = ImageUtils.getBitmapWidth(imagePath);
        int outHeight = ImageUtils.getBitmapHeight(imagePath);

        float imageRatio = (float) outHeight / (float) outWidth;
        int orientation = -1;
        if (imageRatio > 1)
        {
            // Portrait
            desiredWidth = (int) Math.ceil(size * scale);
        }
        else
        {
            // Landscape
            desiredWidth = (int) Math.ceil((size * scale) / imageRatio);
        }

        try
        {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, orientation);
        }
        catch (IOException e)
        {
            // Do nothing, just go with the default orientation, determined from the image ratio
            Log.i(TAG, "Could not determine orientation from Exif data");
        }

        // Log.v(TAG, "Orientation: " + orientation);
        Bitmap scaledBitmap = ImageUtils.scaleImage(imagePath, desiredWidth, orientation);
        if (scaledBitmap == null)
        {
            return null;
        }
        Bitmap croppedBitmap = ImageUtils.cropBitmap(scaledBitmap, (int) (size * scale), (int) (size * scale), context);
        Bitmap roundedBitmap = ImageUtils.getRoundedCornerBitmap(croppedBitmap, 6);
        return roundedBitmap;
    }

    /**
     * @param srcBitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap cropBitmap(Bitmap srcBitmap, int width, int height, Context context)
    {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        int x = 0;
        int y = 0;
        if (width < srcWidth)
        {
            x = (srcWidth - width) / 2;
        }
        if (height < srcHeight)
        {
            y = (srcHeight - height) / 2;
        }

        Bitmap output = Bitmap.createBitmap(srcBitmap, x, y, width, height);
        return output;
    }

    /**
     * @param imagePath
     * @return
     */
    public static int getBitmapHeight(String imagePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        return options.outHeight;
    }

    /**
     * @param imagePath
     * @return
     */
    public static int getBitmapWidth(String imagePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        return options.outWidth;
    }

    /**
     * @param imagePath
     * @return
     */
    public static int getMaximumBitmapDimension(String imagePath)
    {
        int width = getBitmapWidth(imagePath);
        int height = getBitmapHeight(imagePath);

        if (width > height)
        {
            return width;
        }

        return height;
    }

    /**
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radius)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = radius;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * This method will save a bitmap to a file.
     * 
     * @param bm
     *            The Bitmap to save
     * @param path
     *            The absolute path of the activity_location to save the bitmap.
     * @throws FileNotFoundException
     */
    public static void saveBitmapToFile(Bitmap bm, String path, Bitmap.CompressFormat compressFormat)
        throws FileNotFoundException
    {
        FileOutputStream out = new FileOutputStream(path);
        bm.compress(compressFormat, 90, out);
        try
        {
            out.close();
        }
        catch (IOException e)
        {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * @param imagePath
     * @param options
     * @param desiredWidth
     * @return
     */
    public static Bitmap scaleImage(String imagePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        int desiredWidth;

        float imageRatio = (float) srcWidth / (float) srcHeight;
        if (imageRatio > 1)
        {
            // Portrait
            desiredWidth = 1000;
        }
        else
        {
            // Landscape
            desiredWidth = (int) Math.ceil((1000) / imageRatio);
        }

        // Calculate the correct inSampleSize/scale value. This helps reduce memory use. It should be a power of 2
        // from: http://stackoverflow.com/questions/477572/android-strange-out-of-memory-issue/823966#823966
        int inSampleSize = 1;
        while (srcWidth / 2 >= desiredWidth)
        {
            srcWidth /= 2;
            srcHeight /= 2;
            inSampleSize *= 2;
        }

        float desiredScale = (float) desiredWidth / srcWidth;

        // Decode with inSampleSize
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = inSampleSize;
        options.inScaled = false;
        options.inPreferredConfig = Config.ARGB_8888;
        Bitmap sampledSrcBitmap = null;
        try
        {
        	System.gc();
            sampledSrcBitmap = BitmapFactory.decodeFile(imagePath, options);
        }
        catch (OutOfMemoryError e)
        {
            try
            {
                Log.e(TAG, "Bitmap creation failed, trying again after gc.");
                System.gc();
                sampledSrcBitmap = BitmapFactory.decodeFile(imagePath, options);
            }
            catch (OutOfMemoryError e2)
            {
                Log.e(TAG, "Out of memory error, reading file\n" + e2.getMessage() + "\n" + imagePath
                    + "\nSample Size: " + inSampleSize);
                return null;
            }
        }

        // Resize
        Matrix matrix = new Matrix();
        matrix.postScale(desiredScale, desiredScale);

        Bitmap scaledBitmap =
            Bitmap.createBitmap(sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(),
                matrix, true);

        return scaledBitmap;
    }

    /**
     * @param imagePath
     * @param options
     * @param desiredWidth
     * @return
     */
    public static Bitmap scaleImage(String imagePath, int desiredWidth, int orientation)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        int rotateDegrees = 0;
        boolean flipWidthHeight = false;
        // Rotate if needed
        switch (orientation)
        {
            case ExifInterface.ORIENTATION_NORMAL:
                rotateDegrees = 0;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotateDegrees = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotateDegrees = 270;
                flipWidthHeight = true;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotateDegrees = 90;
                flipWidthHeight = true;
                break;
            default:
                Log.w(TAG, "undefined orientation:  " + orientation);
                Log.w(TAG, "Defined values in ExifInterface:");
                Log.w(TAG, ExifInterface.ORIENTATION_FLIP_HORIZONTAL + " = ORIENTATION_FLIP_HORIZONTAL");
                Log.w(TAG, ExifInterface.ORIENTATION_FLIP_VERTICAL + " = ORIENTATION_FLIP_VERTICAL");
                Log.w(TAG, ExifInterface.ORIENTATION_TRANSPOSE + " = ORIENTATION_TRANSPOSE");
                Log.w(TAG, ExifInterface.ORIENTATION_TRANSVERSE + " = ORIENTATION_TRANSVERSE");
                Log.w(TAG, ExifInterface.ORIENTATION_UNDEFINED + " = ORIENTATION_UNDEFINED");
                break;
        }

        if (flipWidthHeight)
        {
            int tmp = srcWidth;
            srcWidth = srcHeight;
            srcHeight = tmp;
        }
        // Calculate the correct inSampleSize/scale value. This helps reduce memory use. It should be a power of 2
        // from: http://stackoverflow.com/questions/477572/android-strange-out-of-memory-issue/823966#823966
        int inSampleSize = 1;
        while (srcWidth / 2 > desiredWidth)
        {
            srcWidth /= 2;
            srcHeight /= 2;
            inSampleSize *= 2;
        }

        float desiredScale = (float) desiredWidth / srcWidth;

        // Decode with inSampleSize
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = inSampleSize;
        options.inScaled = false;
        options.inPreferredConfig = Config.ARGB_8888;
        Bitmap sampledSrcBitmap = null;
        try
        {
            sampledSrcBitmap = BitmapFactory.decodeFile(imagePath, options);
        }
        catch (OutOfMemoryError e)
        {
            try
            {
                Log.e(TAG, "Bitmap creation failed, trying again after gc.");
                sampledSrcBitmap = BitmapFactory.decodeFile(imagePath, options);
            }
            catch (OutOfMemoryError e2)
            {
                Log.e(TAG, "Out of memory error, reading file\n" + e2.getMessage() + "\n" + imagePath
                    + "\nSample Size: " + inSampleSize);
                return null;
            }
        }
        // Resize
        Matrix matrix = new Matrix();
        matrix.postScale(desiredScale, desiredScale);

        matrix.postRotate(rotateDegrees);

        Bitmap scaledBitmap =
            Bitmap.createBitmap(sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(),
                matrix, true);

        return scaledBitmap;
    }

    /**
     * private constructor to prevent instantiation
     */
    private ImageUtils()
    {
        super();
    }
}
