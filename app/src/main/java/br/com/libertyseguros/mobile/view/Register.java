package br.com.libertyseguros.mobile.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.RegisterBeans;
import br.com.libertyseguros.mobile.controller.LoginController;
import br.com.libertyseguros.mobile.controller.RegisterController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.model.RegisterModel;
import br.com.libertyseguros.mobile.util.AnalyticsApplication;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.CepMaskUtil;
import br.com.libertyseguros.mobile.view.custom.DateMaskUtil;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.PlateMaskUtil;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;

public class Register extends BaseActionBar implements View.OnClickListener {
    public static Activity activityBefore;

    public static Activity activityBefore_2;

    public static Activity activityBefore_3;
    private static TextWatcher plate_watcher;
    private static TextWatcher cep_watcher;
    private static TextWatcher date_watcher;
    private RegisterController registerController;
    private LoginController loginController;
    private EditTextCustom etName;
    private EditTextCustom etPolicy;
    private EditTextCustom etCPF;
    private EditTextCustom etEmail;
    private EditTextCustom etEmailConfirm;
    private EditTextCustom etPassword;
    private EditTextCustom etPasswordConfirm;
    private TextView tvMsgError;
    private CheckBox cbRegister;
    private boolean value;
    private LinearLayout llLoading;
    private ScrollView llContent;
    private Dialog dialogMessage;
    private Dialog dialogMessageTwoButton;
    private Dialog dialogEditEmail;
    private TextView tvMessageDialog;
    private String idFacebook;
    private boolean isFacebook;
    private String photoGoogle;
    private boolean login;
    private ImageButton bt_auto;
    private ImageButton bt_home;
    private ImageButton bt_life;
    private boolean verified;

    private RelativeLayout relativeLayoutMain;
    private LinearLayout linearLayoutCaution;
    private String password;
    private String passwordConfirm;

    // key

