package br.com.libertyseguros.mobile.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;

import java.util.Arrays;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.LoginController;
import br.com.libertyseguros.mobile.libray.DownloadImageUser;
import br.com.libertyseguros.mobile.libray.FingerprintsAndroid;
import br.com.libertyseguros.mobile.model.LoginModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;


public class Login extends BaseActionBar implements View.OnClickListener {

    private final int RC_SIGN_IN = 998;

    private EditTextCustom etEmail;

    private EditTextCustom etPassword;

    private LoginController loginController;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private Dialog dialogFingerprint;

    private TextView tvMessageDialog;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private boolean value;

    private CallbackManager callbackManager;

    private DownloadImageUser downloadImageUser;

    private boolean loginUser;

    private FingerprintsAndroid fingerprintsAndroid;

    private Toast toast;

    private GoogleSignInClient mGoogleSignInClient;


    private String emailFB = "";

    public void onResume() {
        super.onResume();

        if (LoginModel.isCloseScreen()) {
            finish();
            LoginModel.setCloseScreen(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            loginController.openHome(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setTitle(getString(R.string.title_action_bar_0));

        mFirebaseAnalytics.setCurrentScreen(this, "Login", null);

        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        showLoading(true);
// aqui marcio
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Clique");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Login");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT, "Facebook Connect");

                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        loginController.setIdFacebook(loginResult.getAccessToken().getUserId() + "");



                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    try {
                                        if (object.has("name")) {
                                            loginController.setNameFacebook(object.getString("name"));
                                        } else {
                                            loginController.setNameFacebook("");
                                        }
                                        if (object.has("email")) {
                                            loginController.setEmailFacebook(object.getString("email"));

                                        } else {
                                            loginController.setEmailFacebook("");
                                        }

                                        loginController.setPhotoGoogle("");
                                        loginController.setIdFacebook(object.getString("id"));

                                        loginController.disableFingerprints(Login.this);
                                        loginController.getLoginRedesSociais(Login.this, loginController.getEmailFacebook(), loginController.getIdFacebook(), true);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();



                        Intent i = new Intent(getApplicationContext(), Register.class);
                        i.putExtra("emailFB",emailFB);
                        startActivity(i);


                    }

                    @Override
                    public void onCancel() {
                        Log.i("aqui", "aqui");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i("aqui", "aqui");
                    }
                });

        setContentView(R.layout.activon_login);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        SignInButton signInButton = findViewById(R.id.login_google);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText(getString(R.string.google_button));

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setBackgroundResource(R.drawable.bt_login_facebook);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.setText("Login");

