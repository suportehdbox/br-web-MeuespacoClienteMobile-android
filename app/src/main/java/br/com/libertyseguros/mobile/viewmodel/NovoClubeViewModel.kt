package br.com.libertyseguros.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.libertyseguros.mobile.beans.LoginBeans
import br.com.libertyseguros.mobile.libray.InfoUser

class NovoClubeViewModel(application: Application) : AndroidViewModel(application){

    private lateinit var loginBeans: LoginBeans
    private val infoUser: InfoUser = InfoUser()

    var buttonEnabled : Boolean = false

    var isAgreed:Boolean = false
        set(value) {
            field = value
            this.buttonEnabled = value
        }


    fun agreedTerms(){
        infoUser.saveClubTerms(true, loginBeans.cpfCnpj, getApplication())
    }

    fun isLoggedIn() : Boolean{
        if (getAccessToken()===null){
            return false
        }
        return true;
    }

    private fun getAccessToken(): String? {
        if (!this::loginBeans.isInitialized) {
            loginBeans = infoUser.getUserInfo(getApplication())
        }

        return loginBeans.access_token
    }


}
