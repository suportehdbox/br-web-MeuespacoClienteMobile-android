package br.com.libertyseguros.mobile.libray

import android.R
import android.content.Context
import android.os.Build
import android.util.Log
import br.com.libertyseguros.mobile.BuildConfig
import com.scottyab.rootbeer.RootBeer
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


class Security {

    fun isDeviceCompliance(context: Context): Boolean {

        if ((isEmulatorDevice() && !BuildConfig.DEBUG) || !this.crcTest(context)) {
            return false
        }

        return true
    }

    fun isRootedDevice(context: Context) :Boolean
    {
        val rootBeer = RootBeer(context)
        return rootBeer.isRooted()
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.SUPPORTED_ABIS.toList().any { it.equals("armeabi", true) } &&
                        Build.SUPPORTED_ABIS.toList().any { it.equals("unknown", true) }) {
                    return true
                }
            } else {
                if (Build.CPU_ABI.equals("armeabi", true) &&
                        Build.CPU_ABI.equals("unknown", true)) {
                    return true
                }
            }
        }

        return false

    }

    @Throws(IOException::class)
    private fun crcTest(context: Context): Boolean {

        val dexCrc: Long = context.getString(br.com.libertyseguros.mobile.R.string.crc).toLong()
        val zf = ZipFile(context.packageCodePath)
        val ze: ZipEntry = zf.getEntry("classes.dex")

        return ze.getCrc() !== dexCrc
    }

}