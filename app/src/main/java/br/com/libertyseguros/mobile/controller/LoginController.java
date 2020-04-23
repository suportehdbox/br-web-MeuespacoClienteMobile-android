package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;

//import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageTypeTwoBeans;
import br.com.libertyseguros.mobile.model.LoginModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class LoginController {

    private LoginModel loginModel;

    public LoginController(Activity activity, OnConnectionResult onConnectionResult) {
        loginModel = new LoginModel(activity, onConnectionResult);
    }


    /**
     * Method login
     *
     * @param ctx
     * @param user
     * @param password
     * @param token
     */
    public void getLogin(Context ctx, String user, String password, boolean token) {
        loginModel.login(ctx, user, password, token);
    }

    /**
     * Method login
     *
     * @param ctx
     */
    public void getLoginToken(Context ctx) {
        loginModel.loginToken(ctx);
    }

    /**
     * Method login facebook
     *
     * @param ctx
     * @param email
     * @param idFacebook
     */
    public void getLoginRedesSociais(Context ctx, String email, String idFacebook, boolean isFacebook) {
        loginModel.getLoginRedesSociais(ctx, email, idFacebook, isFacebook);
    }

    /**
     * Validates fields login
     *
     * @param cpfEmail
     * @param password
     * @return
     */
    public String[] validField(Context ctx, String cpfEmail, String password) {
        return loginModel.validField(ctx, cpfEmail, password);
    }

    /**
     * Get Type Connection
     *
     * @return
     */
    public int getTypeConnection() {
        return loginModel.getTypeConnection();
    }

    public boolean isFacebook(){
        return loginModel.isFacebook();
    }

    /**
     * Get Type Error Connection
     *
     * @return
     */
    public int getTypeError() {
        return loginModel.getTypeError();
    }

    /**
     * Set Type Error Connection
     *
     * @param typeError
     */
    public void setTypeError(int typeError) {
        loginModel.setTypeError(typeError);
    }


    /**
     * Open Main Screen
     */
    public void OpenMainScreen(boolean loginUser) {
        loginModel.OpenMainScreen(loginUser);
    }

    /**
     * Open Screen Change Email
     */
    public void OpenChangeEmail(Context context) {
        loginModel.openChangePassword(context);
    }

    /**
     * Get Email Facebook
     *
     * @return
     */
    public String getEmailFacebook() {
        return loginModel.getEmailFacebook();
    }

    /**
     * Set email facebook
     *
     * @param emailFacebook
     */
    public void setEmailFacebook(String emailFacebook) {
        loginModel.setEmailFacebook(emailFacebook);
    }

    /**
     * Get id Facebook
     *
     * @return
     */
    public String getIdFacebook() {
        return loginModel.getIdFacebook();
    }

    /**
     * Set id Facebook
     *
     * @param idFacebook
     */
    public void setIdFacebook(String idFacebook) {
        loginModel.setIdFacebook(idFacebook);
    }

    /**
     * Open Screen Register
     *
     * @param context
     * @param email
     */
    public void openRegister(Activity context, String email) {
        loginModel.openRegister(context, email);
    }

    /**
     * Open Screen Main Class - LoginOff
     */
    public void openSkipLogOff(Activity activity) {
        loginModel.openSkipLogOff(activity);
    }


    /**
     * Open Screen Change Password
     *
     * @param context
     */
    public void openChangePassword(Context context) {
        loginModel.openChangePassword(context);
    }

    /**
     * Open Screen Change Password
     *
     * @param context
     */
    public void openChangeEmail(Context context) {
        loginModel.openChangeEmail(context);
    }

    /**
     * Open Home
     *
     * @param context
     */
    public void openHome(Activity context) {
        loginModel.openHome(context);
    }

    /**
     * Get boolean token Login
     *
     * @return
     */
    public boolean isTokenLogin() {
        return loginModel.isTokenLogin();
    }

    /**
     * Set boolean Token Login
     *
     * @param tokenLogin
     */
    public void setTokenLogin(boolean tokenLogin) {
        loginModel.setTokenLogin(true);
    }

    /**
     * Set name facebook
     *
     * @param name
     */
    public void setNameFacebook(String name) {
        loginModel.setNameFacebook(name);
    }

    /**
     * Set name facebook
     *
     * @return
     */
    public String getNameFacebook() {
        return loginModel.getNameFacebook();
    }

    /**
     * Get Login Beans
     *
     * @return
     */
    public LoginBeans getLoginBeans() {
        return loginModel.getLoginBeans();
    }

    /**
     * Permission Device ID
     *
     * @param activity
     */
    public void setPermission(Activity activity) {
        loginModel.setPermission(activity);
    }


    /**
     * Open Terms
     */
    public void openLinkTerms(Context context) {
        loginModel.openLinkTerms(context);
    }


    /**
     * Get message error
     *
     * @return
     */
    public MessageTypeTwoBeans getMessage() {
        return loginModel.getMessage();
    }

    /**
     * Get Enable Fingerprints enable
     *
     * @param context
     */
    public String isEnableFingerprints(Context context) {
        return loginModel.isEnableFingerprints(context);
    }

    /**
     * Enable Fingerprints disable
     *
     * @param context
     */
    public void disableFingerprints(Context context) {
        loginModel.disableFingerprints(context);
    }

    /**
     * Remove token
     */
    public void removeToken() {
        loginModel.removeToken();
    }

    /**
     * Set Photo Google
     * @return
     */
    public String getPhotoGoogle() {
        return loginModel.getPhotoGoogle();
    }

    /**
     * Get Photo Google
     * @param photoGoogle
     */
    public void setPhotoGoogle(String photoGoogle) {
        loginModel.setPhotoGoogle(photoGoogle);
    }


    /**
     * SingIn Google Plus
     *
     * @param context
     * @param mGoogleSignInClient
     * @param RC_SIGN_IN
     */
    public void signInGoogle(Activity context, GoogleSignInClient mGoogleSignInClient, int RC_SIGN_IN) {
        loginModel.signInGoogle(context, mGoogleSignInClient, RC_SIGN_IN);
    }

}
