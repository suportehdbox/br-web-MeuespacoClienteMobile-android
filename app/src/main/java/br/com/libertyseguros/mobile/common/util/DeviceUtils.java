/*
 * Copyright (c) 2011, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;

/**
 * @author n0079911
 */
public final class DeviceUtils
{
    /**
     * This method will return the version name contained in the manifest file.
     * 
     * @return
     */
    public static String getVersionName(Context context)
    {
        String versionName = null;
        PackageInfo manager;
        try
        {
            manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = manager.versionName;
        }
        catch (NameNotFoundException e)
        {
            versionName = "";
        }

        return versionName;
    }

    /**
     * This method will return true if the device has a camera, else false
     * 
     * @param context
     * @return
     */
    public static boolean hasCamera(Context context)
    {
        PackageManager pm = context.getPackageManager();

        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * This method will return true if the device has a CDMA radio, else false
     * 
     * @param context
     * @return
     */
    public static boolean hasCDMARadio(Context context)
    {
        PackageManager pm = context.getPackageManager();

        return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA);
    }

    /**
     * This method will return true if the device has a GSM radio, else false
     * 
     * @param context
     * @return
     */
    public static boolean hasGSMRadio(Context context)
    {
        PackageManager pm = context.getPackageManager();

        return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_GSM);
    }

    /**
     * This method will return true if the device has a CDMA or GSM radio, else false
     * 
     * @param context
     * @return
     */
    public static boolean hasTelephony(Context context)
    {
        return hasCDMARadio(context) || hasGSMRadio(context);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private DeviceUtils()
    {

    }


    public static boolean isConnected(Context context) {

        /** << Liberty
         ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

         return (networkInfo == null) ? false : networkInfo.isConnected();
         >> */
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null && conectivtyManager.getActiveNetworkInfo().isAvailable() && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }
}
