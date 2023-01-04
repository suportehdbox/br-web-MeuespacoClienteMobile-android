package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;

import br.com.libertyseguros.mobile.model.HomeModel;

public class HomeController {

    private HomeModel homeModel;

    public HomeController(Activity activity, boolean token) {
        homeModel = new HomeModel(activity, token);
    }

    public void openSkipLogOff() {
        homeModel.openSkipLogOff();
    }

    /**
     * Open Screen Login Class
     */
    public void openLogin() {
        homeModel.openLogin();
    }

    /**
     * Open Screen Register
     */
    public void openRegister() {
        homeModel.openRegister();
    }

    public String getNameImage() {
        return homeModel.getNameImage();
    }

    public boolean shownPopUp(){
        return homeModel.shownPopUp();
    }
    /**
     * Open support
     * @param context
     */
    public void openSupport(Context context) {
        homeModel.openSupport(context);
    }

    public boolean isShowingDialog(){
        return homeModel.showingDialog();
    }
}