    String TAG = Register.class.getSimpleName();
    String SITE_KEY = "6LeeL8wgAAAAAMmM4Pr9QdvkqUdRg5-VnQA5L5Vm";
    String SECRET_KEY = "6LeeL8wgAAAAAG5cocMGXE1Z8o9957TXG-SxqKhF";
    RequestQueue queue;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_register);

        queue = Volley.newRequestQueue(getApplicationContext());

        mFirebaseAnalytics.setCurrentScreen(this, "Cadastro", null);

        setTitle(getString(R.string.title_action_bar_3));

        String nameFacebook;
        String emailFacebook;



        //String email2 = Singleton.getInstance().getEmail();

        String email2 = AnalyticsApplication.email;

        System.out.println(email2);


        Intent myIntent = getIntent();

        // fazer um if caso o singleton estiver nulo
        // fazer um if caso o singleton estiver nulo
        // fazer um if caso o singleton estiver nulo
        // fazer um if caso o singleton estiver nulo


        String emailFB = myIntent.getStringExtra("emailFB");

        relativeLayoutMain = findViewById(R.id.relativeLayoutMain);

        try {

            emailFacebook = AnalyticsApplication.email;

            System.out.println(emailFacebook);

            idFacebook = getIntent().getStringExtra("idFacebook");
            nameFacebook = getIntent().getStringExtra("nameFacebook");

            isFacebook = getIntent().getBooleanExtra("isFacebook", false);
            photoGoogle = getIntent().getStringExtra("photoGoogle");

        } catch (Exception ex) {
            emailFacebook = "";
            idFacebook = "";
            nameFacebook = "";
            photoGoogle = "";
        }


        if (idFacebook == null) {
            emailFacebook = "";
            idFacebook = "";
            nameFacebook = "";
            photoGoogle = "";
        }

        loginController = new LoginController(this, new OnConnectionResult() {
            @Override
            public void onError() {
                showLoading(false);
                try {
                    runOnUiThread(() -> {

                        if (loginController.getTypeError() == 1) {
                            showDialogMessageTwoButton();
                        } else if (loginController.getTypeError() == 2) {
                            tvMessageDialog.setText(getString(R.string.message_error_login));
                            showDialogMessage();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }



            }

            @Override
            public void onSucess() {

                try {
                    runOnUiThread(() -> {
                        try {
                            showLoading(false);

                            Toast toast = Toast.makeText(Register.this, getString(R.string.register_ok), Toast.LENGTH_LONG);
                            toast.show();

                            if (activityBefore != null) {
                                try {
                                    activityBefore.finish();
                                    activityBefore = null;

                                } catch (Exception ex) {
                                    activityBefore = null;

                                }

                            }

                            if (activityBefore_2 != null) {
                                try {
                                    activityBefore_2.finish();
                                    activityBefore_2 = null;

                                } catch (Exception ex) {
                                    activityBefore_2 = null;

                                }
                            }

                            if (activityBefore_3 != null) {
                                try {
                                    activityBefore_3.finish();
                                    activityBefore_3 = null;

                                } catch (Exception ex) {
                                    activityBefore_3 = null;

                                }
                            }

                            loginController.OpenMainScreen(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        registerController = new RegisterController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(() -> {
                        try {

                            showLoading(false);

                            if (verified) {
                                RegisterBeans message = registerController.getRegisterMessage();

                                if (message.getMessageCode() != null) {


                                    if (message.getMessageCode().equalsIgnoreCase("ADD_USER_DUPLICATED_EMAIL")) {
                                        //Change e-mail

                                        showRegisterMessage(message.getMessage(), true);

                                    } else if (message.getMessageCode().equalsIgnoreCase("ADD_USER_INACTIVE")) {
                                        //send activation
                                        showActiveLink(message.getMessage());
                                        return;

                                    } else
                                        showRegisterMessage(message.getMessage(), !message.getMessageCode().equalsIgnoreCase("ADD_USER_DUPLICATED") &&
                                                !message.getMessageCode().equalsIgnoreCase("ADD_USER_DUPLICATED_CPFCNPJ"));
                                } else {
                                    showRegisterMessage(getString(R.string.message_error_server), false);

                                }
                            } else {
                                if (registerController.getTypeError() == 1) {
                                    showDialogMessageTwoButton();
                                } else {

                                    if (registerController.getMessage().getMessage() == null) {
                                        showDialogMessageTwoButton();
                                    } else {
                                        tvMessageDialog.setText(registerController.getMessage().getMessage());
                                        showDialogMessage();
                                    }
                                }
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

                    if (verified) {
                        runOnUiThread(() -> showRegisterSuccess());
                    } else {
                        verified = true;



                        System.out.println(etPassword.getText());
                        System.out.println(etEmail.getText());
                        System.out.println(photoGoogle);



                        registerController.registerSTEP2(Register.this, etPassword.getText(), etCPF.getText(), etEmail.getText(), photoGoogle);

                        etEmail.getTextInputLayout().setVisibility(View.GONE);


                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, idFacebook, isFacebook);

        llLoading = findViewById(R.id.ll_loading);

        llContent = findViewById(R.id.sv_content);

        TextView tvTitlePassword = findViewById(R.id.tv_title_password);

        linearLayoutCaution = findViewById(R.id.linearLayoutCaution);


        etName = new EditTextCustom(this);
        etPolicy = new EditTextCustom(this);

        etPolicy.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        plate_watcher = PlateMaskUtil.insert(etPolicy.getEditText());
        cep_watcher = CepMaskUtil.insert(etPolicy.getEditText());
        date_watcher = DateMaskUtil.insert(etPolicy.getEditText());

        etCPF = new EditTextCustom(this);

        etEmail = new EditTextCustom(this);
        etEmail.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        etPassword = new EditTextCustom(this);
        etPasswordConfirm = new EditTextCustom(this);

        LinearLayout llContent1 = findViewById(R.id.ll_content_field_1);
        LinearLayout llContent2 = findViewById(R.id.ll_content_field_2);
        LinearLayout llContent3 = findViewById(R.id.ll_content_field_3);

        if ("".equals(nameFacebook)) {
            etName.config("Name", getString(R.string.hint_name), "", 1);
        } else {
            //llContent1.addView(etName.config(nameFacebook, getString(R.string.hint_name), "", 1));
            etName.config("Name", getString(R.string.hint_name), "", 1);
        }

        llContent1.addView(etPolicy.config("", getString(R.string.register_apolice_cep), "", 2));
        etPolicy.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);

        llContent1.addView(etCPF.config("", getString(R.string.hint_cpf), "", 2));
        etCPF.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});


        if ("".equals(emailFacebook)) {
            llContent2.addView(etEmail.config("", getString(R.string.hint_email), "", 1));
//            llContent2.addView(etEmailConfirm.config("", getString(R.string.hint_confirm_email), "", 1));
        } else {
            etEmail.getEditText().setText(emailFacebook);


        }


        tvTitlePassword.setText(Html.fromHtml(getString(R.string.register_info_password)));

        if ("".equals(idFacebook)) {
            llContent3.addView(etPassword.config("", getString(R.string.hint_password), "", 3));
            llContent3.addView(etPasswordConfirm.config("", getString(R.string.hint_confirm_password), "", 3));
        } else {
            tvTitlePassword.setVisibility(View.GONE);

        }

        ButtonViewCustom btRegister = findViewById(R.id.bt_register);
        btRegister.setOnClickListener(this);

        tvMsgError = findViewById(R.id.tv_check_error);

        TextViewCustom tvTerms = findViewById(R.id.tv_terms);

        StringBuilder builderTerms = new StringBuilder();
        builderTerms.append(getString(R.string.register_cb_terms));

        builderTerms.append(" <u><a href='#'>");
        builderTerms.append(getString(R.string.register_cb_terms_2));

        builderTerms.append("</a></u>");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTerms.setText(Html.fromHtml(builderTerms.toString(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvTerms.setText(Html.fromHtml(builderTerms.toString()));
        }

        tvTerms.setOnClickListener(this);

        cbRegister = findViewById(R.id.cb_register);
        cbRegister.setOnCheckedChangeListener((buttonView, isChecked) -> tvMsgError.setVisibility(View.INVISIBLE));


        bt_auto = findViewById(R.id.ib_auto_register);
        bt_auto.setOnClickListener(this);
        bt_home = findViewById(R.id.ib_home_register);
        bt_home.setOnClickListener(this);
        bt_life = findViewById(R.id.ib_life_register);
        bt_life.setOnClickListener(this);

        setAutoSelected();

        if (BuildConfig.brandMarketing == 2) {
            bt_home.setVisibility(View.GONE);
            findViewById(R.id.iv_home).setVisibility(View.GONE);
            bt_life.setVisibility(View.GONE);
            findViewById(R.id.iv_life).setVisibility(View.GONE);
            View auto_view = findViewById(R.id.iv_auto_register);
            LinearLayout.LayoutParams childParam1 = (LinearLayout.LayoutParams) auto_view.getLayoutParams();
            childParam1.weight = 3f;
            auto_view.setLayoutParams(childParam1);
        }

        TextView lgpd = findViewById(R.id.tv_lgpd);
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.lgpd_grupo_liberty));

        builder.append(" <u><a href='");
        if (BuildConfig.prod) {
            builder.append(getString(R.string.url_canal_report_prod));
        } else {
            builder.append(getString(R.string.url_canal_report_act));
        }
        builder.append("'>");
        builder.append(getString(R.string.lgpd_grupo_liberty_link));

        builder.append("</a></u>");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lgpd.setText(Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            lgpd.setText(Html.fromHtml(builder.toString()));
        }

        lgpd.setOnClickListener(v -> registerController.openCanalReport(getApplicationContext()));
        configDialog();





        String email = AnalyticsApplication.email;
        System.out.println(email);

        etEmail.getEditText().setText(email);


        // aqui marcio
        /*
        if(email!=""){
            etEmail.getTextInputLayout().setVisibility(View.GONE);
            etEmail.removeLine();

            etPassword.getTextInputLayout().setVisibility(View.GONE);
            etPassword.removeLine();
            etPassword.hideIcon();

            etPasswordConfirm.getTextInputLayout().setVisibility(View.GONE);
            etPasswordConfirm.removeLine();
            etPasswordConfirm.hideIcon();

            tvTitlePassword.setVisibility(View.GONE);

            relativeLayoutMain.removeView(linearLayoutCaution);


            llContent3.removeAllViews();
            llContent2.removeAllViews();

       }
*/



        RegisterModel registerModel = new RegisterModel();
        String pswd = registerModel.generatePassword();

        // aqui continuar

        // passar variaveis

        password = pswd;
        passwordConfirm = pswd;



        etPassword.getEditText().setText(pswd);
        etPasswordConfirm.getEditText().setText(pswd);

    }


    @Override
    public void onClick(View view) {
        boolean bt_auto_1 = bt_auto.isSelected();
        boolean bt_home_1 = bt_home.isSelected();
        boolean bt_life_1 = bt_life.isSelected();

        int id = view.getId();
        if (id == R.id.bt_register) {
            String[] typePolicyAuto = {"24","26","31","42"};
            String[] typePolicyLife = {"69","77","80","81", "82", "91", "93", "98"};
            String[] typePolicyHome = {"14"};
            String policy = etPolicy.getText();


            if(etPolicy.getText() != null && !etPolicy.getText().isEmpty()){

                String prefixBoard = etPolicy.getText().substring(0,2);

                Boolean isPlace = policy.matches("^[a-zA-z][a-zA-z][a-zA-z]-[0-9][a-zA-z0-9][0-9][0-9]$");
                if (bt_auto_1 && !isPlace && !ArrayUtils.contains(typePolicyAuto, prefixBoard)) {
                    etPolicy.showMessageError("Opa! Algo está errado. Verifique as informações e tente novamente.");
                    return;
                }

                Boolean isCep = policy.matches("^[0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9]$");
                if (bt_home_1 && !isCep && !ArrayUtils.contains( typePolicyHome, prefixBoard ) ) {
                    etPolicy.showMessageError("Opa! Algo está errado. Verifique as informações e tente novamente.");
                    return;
                }

                Boolean isBirthday = policy.matches("^[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]$");
                if (bt_life_1 && !isBirthday && !ArrayUtils.contains( typePolicyLife, prefixBoard ) ) {
                    etPolicy.showMessageError("Opa! Algo está errado. Verifique as informações e tente novamente.");
                    return;
                }
            }


            String[] msg = registerController.validField(Register.this, etName.getText(), etPolicy.getText(), etCPF.getText(), etEmail.getText(), etEmail.getText(), password, passwordConfirm, cbRegister.isChecked());

            boolean error = false;

            for (int ind = 0; ind < msg.length; ind++) {
                if (msg[ind].length() != 0) {
                    if (!"".equals(idFacebook)) {
                        error = ind != 3 && ind != 4 && ind != 5 && ind != 6;
                    } else {
                        error = true;
                    }


                    switch (ind) {
                        case 0:
                            etName.showMessageError(msg[0]);
                            break;
                        case 1:
                            etPolicy.showMessageError(msg[1]);
                            break;
                        case 2:
                            etCPF.showMessageError(msg[2]);
                            break;
                        case 3:
                            etEmail.showMessageError(msg[3]);
                            break;
                        case 4:
                            etEmail.showMessageError(msg[4]);
                            break;
                        case 5:
                            etPassword.showMessageError(msg[5]);
                            break;
                        case 6:
                            etPasswordConfirm.showMessageError(msg[6]);
                            break;
                        case 7:
                            tvMsgError.setText(msg[7]);
                            tvMsgError.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            if (!error) {

                // Aqui register
                callRegister();
               /*

                SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                    .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                        @Override
                        public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {

                            if (!response.getTokenResult().isEmpty()) {
                                handleSiteVerify(response.getTokenResult());
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof ApiException) {
                                ApiException apiException = (ApiException) e;
                                Log.d(TAG, "Error message: " +
                                        CommonS tatusCodes.getStatusCodeString(apiException.getStatusCode()));
                            } else {
                                Log.d(TAG, "Unknown type of error: " + e.getMessage());
                            }
                        }
                    });
    */


            }
        } else if (id == R.id.tv_terms) {
            registerController.openLinkTerms(Register.this);
        } else if (id == R.id.ib_auto_register) {
            setAutoSelected();
        } else if (id == R.id.ib_home_register) {
            if (!bt_home.isSelected()) {
                etPolicy.getEditText().setText("");
                etPolicy.getEditText().removeTextChangedListener(plate_watcher);
                etPolicy.getEditText().removeTextChangedListener(date_watcher);
                etPolicy.getEditText().addTextChangedListener(cep_watcher);
                etPolicy.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                etPolicy.getTextInputLayout().setHint(getString(R.string.register_apolice_cep));
            }

            bt_auto.setSelected(false);
            bt_life.setSelected(false);
            bt_home.setSelected(true);
        } else if (id == R.id.ib_life_register) {
            if (!bt_life.isSelected()) {
                etPolicy.getEditText().setText("");
                etPolicy.getEditText().removeTextChangedListener(plate_watcher);
                etPolicy.getEditText().removeTextChangedListener(cep_watcher);
                etPolicy.getEditText().addTextChangedListener(date_watcher);
                etPolicy.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                etPolicy.getTextInputLayout().setHint(getString(R.string.register_apolice_date));
            }
            bt_auto.setSelected(false);
            bt_life.setSelected(true);
            bt_home.setSelected(false);
        }
    }

    protected  void handleSiteVerify(final String responseToken){
        Log.d(TAG, "handleSiteVerify: Success - " + responseToken);
        //it is google recaptcha siteverify server
        //you can place your server url
        String url = "https://www.google.com/recaptcha/api/siteverify";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success")){
                                showLoading(true);
                                callRegister();
                            } else{
                                Log.d(TAG, "Error message: recaptcha");
                            }
                        } catch (Exception ex) {
                            Log.d(TAG, "JSON exception: " + ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error message: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SECRET_KEY);
                params.put("response", responseToken);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        queue.add(request);
    }

    private void setAutoSelected() {
        if (!bt_auto.isSelected()) {
            etPolicy.getEditText().setText("");
            etPolicy.getEditText().removeTextChangedListener(cep_watcher);
            etPolicy.getEditText().removeTextChangedListener(date_watcher);
            etPolicy.getEditText().addTextChangedListener(plate_watcher);
            etPolicy.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            etPolicy.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPolicy.getTextInputLayout().setHint(getString(R.string.register_apolice_plate));

        }
        bt_auto.setSelected(true);
        bt_life.setSelected(false);
        bt_home.setSelected(false);
    }

    /**
     * Config Dialogs
     */
    private void configDialog() {
        dialogMessage = new Dialog(this, R.style.AppThemeDialog);
        dialogMessage.setCancelable(false);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(v -> {
            dismissDialogMessage();

            if (getString(R.string.register_ok).equals(tvMessageDialog.getText().toString())) {
                finish();
            }
        });

        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogMessageTwoButton.setCancelable(false);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(v -> dismissDialogMessageTwoButton());

        TextView tvTryAgain = dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(v -> {
            dismissDialogMessageTwoButton();
            showLoading(true);

            if (login) {
                login = false;
                loginController.getLogin(Register.this, etEmail.getText(), etPassword.getText(), false);
            } else {
//                    registerController.register(Register.this, etPolicy.getText(), etPassword.getText(), etCPF.getText(), etEmail.getText());
                callRegister();
            }

        });

    }

    private void showRegisterMessage(String message, boolean shouldEdit) {

        dialogEditEmail = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogEditEmail.setCancelable(false);
        TextView tvTryAgainEdit;
        if (shouldEdit) {
            dialogEditEmail.setContentView(R.layout.dialog_message_two_button);

            tvMessageDialog = dialogEditEmail.findViewById(R.id.tv_dialog_message);
            tvMessageDialog.setText(message);

            etEmail.showMessageError(getString(R.string.email_used));
            TextView tvCancelEdit = dialogEditEmail.findViewById(R.id.tv_cancel);
            tvCancelEdit.setText(getString(R.string.change_values));
            tvCancelEdit.setOnClickListener(v -> dialogEditEmail.dismiss());
            tvTryAgainEdit = dialogEditEmail.findViewById(R.id.tv_try_again);
        } else {
            dialogEditEmail.setContentView(R.layout.dialog_message);
            tvMessageDialog = dialogEditEmail.findViewById(R.id.tv_dialog_message);
            tvMessageDialog.setText(message);
            tvTryAgainEdit = dialogEditEmail.findViewById(R.id.tv_ok);
        }

        tvTryAgainEdit.setText(getString(R.string.goto_login));
        tvTryAgainEdit.setOnClickListener(v -> {
            dialogEditEmail.dismiss();
            registerController.goBackLogin(Register.this);
        });

        dialogEditEmail.show();
    }

    private void showActiveLink(String message) {

        dialogEditEmail = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogEditEmail.setCancelable(false);


        dialogEditEmail.setContentView(R.layout.dialog_message_two_button);

        tvMessageDialog = dialogEditEmail.findViewById(R.id.tv_dialog_message);
        tvMessageDialog.setText(message);


        TextView tvCancelEdit = dialogEditEmail.findViewById(R.id.tv_cancel);
        tvCancelEdit.setText(getString(R.string.bt_no));
        tvCancelEdit.setOnClickListener(v -> dialogEditEmail.dismiss());


        TextView tvTryAgainEdit = dialogEditEmail.findViewById(R.id.tv_try_again);
        tvTryAgainEdit.setText(getString(R.string.bt_yes));
        tvTryAgainEdit.setOnClickListener(v -> {
            dialogEditEmail.dismiss();
            registerController.sendActivtaionLink(Register.this, etCPF.getText());
            showLoading(true);
        });
        dialogEditEmail.show();
    }

    private void showRegisterSuccess() {

        dialogEditEmail = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogEditEmail.setCancelable(false);


        dialogEditEmail.setContentView(R.layout.dialog_register_success);

        Button btOk = dialogEditEmail.findViewById(R.id.bt_ok_success);
        btOk.setOnClickListener(v -> {
            dialogEditEmail.dismiss();
            registerController.goBackLogin(Register.this);
        });


        TextView tvSendAgain = dialogEditEmail.findViewById(R.id.tv_resend_link);

        tvSendAgain.setOnClickListener(v -> {
            dialogEditEmail.dismiss();
            registerController.sendActivtaionLink(Register.this, etCPF.getText());
            showLoading(true);

        });

        dialogEditEmail.show();


    }

    /* Show progress loading
     *
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

    private void callRegister() {
        verified = false;
        int typePolice = 0;
        if (bt_home.isSelected()) {
            typePolice = 1;
        } else if (bt_life.isSelected()) {
            typePolice = 2;
        }
        registerController.validRegister(Register.this, typePolice, etPolicy.getText(), etCPF.getText());
    }

    private void showDialogMessageTwoButton() {
        if (!(Register.this).isFinishing()) {
            dialogMessageTwoButton.show();
        }
    }

    private void dismissDialogMessageTwoButton() {
        if (!(Register.this).isFinishing()) {
            dialogMessageTwoButton.dismiss();
        }
    }

    private void showDialogMessage() {
        if (!(Register.this).isFinishing()) {
            dialogMessage.show();
        }
    }

    private void dismissDialogMessage() {
        if (!(Register.this).isFinishing()) {
            dialogMessage.dismiss();
        }
    }


    private String generatePassword() {

        //aqui

        String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@$*()+-";
        String numbers = "0123456789";
        SecureRandom rnd = new SecureRandom();

        int len = 5;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));

        sb.append(numbers.charAt(rnd.nextInt(numbers.length())));
        //Log.v((Config.TAG, sb.toString());

        return sb.toString();

    }
}
