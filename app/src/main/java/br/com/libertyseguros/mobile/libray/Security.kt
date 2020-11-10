package br.com.libertyseguros.mobile.libray

import android.content.Context
import android.os.Build
import com.scottyab.rootbeer.RootBeer

class Security {

     fun isDeviceCompliance(context: Context): Boolean {
        if(isEmulatorDevice()){
            return false
        }

        val rootBeer = RootBeer(context)
        if (rootBeer.isRooted()) {
            //we found indication of root
            return false
        }

        return true
    }


    private fun isEmulatorDevice(): Boolean {

        //STATIC CHECKING
        if (Build.PRODUCT.equals("google_sdk", true) ||
                Build.PRODUCT.equals("sdk_google_phone_x86", true) ||
                Build.PRODUCT.equals("sdk", true) ||
                Build.PRODUCT.equals("sdk_x86", true) ||
                Build.PRODUCT.equals("vbox86p", true) ||
                Build.FINGERPRINT.equals("generic", true) ||
                Build.MANUFACTURER.equals("genymotion", true) ||
                Build.MANUFACTURER.equals("unknown", true) ||
                Build.HARDWARE.equals("goldfish", true) ||
                Build.ID.equals("FRF91", true) ||
                Build.MODEL.equals("emulator", true) ||
                Build.MODEL.equals("sdk", true) ||
                Build.USER.equals("android-build", true) ||
                Build.MODEL.equals("Android SDK built for x86", true) ||
                Build.BOARD.equals("unknown", true)
        ) {
            return true
        }

        if (Build.BRAND.contains("generic", true) &&
                Build.DEVICE.contains("generic", true)) {
            return true
        }

        if (Build.HOST.contains("android-test", true)) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                if (Build.SUPPORTED_ABIS.toList().any { it.equals("armeabi", true) } &&
                        Build.SUPPORTED_ABIS.toList().any { it.equals("unknown", true) }) {
                    return true
                }
            }else{
                if(Build.CPU_ABI.equals("armeabi", true) &&
                        Build.CPU_ABI.equals("unknown", true) ){
                    return true
                }
            }
        }

        return false

    }

}