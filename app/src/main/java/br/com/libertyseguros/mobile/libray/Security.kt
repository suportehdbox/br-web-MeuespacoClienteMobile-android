package br.com.libertyseguros.mobile.libray

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import br.com.libertyseguros.mobile.BuildConfig
import com.google.android.gms.common.wrappers.Wrappers
import com.google.firebase.analytics.FirebaseAnalytics
import com.scottyab.rootbeer.RootBeer
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


class Security {

    fun isDeviceCompliance(context: Context): Boolean {
        if (!this.isValidSignature(context)) {
            return false
        } else if (!this.isValidCrc(context)) {
            return false
        } else if ((isEmulatorDevice(context) && !BuildConfig.DEBUG)) {
            return false
        }

        return true
    }

    fun isRootedDevice(context: Context): Boolean {
        val rootBeer = RootBeer(context)
        return rootBeer.isRooted()
    }


    private fun isEmulatorDevice(context: Context): Boolean {
//        Log.v(Config.TAG, String.format("Prod: %s, Fing: %s, Man: %s, Hwd: %s,  ID: %s, Brd: %s, Model: %s, Bnd: %s, Dev: %s, Host: %s, Usr: %s",
//        Build.PRODUCT, Build.FINGERPRINT, Build.PRODUCT, Build.MANUFACTURER, Build.HARDWARE, Build.ID, Build.BOARD, Build.MODEL,
//                Build.BRAND, Build.DEVICE, Build.HOST, Build.USER))


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
//        Removido pois estava dando imcopatibildiade
//        else if( Build.USER.equals("android-build", true) ) {
//            Log.v(Config.TAG, "Emulator 01 - 07 : " + Build.USER)
//            return true
//        }
        else if (Build.BOARD.equals("unknown", true)) {
            FirebaseAnalytics.getInstance(context).logEvent("security_error", Bundle().apply {
                this.putString(FirebaseAnalytics.Param.ITEM_NAME, "BOARD")
                this.putString(FirebaseAnalytics.Param.CONTENT, Build.BOARD)
            })
            return true
        }

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

    @Throws(IOException::class)
    private fun isValidCrc(context: Context): Boolean {
//
//        val dexCrc: Long = Config.getCrc().toLong()
//        val zf = ZipFile(context.packageCodePath)
//        val ze: ZipEntry = zf.getEntry("classes.dex")
//        Log.v(Config.TAG,ze.getCrc().toString())
        return true//ze.crc.equals(dexCrc)
    }

    private fun getSignature(context: Context): String {
        val info: PackageInfo
        var signatureBase64: String = ""

        try {
            info = Wrappers.packageManager(context).getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                signatureBase64 = String(Base64.encode(md.digest(), 0))
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("Sign Base64 API < 28 ", signatureBase64!!)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
        return signatureBase64
    }

    private fun isValidSignature(context: Context): Boolean {
        if (getSignature(context).isEmpty()) {
            return false
        } else if (getSignature(context).contains(Config.getSignature())) {
            return true
        }
        return false
    }
}