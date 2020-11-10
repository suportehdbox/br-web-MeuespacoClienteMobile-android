package br.com.libertyseguros.mobile.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.gson.Gson;

import java.net.URLEncoder;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageTypeTwoBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.util.ValidCNPJ;
import br.com.libertyseguros.mobile.util.ValidCPF;
import br.com.libertyseguros.mobile.util.ValidEmail;
import br.com.libertyseguros.mobile.util.ValidPassword;
import br.com.libertyseguros.mobile.view.ChangeEmail;
import br.com.libertyseguros.mobile.view.ChangePassword;
import br.com.libertyseguros.mobile.view.Home;
import br.com.libertyseguros.mobile.view.Main;
import br.com.libertyseguros.mobile.view.PrivacyPolicy;
import br.com.libertyseguros.mobile.view.Register;
import br.com.libertyseguros.mobile.view.fragment.HomeOff;

public class LoginModel extends BaseModel {

    private Context context;

    private Connection conn;

    private OnConnectionResult onConnectionResult;

    private ValidCPF validCPF;

    private ValidCNPJ validCNPJ;

    private ValidEmail validEmail;

    private ValidPassword validPassword;

    private int typeError;

    private int typeConnection;

    private String idFacebook;

    private String emailFacebook;

    private String nameFacebook;

    private String photoGoogle;

    private boolean tokenLogin;

    private boolean isFacebook;

    private LoginBeans lb;

    private static boolean closeScreen;

    private LoadFile lf;

    private MessageTypeTwoBeans message;

    public LoginModel(Context ctx, OnConnectionResult onConnectionResult) {
        this.context = ctx;

        this.onConnectionResult = onConnectionResult;

        lf = new LoadFile();

        lf.savePref(Config.TAGPOLICYNUMBER, "", Config.TAG, ctx);

        validCPF = new ValidCPF();

        validCNPJ = new ValidCNPJ();

        validEmail = new ValidEmail();

        validPassword = new ValidPassword();

        lb = new LoginBeans();

        lb = infoUser.getUserInfo(context);

        String token = loadFile.loadPref(Config.TAG, context, Config.TAGTOKEN);
        if (token != null && token.equals("1")) {
            tokenLogin = true;
        } else {
            tokenLogin = false;
        }

    }

    /**
     * Method login facebook
     *
     * @param ctx
     * @param email
     * @param idFacebook
     */
    public void getLoginRedesSociais(Context ctx, String email, String idFacebook, boolean isFacebook) {
        String param = "";
        this.idFacebook = idFacebook;
        this.isFacebook = isFacebook;

        try {
            context = ctx;

            conn = new Connection(context);

            typeConnection = 2;

            createConnection();

            String type;

            if (isFacebook) {
                type = "byFacebook";
            } else {
                type = "byGooglePlus";
            }

            tokenLogin = true;

            param = "grant_type=ControleAcesso&userId=" + URLEncoder.encode(email, "UTF-8") + "&idMidiaSocial=" + URLEncoder.encode(idFacebook, "UTF-8") + "&type=" + type + "&brandMarketing=" + BuildConfig.brandMarketing + "&deviceOS=" + Config.deviceOS + "&deviceModel=" + URLEncoder.encode(Build.MODEL, "UTF-8") + "&deviceId=" + URLEncoder.encode(Config.getDeviceUID(context), "UTF-8") + "&useToken=" + tokenLogin;

            param += "&AppVersion="+BuildConfig.VERSION_NAME;


            conn.startConnection("token", param, 1);
        } catch (Exception ex) {

        }


    }

