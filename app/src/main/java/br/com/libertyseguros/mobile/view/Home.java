package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.HomeController;
import br.com.libertyseguros.mobile.controller.RegisterController;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.listeners.WhatsAppClickListener;
import br.com.libertyseguros.mobile.model.RegisterModel;
import br.com.libertyseguros.mobile.util.AnalyticsApplication;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActivity;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.fragment.HomeOff;

public class Home extends BaseActivity implements View.OnClickListener {

    private RegisterController registerController;
    private HomeController homeController;
    private FirebaseAnalytics mFirebaseAnalytics;

    private TextView tvMessageDialog;

    private Dialog dialogMessage;
    private Dialog dialogMessageTwoButton;

    private ProgressDialog bar;

    private String registerActivation;

    private boolean token;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_home);


        LoadFile lf = new LoadFile();

        registerActivation = "";

        try {
            Intent intent = getIntent();
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                Uri uri = intent.getData();
                registerActivation = uri.getQueryParameter("token");

                token = registerActivation != null && !registerActivation.equals("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        HomeOff.isHomeOff = false;

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mFirebaseAnalytics = application.getFirebaseAnalitycs();
        mFirebaseAnalytics.setCurrentScreen(this, "Tela Inicial", null);

        homeController = new HomeController(this, token);
        ImageView ivBackground = (ImageView) findViewById(R.id.iv_background);


        if(BuildConfig.brandMarketing == 2) {
            Drawable dr = lf.getImage(this, homeController.getNameImage());

            if (dr != null) {
                ivBackground.setImageDrawable(dr);
            }
        } else {
            ivBackground.setBackgroundColor(getResources().getColor(R.color.background_home));
        }

        ButtonViewCustom btLoginSkip = (ButtonViewCustom) findViewById(R.id.bt_skip);
        btLoginSkip.setOnClickListener(this);

        ButtonViewCustom btWhats = (ButtonViewCustom) findViewById(R.id.bt_whatsapp);
        btWhats.setOnClickListener(new WhatsAppClickListener());

        ButtonViewCustom btLogin = (ButtonViewCustom) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

        ButtonViewCustom btRegister = (ButtonViewCustom) findViewById(R.id.bt_register);
        btRegister.setOnClickListener(this);

        ImageViewCustom ivSupport = (ImageViewCustom) findViewById(R.id.iv_support);
        ivSupport.setOnClickListener(this);

        printHashKey();
        configDialog();

        if (registerActivation != null && !registerActivation.equals("")) {
            registerController = new RegisterController(new OnConnectionResult() {
                @Override
                public void onError() {
                    bar.dismiss();
                    runOnUiThread(() -> {
                        if (registerController.getTypeError() == 1) {
                            dialogMessageTwoButton.show();
                        } else {
                            tvMessageDialog.setText(registerController.getRegisterActivationBeans().getMensagem().replace("\\\\n", System.getProperty("line.separator")));
                            dialogMessage.show();
                        }
                    });

                }

                @Override
                public void onSucess() {

                    runOnUiThread(() -> {
                        bar.dismiss();
                        tvMessageDialog.setText(registerController.getRegisterActivationBeans().getMensagem());
                        dialogMessage.show();
                    });

                }

            });
            bar.show();
            registerController.getRegisterActivation(this, registerActivation);
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_skip) {
            homeController.openSkipLogOff();
        } else if (id == R.id.bt_login) {
            homeController.openLogin();
        } else if (id == R.id.bt_register) {
            homeController.openRegister();
        } else if (id == R.id.iv_support) {
            homeController.openSupport(Home.this);

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Clique");
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Home Inicial");
            bundle.putString(FirebaseAnalytics.Param.CONTENT, "Facebook Connect");

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    private void printHashKey() {

        try {
            // FIXME: Android 4.4 Lower this can be exploited
            PackageInfo info = getPackageManager().getPackageInfo(
                    "br.com.libertyseguros.mobile",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnDataPlanChange(boolean freeNavigation) {


        if (!freeNavigation) {
            //msg = getString(R.string.paid_navigation);
            return;
        }
        String msg = getString(R.string.free_navigation);
        if (homeController.shownPopUp()) {
            return;
        }
        CoordinatorLayout Clayout = (CoordinatorLayout) findViewById(R.id.snackbarlocation);

        Snackbar snackbar = Snackbar.make(Clayout, msg, Snackbar.LENGTH_SHORT);
        snackbar.setDuration(4000);
        snackbar.show();
    }


    /**
     * Config Dialogs
     */
    private void configDialog() {

        dialogMessage = new Dialog(this, R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);
        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);
        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(v -> dialogMessage.dismiss());

        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);
        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> dialogMessageTwoButton.dismiss());

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(v -> {
            if (registerController.getTypeConnection() == RegisterModel.REGISTER_ACTIVATION) {
                bar.show();
                dialogMessageTwoButton.dismiss();
                registerController.getRegisterActivation(Home.this, registerActivation);
            }
        });

        bar = new ProgressDialog(this);
        bar.setCancelable(false);
        bar.setMessage(getString(R.string.loading_register));
        bar.setIndeterminate(true);
        bar.setCanceledOnTouchOutside(false);
    }



}
