package br.com.libertyseguros.mobile.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import br.com.libertyseguros.mobile.BuildConfig
import br.com.libertyseguros.mobile.beans.LoginBeans
import br.com.libertyseguros.mobile.libray.Config
import br.com.libertyseguros.mobile.libray.Connection
import br.com.libertyseguros.mobile.libray.InfoUser
import br.com.libertyseguros.mobile.libray.LoadFile
import br.com.libertyseguros.mobile.util.OnConnection
import br.com.libertyseguros.mobile.view.Login
import java.net.URLEncoder


class LogoutModel(private val context: Context) {

    private lateinit var conn:Connection
    private lateinit var activity: Activity
    private val infoUser:InfoUser = InfoUser()
    private val loadFile:LoadFile = LoadFile()

    init {

    }

    private fun createConnection() {
        conn = Connection(context)
        conn.setOnConnection(object : OnConnection {
            override fun onError(msg: String) {}
            override fun onSucess(result: String) {
                //Log.i(Config.TAG, "SendToken: " + result);
                if (result == "") {
                    activity.finish()
                }
            }
        })
    }

    fun logout(_activity: Activity) {
        this.activity = _activity
        createConnection()

        var param = ""
        var lb = LoginBeans()

        lb = infoUser.getUserInfo(activity)

        val token: String = lb.authToken

        try {
            param = "?p1=1&IdDevice=" + URLEncoder.encode(Config.getDeviceUID(context), "UTF-8") + "&TokenAutenticacao=" + URLEncoder.encode(token, "UTF-8") + "&MarcaComercializacao=" + BuildConfig.brandMarketing
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        conn.startConnection("Acesso/Logout", param, 1, true)


        loadFile.savePref(Config.TAGHOMEON, "", Config.TAG, activity)
        loadFile.savePref(Config.TAGTOKEN, "0", Config.TAG, activity)
        infoUser.saveUserInfo("", activity)

        val it: Intent = Intent(activity, Login::class.java)
        activity.startActivity(it)


    }
}