        llContent = findViewById(R.id.ll_content);
        llLoading = findViewById(R.id.ll_loading);
        CheckBox cbLogin = findViewById(R.id.cb_login);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                if (accessToken == null) {

                    //LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
                    LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile"));
                    //LoginManager.getInstance().logInWithReadPermissions(Login.this, null);

                } else {

                    Log.i("aqui marcio", "aqui marcio");

                }
            }
        });
        cbLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                Bundle bundle1 = new Bundle();
                bundle1.putString(FirebaseAnalytics.Param.ITEM_ID, "Clique");
                bundle1.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Login");
                bundle1.putString(FirebaseAnalytics.Param.CONTENT, "Manter Logado");

                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle1);

            }
        });


        loginController = new LoginController(this, new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(() -> {
                        try {
                            switch (loginController.getTypeConnection()) {
                                case 1:
                                case 3:
                                    if (loginController.getTypeError() == 1) {
                                        dialogMessageTwoButton.show();
                                    } else if (loginController.getTypeError() == 2) {
                                        if (loginController.getMessage() != null) {
                                            tvMessageDialog.setText(loginController.getMessage().getMessage());
                                            dialogMessage.show();
                                        } else {
                                            dialogMessageTwoButton.show();
                                        }


                                    }
                                    break;
                                case 2:
                                    LoginManager.getInstance().logOut();
                                    mGoogleSignInClient.signOut();

                                    if (loginController.getTypeError() == 1) {
                                        dialogMessageTwoButton.show();
                                    } else if (loginController.getTypeError() == 2) {



                                        String x = loginController.getEmailFacebook();
                                        String jjj = "";

                                        loginController.openRegister(Login.this, loginController.getEmailFacebook());
                                    }
                                    break;
                            }

                            showLoading(false);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess() {
                try {
                    downloadImageUser = new DownloadImageUser(() -> runOnUiThread(() -> {
                        loginController.OpenMainScreen(loginUser);
                        showLoading(false);
                    }));

                    String name = loginController.getLoginBeans().getUserName().replace(".", "");
                    name = name.replace(" ", "") + ".jpg";

                    downloadImageUser.startDownload(Login.this, name, loginController.getLoginBeans().getPhoto()/*"https://www.facebook.com/images/fb_icon_325x325.png"/*loginController.getLoginBeans().getPhoto()*/);
                } catch (Exception ex) {
                    try {
                        runOnUiThread(() -> {
                            loginController.OpenMainScreen(loginUser);
                            showLoading(false);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        TextViewCustom tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgotPassword.setOnClickListener(this);

        TextViewCustom tvForgotEmail = findViewById(R.id.tv_forgot_email);
        tvForgotEmail.setPaintFlags(tvForgotEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgotEmail.setOnClickListener(this);

        TextViewCustom tvPrivacy = findViewById(R.id.tv_privacy);
        tvPrivacy.setPaintFlags(tvPrivacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvPrivacy.setOnClickListener(this);

        TextViewCustom tvRegister = findViewById(R.id.tv_not_user);
        tvRegister.setPaintFlags(tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvRegister.setOnClickListener(this);

        ButtonViewCustom btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

        ButtonViewCustom btSkip = findViewById(R.id.bt_skip);
        btSkip.setOnClickListener(this);

        LinearLayout llContentEditText = findViewById(R.id.ll_content_edittext);

        etEmail = new EditTextCustom(this);
        etPassword = new EditTextCustom(this);

        etEmail.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        llContentEditText.addView(etEmail.config("", getString(R.string.email_cpf), "", 1));
        llContentEditText.addView(etPassword.config("", getString(R.string.password), "", 3));

        etPassword.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);

        etPassword.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            login();
            return false;
        });


        configDialog();
        mGoogleSignInClient.signOut();
        LoginManager.getInstance().logOut();

    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_login) {
            login();
        } else if (id == R.id.tv_forgot_password) {
            loginController.openChangePassword(this);
        } else if (id == R.id.tv_forgot_email) {
            loginController.openChangeEmail(this);
        } else if (id == R.id.tv_not_user) {
            loginController.openRegister(this, null);
        } else if (id == R.id.bt_skip) {
            loginController.openSkipLogOff(Login.this);
        } else if (id == R.id.tv_privacy) {
            loginController.openLinkTerms(Login.this);
        } else if (id == R.id.login_google) {
            loginController.signInGoogle(this, mGoogleSignInClient, RC_SIGN_IN);
        }
    }

    /**
     * Login
     */
    private void login() {
        loginController.disableFingerprints(this);

        String[] msg = loginController.validField(this, etEmail.getText(), etPassword.getText());

        boolean error = false;

        for (int ind = 0; ind < msg.length; ind++) {
            if (msg[ind].length() != 0) {
                error = true;

                switch (ind) {
                    case 0:
                        etEmail.showMessageError(msg[0]);
                        break;
                    case 1:
                        etPassword.showMessageError(msg[1]);
                        break;
                }
            }
        }


        if (!error) {
            showLoading(true);
            loginUser = true;
            String x = etEmail.getText();

            loginController.getLogin(this, etEmail.getText(), etPassword.getText(), true);
        }
    }


    /**
     * Login Token
     */
    private void loginToken() {
        showLoading(true);
        loginUser = false;
        loginController.getLoginToken(this);
    }


    /**
     * Config Dialogs
     */
    private void configDialog() {

        dialogFingerprint = new Dialog(Login.this, R.style.AppThemeDialog);
        dialogFingerprint.setCancelable(false);
        dialogFingerprint.setContentView(R.layout.dialog_fingerprints);

        TextView tvCancelFinger = dialogFingerprint.findViewById(R.id.tv_cancel);

        tvCancelFinger.setOnClickListener(v -> {
            showLoading(false);
            loginController.removeToken();
            dialogFingerprint.dismiss();
        });

        dialogMessage = new Dialog(this, R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(v -> dialogMessage.dismiss());


        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(v -> dialogMessageTwoButton.dismiss());

        TextView tvTryAgain = dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(v -> {
            dialogMessageTwoButton.dismiss();
            showLoading(true);

            switch (loginController.getTypeConnection()) {
                case 1:
                    login();
                    break;
                case 2:

                    String x = loginController.getEmailFacebook();

                    loginController.getLoginRedesSociais(Login.this, loginController.getEmailFacebook(), loginController.getIdFacebook(), loginController.isFacebook());
                    break;
                case 3:
                    loginToken();
                    break;
            }
        });

        if (loginController.isTokenLogin()) {
            showLoading(true);

            if (loginController.isEnableFingerprints(Login.this).equals("1")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    fingerprintsAndroid = new FingerprintsAndroid(Login.this);

                    if (fingerprintsAndroid.isHardwareOk()) {
                        if (ActivityCompat.checkSelfPermission(Login.this, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Login.this,
                                    new String[]{android.Manifest.permission.USE_FINGERPRINT},
                                    1);
                        } else {
                            startFingerPrints();
                        }
                    }  // Log.i(Config.TAG, "Fingerprints not present");


                }
            } else {
                loginController.getLoginToken(this);
            }

        }


    }

    /* Show progress loading
     * @param v
     * @param m
     */
    private void showLoading(boolean v) {
        this.value = v;

        runOnUiThread(() -> {
            if (value) {
                llLoading.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.GONE);
            } else {
                llLoading.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginUser = true;
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            handleResult(result);

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();

            String name = account.getDisplayName();
            String email = account.getEmail();




            String photo = "";

            if (account.getPhotoUrl() != null) {
                photo = account.getPhotoUrl().toString();
            }

            String id = account.getId();

            loginController.setNameFacebook(name);
            loginController.setEmailFacebook(email);
            loginController.setIdFacebook(id);
            loginController.setPhotoGoogle(photo);

            showLoading(true);
            loginController.disableFingerprints(Login.this);
            loginController.getLoginRedesSociais(this, email, id, false);

            //Log.i(Config.TAG, "Google ok: " + name + " - " + email + " - " + id);
        } else {
            //Log.i(Config.TAG, "Google Error");
            Toast toast = Toast.makeText(this, getString(R.string.google_sign_error), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (permissions[0] == Manifest.permission.USE_FINGERPRINT) {
                    startFingerPrints();
                }
            } else {
                finish();
            }
        }
    }

    private void startFingerPrints() {
        dialogFingerprint.show();

        fingerprintsAndroid.start(new FingerprintsAndroid.OnFingerprintsListener() {
            @Override
            public void onError() {

                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(Login.this, getString(R.string.message_fingertips_error), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onSucess() {
                dialogFingerprint.dismiss();
                fingerprintsAndroid.cancel();

                loginToken();

            }
        });
    }

    @Override
    public void onBackPressed() {
        loginController.openHome(this);
    }
}