    /**
     * Method login
     *
     * @param ctx
     * @param user
     * @param password
     * @param token
     */
    public void login(Context ctx, String user, String password, boolean token) {
        String param = "";
        idFacebook = "";
        try {
            context = ctx;
            conn = new Connection(context);

            typeConnection = 1;

            createConnection();

            param = "grant_type=ControleAcesso&type=UserAndPwd&userId=" + URLEncoder.encode(user, "UTF-8") + "&pwd=" + URLEncoder.encode(password, "UTF-8") + "&deviceOS=" +
                    Config.deviceOS + "&useToken=" + token + "&deviceId=" + URLEncoder.encode(Config.getDeviceUID(context), "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing + "&deviceModel=" + URLEncoder.encode(Build.MODEL, "UTF-8");

            param += "&AppVersion="+BuildConfig.VERSION_NAME;

            tokenLogin = token;
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        conn.startConnection("token", param, 1);

    }


    /**
     * Get boolean token Login
     *
     * @return
     */
    public boolean isTokenLogin() {
        return tokenLogin;
    }

    /**
     * Set boolean Token Login
     *
     * @param tokenLogin
     */
    public void setTokenLogin(boolean tokenLogin) {
        this.tokenLogin = tokenLogin;
    }

    /**
     * Method login token
     *
     * @param ctx
     */
    public void loginToken(Context ctx) {

        String param = "";

        try {
            context = ctx;
            typeConnection = 3;

            createConnection();

            param = "grant_type=ControleAcesso&type=byAuthToken&authToken=" + URLEncoder.encode(lb.getAuthToken(), "UTF-8") + "&deviceId=" + URLEncoder.encode(Config.getDeviceUID(context) + "", "UTF-8") + "&userId=" + URLEncoder.encode(lb.getCpfCnpj() + "", "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing + "&deviceModel=" + URLEncoder.encode(Build.MODEL, "UTF-8");
            //param = "grant_type=ControleAcesso&type=byAuthToken&authToken=" + URLEncoder.encode("coU1W2FqIkGlmxBUBWms1IRjdauSza0v747XJub74B0wSa8sLfNKTSPn6J48XoZZsmz7_6faKZ-KlfKeUAR9XNdtxGMQ_nYbtc6VD_wS7ja8SZAbXavf-K0WAcLOe9BW3bD0Lvvzyhxe_JA9kp6kRQE1WzwOJByvdxqqeMY7DiH1uiO6cmaoN544QIH7nkWsFh5JGLURqAiQFAMgw2OHwLsTZaiJ73be8i9lXUdeigPQPmN6k9r2o2m1ebfaOUDXuV_caQ64jLfhUULKJIKZYcP0F8kZ8adcmhy3n3NhxoaL7AYaIJYsGad-Jx5cKaJXsfZIVBjt4oZA51XXB55d24_sNiagonZoNn51566A_r5vbZzXKPnh-gq8BvXaM_3qSGT7wV-1o_kI3J9LTWZrw1Gp5lovt-scH4cFSJeJAdg", "UTF-8") + "&deviceId=" + URLEncoder.encode(Config.getDeviceUID(context) + "", "UTF-8") + "&userId=" + URLEncoder.encode( "32693587840", "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing + "&deviceModel=" + URLEncoder.encode(Build.MODEL, "UTF-8");

            param += "&AppVersion="+BuildConfig.VERSION_NAME;

            tokenLogin = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        conn.startConnection("Token", param, 1);

    }

    /**
     * Validates fields login
     *
     * @param cpfEmail
     * @param password
     * @return
     */
    public String[] validField(Context ctx, String cpfEmail, String password) {

        context = ctx;

        String msg[] = new String[2];

        for (int ind = 0; ind < msg.length; ind++) {
            msg[ind] = "";
        }

        if (password.length() == 0) {
            msg[1] = context.getString(R.string.message_empty_password);
        } else {
            if (!validPassword.checkPassword(password)) {
                //msg[1] = context.getString(R.string.message_error_password);
                msg[1] = "";
            } else {
                msg[1] = "";
            }
        }


        if (cpfEmail.length() == 0) {

            msg[0] = context.getString(R.string.message_empty_cpf_email);

        } else if (!cpfEmail.contains("@")) {

            if (!validCPF.isCPF(cpfEmail) && !validCNPJ.isCNPJ(cpfEmail)) {
                msg[0] = context.getString(R.string.message_error_cpf_cnpj);
            }

        } else {

            if (!validEmail.checkemail(cpfEmail)) {
                msg[0] = context.getString(R.string.message_error_email);
            }
        }

        return msg;

    }

    public int getTypeConnection() {
        return typeConnection;
    }

    public void setTypeConnection(int typeConnection) {
        this.typeConnection = typeConnection;
    }


    /**
     * Get Type of Error
     *
     * @return
     */
    public int getTypeError() {
        return typeError;
    }

    /**
     * Set Type of Error
     *
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.typeError = typeError;
    }

    /**
     * Open Main Screen
     */
    public void OpenMainScreen(boolean loginUser) {

        if (loginUser) {
            loadFile.savePref(Config.TAGLOGINUSER, "1", Config.TAG, context);

        } else {
            loadFile.savePref(Config.TAGLOGINUSER, "0", Config.TAG, context);
        }

        Intent it = new Intent(context, Main.class);
        it.putExtra("login", "1");
        context.startActivity(it);
        ((Activity) context).finish();


    }

    /**
     * Open Change Email Screen
     */
    public void OpenChangeEmail() {
        Intent it = new Intent(context, ChangeEmail.class);
        context.startActivity(it);
    }

    /**
     * Get Email Facebook
     *
     * @return
     */
    public String getEmailFacebook() {
        return emailFacebook;
    }

    /**
     * Set email facebook
     *
     * @param emailFacebook
     */
    public void setEmailFacebook(String emailFacebook) {
        this.emailFacebook = emailFacebook;
    }

    /**
     * Get id Facebook
     *
     * @return
     */
    public String getIdFacebook() {
        return idFacebook;
    }

    /**
     * Set id Facebook
     *
     * @param idFacebook
     */
    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    /**
     * Set name facebook
     *
     * @param name
     */
    public void setNameFacebook(String name) {
        this.nameFacebook = name;
    }

    /**
     * Set name facebook
     *
     * @return
     */
    public String getNameFacebook() {
        return nameFacebook;
    }

    /**
     * Set Photo Google
     *
     * @return
     */
    public String getPhotoGoogle() {
        return photoGoogle;
    }

    /**
     * Get Photo Google
     *
     * @param photoGoogle
     */
    public void setPhotoGoogle(String photoGoogle) {
        this.photoGoogle = photoGoogle;
    }

    /**
     * Open Screen Register
     *
     * @param context
     * @param email
     */
    public void openRegister(Activity context, String email) {
        Intent it = new Intent(context, Register.class);
        if (email != null) {
            it.putExtra("email", email);
            it.putExtra("idFacebook", idFacebook);
            it.putExtra("nameFacebook", nameFacebook);
            it.putExtra("isFacebook", isFacebook);
            it.putExtra("photoGoogle", photoGoogle);
        }

        Register.activityBefore = context;

        context.startActivity(it);
    }

    /**
     * Open Screen Main Class - LoginOff
     */
    public void openSkipLogOff(Activity activity) {

        infoUser.saveUserInfo("", activity);
        Intent it = new Intent(activity, Main.class);
        it.putExtra(Config.TAGLOGINON, "0");
        activity.startActivity(it);
        activity.finish();


    }


    /**
     * Open Screen Change Password
     *
     * @param context
     */
    public void openChangePassword(Context context) {
        Intent it = new Intent(context, ChangePassword.class);
        context.startActivity(it);
        Register.activityBefore_2 = (Activity) context;

    }


    /**
     * Open Screen Change Password
     *
     * @param context
     */
    public void openChangeEmail(Context context) {
        Intent it = new Intent(context, ChangeEmail.class);
        context.startActivity(it);
        Register.activityBefore_2 = (Activity) context;

    }

    /**
     * Open Home
     *
     * @param context
     */
    public void openHome(Activity context) {
        if (HomeOff.isHomeOff) {
            openSkipLogOff(context);
            HomeOff.isHomeOff = false;
        } else {
            Intent it = new Intent(context, Home.class);
            context.startActivity(it);
            context.finish();
        }


    }

    /**
     * Get boolean Close Screen
     *
     * @return
     */
    public static boolean isCloseScreen() {
        return closeScreen;
    }

    /**
     * Set boolean Close Screen
     *
     * @param closeScreen
     */
    public static void setCloseScreen(boolean closeScreen) {
        LoginModel.closeScreen = closeScreen;
    }

    private void createConnection() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try {
                    typeError = 1;
                    lf.savePref(Config.TAGTOKEN, "0", Config.TAG, context);
                    onConnectionResult.onError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess(String result) {
                if(!BuildConfig.prod) {
                    //Log.i(Config.TAG, "LoginModel: " + result);
                }


                Gson gson = new Gson();

                try {
                    lb = new LoginBeans();
                    lb = gson.fromJson(result, LoginBeans.class);
                    lb.setIdFacebook(idFacebook);


                    if (lb.getAccess_token() != null) {

                        String json = gson.toJson(lb);

                        infoUser.saveUserInfo(json, context);

                        if (tokenLogin) {
                            lf.savePref(Config.TAGTOKEN, "1", Config.TAG, context);
                        }

                        onConnectionResult.onSucess();
                    } else {
                        typeError = 2;
                        lf.savePref(Config.TAGTOKEN, "0", Config.TAG, context);

                        message = gson.fromJson(result, MessageTypeTwoBeans.class);

                        onConnectionResult.onError();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    onConnectionResult.onError();
                }
            }
        });

    }

    /**
     * Get Login Beans
     *
     * @return
     */
    public LoginBeans getLoginBeans() {
        return lb;
    }

    /**
     * Permission Device ID
     *
     * @param activity
     */
    public void setPermission(Activity activity) {
//        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{Manifest.permission.READ_PHONE_STATE,
//                            Manifest.permission.READ_PHONE_STATE},
//                    1);
//
//        } else {
//
//        }

    }

    /**
     * Open Terms
     */
    public void openLinkTerms(Context context) {
        Intent it = new Intent(context, PrivacyPolicy.class);
        context.startActivity(it);

    }


    /**
     * Get message error
     *
     * @return
     */
    public MessageTypeTwoBeans getMessage() {
        return message;
    }

    /**
     * Get Enable Fingerprints enable
     *
     * @param context
     */
    public String isEnableFingerprints(Context context) {
        String finger = "0";

        finger = lf.loadPref(Config.TAG, context, Config.TAGFINGERENALBE);

        if (finger == null) {
            finger = "0";
        }

        return finger;
    }

    /**
     * Enable Fingerprints disable
     *
     * @param context
     */
    public void disableFingerprints(Context context) {
        loadFile.savePref(Config.TAGFINGERENALBE, "0", Config.TAG, context);
    }

    /**
     * Remove token
     */
    public void removeToken() {
        lf.savePref(Config.TAGTOKEN, "0", Config.TAG, context);
    }

    public boolean isFacebook() {
        return isFacebook;
    }

    /**
     * SingIn Google Plus
     *
     * @param context
     * @param mGoogleSignInClient
     * @param RC_SIGN_IN
     */
    public void signInGoogle(Activity context, GoogleSignInClient mGoogleSignInClient, int RC_SIGN_IN) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        context.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
