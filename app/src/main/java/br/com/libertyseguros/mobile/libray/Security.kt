package br.com.libertyseguros.mobile.libray

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import br.com.libertyseguros.mobile.BuildConfig
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scottyab.rootbeer.RootBeer


interface SecurityListener {
    public fun onSecurityCheckComplete(isCompliant: Boolean)
}

interface DeviceVerificaionListener {
    public fun onVerificationComplete()
    public fun onVerificationFail(message: String)
}


class Security {

    fun isDeviceCompliance(context: Activity, listener: SecurityListener) {
        if ((isEmulatorDevice(context) && !BuildConfig.DEBUG)) {
            listener.onSecurityCheckComplete(false)
        }


        listener.onSecurityCheckComplete(true)
    }


    fun getDeviceVerification(context: Activity, authToken: String, listener: DeviceVerificaionListener) {
        if (GoogleApiAvailability.getInstance()
                        .isGooglePlayServicesAvailable(context, 13000000) == ConnectionResult.SUCCESS) {
            SafetyNet.getClient(context).attest(generateNonce(context, authToken), "AIzaSyCyPgeh4Hi9nlPcsGHsKCIJPYZV4v_oPUw")
                .addOnSuccessListener(context) { response ->
                    // Indicates communication with the service was successful.
                    // This part should moved to backend - GetHomeAPI
                    val result = response.jwsResult
                    val parts = result.split(".")
                    if (parts.size == 3){
                        val jsonString = String(Base64.decode(parts.get(1).toString(), DEFAULT_BUFFER_SIZE))
                        val gson = Gson()
                        val mapType = object : TypeToken<Map<String, Any>>() {}.type

                        val dictionary: Map<String, Any> = gson.fromJson(jsonString, mapType)
                        if(dictionary.get("basicIntegrity") == true){
                            listener.onVerificationComplete();
                        }else{
                            listener.onVerificationFail("Integridade do smartphone comprometida, alterações no software não são permitidas.")
                        }
                    }
                }.addOnFailureListener(context) { e ->
                    // An error occurred while communicating with the service.
                    var msg = ""
                    if (e is ApiException) {
                        // An error with the Google Play services API contains some
                        // additional details.
                        val apiException = e as ApiException

                        // You can retrieve the status code using the
                        msg = String.format("Não foi possível validar o seu dispositivo: %d - %s", apiException.statusCode, apiException.localizedMessage)

                        if(apiException.statusCode == 7){
                            if(isConnected(context)){
                                //Possivel quebra de certificado!
                                listener.onVerificationFail("O seu dispositivo não está compatível com os requisitos para utilizar o aplicativo")
                            }
                        }
                        if (msg.isEmpty()) {
                            msg = "Não foi possível validar o seu dispositivo, verifique sua conexão ou bloqueios por firewall"
                        }

                        listener.onVerificationFail(msg)
                    } else {
                        // A different, unknown type of error occurred.
                        val d = e.localizedMessage
                        if (d != null) msg = d.toString()

                        listener.onVerificationComplete();
                    }

                }
        } else {
            listener.onVerificationFail("Por favor atualize o Google Play Service")
        }
    }


    fun isRootedDevice(context: Context): Boolean {
        val rootBeer = RootBeer(context)
        return rootBeer.isRooted()
    }

    @Suppress("DEPRECATION")
    private fun isConnectedOld(context: Context): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.activeNetworkInfo
        if (networkInfo != null) {
            return networkInfo.isConnected
        }
        return false

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnectedNewApi(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return capabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }

    private fun isConnected(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isConnectedNewApi(context)
        } else{
            isConnectedOld(context)
        }
    }

    private fun generateNonce(context: Context, seed: String): ByteArray {
        val newNounce = seed + Config.getDeviceUID(context)
        return newNounce.toByteArray()
    }



    private fun isEmulatorDevice(context: Context): Boolean {
        Log.v(Config.TAG, String.format("Prod: %s, Fing: %s, Man: %s, Hwd: %s,  ID: %s, Brd: %s, Model: %s, Bnd: %s, Dev: %s, Host: %s, Usr: %s",
                Build.PRODUCT, Build.FINGERPRINT, Build.MANUFACTURER, Build.HARDWARE, Build.ID, Build.BOARD, Build.MODEL,
                Build.BRAND, Build.DEVICE, Build.HOST, Build.USER))


        //STATIC CHECKING
        if (Build.PRODUCT.equals("google_sdk", true) ||
                Build.PRODUCT.equals("sdk_google_phone_x86", true) ||
                Build.PRODUCT.equals("sdk", true) ||
                Build.PRODUCT.equals("sdk_x86", true) ||
                Build.PRODUCT.equals("vbox86p", true)) {
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "Product")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.PRODUCT)
            })
            return true
        } else if (Build.FINGERPRINT.equals("generic", true)) {
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "FINGERPRINT")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.FINGERPRINT)
            })
            return true
        } else if (Build.MANUFACTURER.equals("genymotion", true) ||
                Build.MANUFACTURER.equals("unknown", true)) {
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "MANUFACTURER")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.MANUFACTURER)
            })
            return true
        } else if (Build.HARDWARE.equals("goldfish", true)) {
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "HARDWARE")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.HARDWARE)
            })
            return true
        } else if (Build.ID.equals("FRF91", true)) {
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "ID")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.ID)
            })
            return true
        } else if (Build.MODEL.equals("emulator", true) ||
                Build.MODEL.equals("sdk", true) ||
                Build.MODEL.equals("Android SDK built for x86", true)) {
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "MODEL")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.MODEL)
            })
            return true
        }
//        Removido pois estava dando imcopatibildiade em alguns devices
//        else if( Build.USER.equals("android-build", true) ) {
//            Log.v(Config.TAG, "Emulator 01 - 07 : " + Build.USER)
//            return true
//        }


        if (Build.BRAND.contains("generic", true) &&
                Build.DEVICE.contains("generic", true)) {
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "BRAND")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.BRAND)
            })
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "DEVICE")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.DEVICE)
            })
            return true
        }

        if (Build.HOST.contains("android-test", true)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.SUPPORTED_ABIS.toList().any { it.equals("armeabi", true) } &&
                        Build.SUPPORTED_ABIS.toList().any { it.equals("unknown", true) }) {
                    Log.v(Config.TAG, "Emulator 03")
                    return true
                }
            } else {
                if (Build.CPU_ABI.equals("armeabi", true) &&
                        Build.CPU_ABI.equals("unknown", true)) {
                    Log.v(Config.TAG, "Emulator 04")
                    return true
                }
            }
        }

        return false

    }
}