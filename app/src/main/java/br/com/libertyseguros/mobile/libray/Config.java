package br.com.libertyseguros.mobile.libray;

import android.content.Context;

import br.com.libertyseguros.mobile.BuildConfig;

import android.provider.Settings;
import android.telephony.TelephonyManager;

public class Config {

    public final static int TIMEOUTLOCATION = 5000;

    public final static int COUNTTIMEOUTLOCATION = 10;

    public final static float ZOOMDEFAULT = 14.0f;

    public final static String TAG = BuildConfig.tag;

    public final static String TAGLOGINUSER = BuildConfig.tag + "loginUser";

    public final static String TAGFINGERENALBE = BuildConfig.tag + "tokenFingerEnable";

    public final static String TAGGCM = BuildConfig.tag + "GCM";

    public final static String TAGGCMSEND = BuildConfig.tag + "GCMSend";

    public final static String TAGTIMEIMAGE = BuildConfig.tag + "TimeImage";

    public final static String TAGLOGINON = BuildConfig.tag + "LoginOn";

    public final static String TAGUSERINFO = BuildConfig.tag + "UserInfo";

    public final static String TAGTOKEN = BuildConfig.tag + "Token";

    public final static String TAGASALESMAN = BuildConfig.tag + "Salesman";

    public final static String TAGHOMEON = BuildConfig.tag + "HomeOn";

    public final static String TAGHOMEONTIME = BuildConfig.tag + "HomeOnTime";

    public final static String TAGCHANGEEMAILQUESTION = BuildConfig.tag + "ChangeEmailQuestion";

    public final static String TAGPOLICYNUMBER = BuildConfig.tag + "policyNumber";


    public final static int deviceOS = 2;

    public final static int deviceID = 1234;

    public final static String nameFolderImage = TAG + "Image";

    public static boolean refreshScreen;

    public static String auto = "Auto";

    public static String property = "Property";

    public static String life = "Vida";

    public final static int fileSizeAllowed = 7000000;

    public final static String TAGNOTIFICAITONNEW = BuildConfig.tag + "NotificationNew";

    public final static String TAGDIALOGWORKSHOP = BuildConfig.tag + "DialogWorkshop";


    public final static int STOPAUDIO = 180000;

    public static String getDeviceUID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean alreadySentGCM = false;
    public static boolean alreadyShownDialog = false;
    public static boolean hasAutoPolicy = true;
    public static boolean hasHomeAssistance = false;

    public static boolean isAlreadyShownUpdate = false;

    public final static boolean ClaimWebView = true;
    public static boolean aleradyChangedPassword = false;
}
